package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.model.*;
import searchengine.model.repositories.RepositoryIndex;
import searchengine.model.repositories.RepositoryLemma;
import searchengine.model.repositories.RepositoryPage;
import searchengine.model.repositories.RepositorySite;
import searchengine.services.AllServiceForRepository;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class AllServiceForRepositoryImpl implements AllServiceForRepository, Serializable {
    private final RepositoryPage repositoryPage;
    private final RepositorySite repositorySite;
    private final RepositoryLemma repositoryLemma;
    private final RepositoryIndex repositoryIndex;

    @Override
    public void savePage(EntityPage entityPage) {
        repositoryPage.saveAndFlush(entityPage);
    }

    @Override
    public Status findStatusSiteByUrl(String url) {
        return repositorySite.findByUrl(url);
    }

    @Override
    public void saveSite(EntitySite entitySite) {
        repositorySite.saveAndFlush(entitySite);
    }

    @Override
    public Integer findCountPageBySite(EntitySite entitySite) {
        return repositoryPage.findCountBySite(entitySite);
    }

    @Override
    public EntitySite findEntitySiteByUrl(String url) {
        return repositorySite.findEntitySiteByUrl(url);
    }

    @Override
    public String findErrorSiteByUrl(String url) {
        return repositorySite.findErrorByUrl(url);
    }


    @Override
    public EntityLemma saveLemma(EntityLemma lemma) {
        return repositoryLemma.saveAndFlush(lemma);
    }

    @Override
    public void saveIndex(EntityIndex entityIndex) {
        repositoryIndex.saveAndFlush(entityIndex);
    }

    @Override
    public EntityLemma findLemmaAndSite(String lemma, EntitySite entitySite) {
        return repositoryLemma.findByLemmaAndSite(lemma, entitySite);
    }

    @Override
    public Integer countLemmasBySite(EntitySite entitySite) {
        return repositoryLemma.countBySite(entitySite);
    }


}
