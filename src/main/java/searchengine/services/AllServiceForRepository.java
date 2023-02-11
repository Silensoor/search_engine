package searchengine.services;


import searchengine.model.*;

public interface AllServiceForRepository {
     void savePage(EntityPage entityPage);
     Status findStatusSiteByUrl(String url);
     void saveSite(EntitySite entitySite);
     Integer findCountPageBySite(EntitySite entitySite);
     EntitySite findEntitySiteByUrl(String url);
     String findErrorSiteByUrl(String url);

     EntityLemma saveLemma(EntityLemma lemma);
     void saveIndex(EntityIndex entityIndex);

     EntityLemma findLemmaAndSite(String lemma,EntitySite entitySite);
     Integer countLemmasBySite(EntitySite entitySite);


}
