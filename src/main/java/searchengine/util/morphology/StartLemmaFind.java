package searchengine.util.morphology;

import lombok.extern.slf4j.Slf4j;
import searchengine.model.EntityIndex;
import searchengine.model.EntityLemma;
import searchengine.model.EntityPage;
import searchengine.model.EntitySite;
import searchengine.services.AllServiceForRepository;
import searchengine.util.ClearHtmlCode;

import java.io.IOException;
import java.util.*;

@Slf4j

public class StartLemmaFind implements Runnable {
    private static AllServiceForRepository allService;
    private final EntitySite entitySite;
    private static final Morphology morphology = new MorphologyAnalyzer();
    public volatile static boolean stop = false;
    private final EntityPage entityPage;


    public StartLemmaFind(EntitySite entitySite, AllServiceForRepository allService, EntityPage entityPage) {

        this.entitySite = entitySite;
        StartLemmaFind.allService = allService;
        stop = false;
        this.entityPage = entityPage;

    }

    @Override
    public void run() {
        try {
            startLemmaFinder(entityPage);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

    }
    private void startLemmaFinder(EntityPage entityPage) throws IOException {
        String title = ClearHtmlCode.clear(entityPage.getContent(), "title");
        String body = ClearHtmlCode.clear(entityPage.getContent(), "body");
        String x = title.concat(" " + body);
        HashMap<String, Integer> textLemmaList = morphology.getLemmaList(x);
        Set<String> allWords = new HashSet<>(textLemmaList.keySet());
        for (String lemma : allWords) {
            synchronized (EntityLemma.class) {
                Integer count = textLemmaList.get(lemma);
                if (stop) {
                    continue;
                }
                saveLemmaAndIndex(lemma, entityPage, count);
            }
        }
    }

    private void saveLemmaAndIndex(String lemma, EntityPage entityPage, Integer count) {
        EntityLemma lemma1 = allService.findLemmaAndSite(lemma, entitySite);
        if (lemma1 == null) {
            EntityLemma entityLemma = new EntityLemma();
            entityLemma.setSite(entitySite);
            entityLemma.setLemma(lemma);
            entityLemma.setFrequency(1);
            lemma1 = allService.saveLemma(entityLemma);
        } else {
            lemma1.setFrequency(lemma1.getFrequency() + 1);
            allService.saveLemma(lemma1);
        }
        EntityIndex entityIndex = new EntityIndex();
        entityIndex.setPage(entityPage);
        entityIndex.setRank(count);
        entityIndex.setLemma(lemma1);
        allService.saveIndex(entityIndex);
    }
}
