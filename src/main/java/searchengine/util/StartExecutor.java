package searchengine.util;

import lombok.extern.slf4j.Slf4j;
import searchengine.config.Site;
import searchengine.model.EntitySite;
import searchengine.model.Status;
import searchengine.services.NetworkService;
import searchengine.services.AllServiceForRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.concurrent.*;

@Slf4j
public class StartExecutor implements Runnable {
    private final EntitySite entitySite;
    private final Site site;
    private static AllServiceForRepository allService;
    private static ForkJoinPool fjp;
    private static NetworkService network;


    public StartExecutor(EntitySite entitySite, Site site, AllServiceForRepository pageService, NetworkService network) {
        this.entitySite = entitySite;
        this.site = site;
        StartExecutor.allService = pageService;
        StartExecutor.network = network;
        fjp = new ForkJoinPool();

    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            ExecutorHtml executorHtml = new ExecutorHtml(entitySite, site.getUrl() + "/", allService, network);
            fjp.invoke(executorHtml);

            if (!fjp.isShutdown()) {
                entitySite.setStatus_time(new Date());
                entitySite.setStatus(Status.INDEXED);
                allService.saveSite(entitySite);
                log.info("Индексация сайта " + entitySite.getName() + " завершена, за время: " +
                        (System.currentTimeMillis() - startTime));
            }



        } catch (MalformedURLException exception1) {
            log.info("Ошибка старта индексации " + exception1.getMessage());
        } catch (IOException exception2) {
            log.info("Ошибка создания конструктора ExecutorHtml " + exception2.getMessage());
        }
    }


    public static void shutdown() {
        if (fjp != null && !fjp.isShutdown()) {
            fjp.shutdownNow();
        }
    }
}

