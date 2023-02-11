package searchengine.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import searchengine.model.*;
import searchengine.util.morphology.StartLemmaFind;
import searchengine.services.NetworkService;
import searchengine.services.AllServiceForRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

@Slf4j
public class ExecutorHtml extends RecursiveAction {
    private final String url;
    private final EntitySite entitySite;
    private static AllServiceForRepository allService;
    private static Pattern patternUrl;
    private static NetworkService network;
    public static boolean stop = false;
    private static final Set<String> SET_ABS_URLS = ConcurrentHashMap.newKeySet();
    private static final Set<String> SET_ABS_URLS_NOT_FOUNT = ConcurrentHashMap.newKeySet();


    public ExecutorHtml(String url, EntitySite entitySite) throws MalformedURLException {
        this.url = url.trim();
        this.entitySite = entitySite;

    }

    public ExecutorHtml(EntitySite entitySite, String url, AllServiceForRepository pageService, NetworkService network) throws IOException {
        this.entitySite = entitySite;
        this.url = url;
        ExecutorHtml.allService = pageService;
        ExecutorHtml.network = network;
        patternUrl = Pattern.compile("(jpg)|(JPG)|(PNG)|(png)|(PDF)|(pdf)|(JPEG)|(jpeg)|(BMP)|(bmp)");

    }

    @Override
    protected void compute() {

        CopyOnWriteArrayList<ExecutorHtml> tasks = new CopyOnWriteArrayList<>();

        try {
            if (stop) {
                StartLemmaFind.stop = true;
                StartExecutor.shutdown();
                stopIndexing();
                Thread.currentThread().interrupt();

            }
            Thread.sleep(150);
            Connection.Response response = network.getConnection(url);
            if (response.statusCode() != (HttpStatus.OK.value())) {
                SET_ABS_URLS_NOT_FOUNT.add(url);
                return;
            }
            if (!addAbsUrlToSet(url)) {
                SET_ABS_URLS_NOT_FOUNT.add(url);
                return;
            }
            //TODO update Site time
            updateSiteTime(entitySite);

            Document document = response.parse();
            //TODO Create and save page
            EntityPage entityPage = getEntityPage(document, url);
            allService.savePage(entityPage);

            //TODO Create and save lemmas
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> submit = executorService.submit(new StartLemmaFind(entitySite, allService, entityPage));
            submit.get();


            log.info("Добавлена запись " + url + " " + Thread.currentThread());

            Elements elements = document.select("a");
            for (Element element : elements) {
                String absUrl = element.absUrl("href").
                        indexOf(0) == '/' ? entitySite.getUrl() + element.absUrl("href") :
                        element.absUrl("href");

                if (!absUrl.isEmpty()
                        && !absUrl.contains("#")
                        && absUrl.startsWith(entitySite.getUrl())
                        && !patternUrl.matcher(absUrl).find()
                        && !SET_ABS_URLS.contains(absUrl)
                        && !SET_ABS_URLS_NOT_FOUNT.contains(absUrl)) {

                    ExecutorHtml executorHtml = new ExecutorHtml(absUrl, entitySite);
                    tasks.add(executorHtml);
                    executorHtml.fork();
                }
            }

            tasks.forEach(ForkJoinTask::join);

        } catch (SocketTimeoutException exception1) {
            addAbsUrlToSetNotFount(url);
            log.debug("Не подключился к сайту " + url + " " + exception1.getMessage());
        } catch (HttpStatusException exception2) {
            addAbsUrlToSetNotFount(url);
            log.debug("Сайт не доступен " + url + " " + exception2.getMessage());
        } catch (IOException exception3) {
            addAbsUrlToSetNotFount(url);
            log.debug("Ошибка подключения " + url + exception3.getMessage());
        } catch (Exception exception4) {
            log.debug("Прерывание потока " + exception4.getMessage());
        }

    }

    private EntityPage getEntityPage(Document document, String url) {
        EntityPage entityPage = new EntityPage();
        entityPage.setPath(getPath(url));
        entityPage.setSite(entitySite);
        entityPage.setCode(200);
        entityPage.setContent(document.outerHtml());
        return entityPage;
    }

    private String getPath(String url) {
        return url.substring(entitySite.getUrl().length());

    }

    private void updateSiteTime(EntitySite entitySite) {
        entitySite.setStatus_time(new Date());
        allService.saveSite(entitySite);
    }

    private void stopIndexing() {
        entitySite.setStatus(Status.FAILED);
        entitySite.setLast_error("Индексация остановлена пользователем");
        allService.saveSite(entitySite);

    }

    private boolean addAbsUrlToSet(String url) {
        return SET_ABS_URLS.add(url);

    }

    private void addAbsUrlToSetNotFount(String url) {
        SET_ABS_URLS_NOT_FOUNT.add(url);
    }

    public static void clearSET_ABSURL() {
        SET_ABS_URLS.clear();
        SET_ABS_URLS_NOT_FOUNT.clear();
    }

}
