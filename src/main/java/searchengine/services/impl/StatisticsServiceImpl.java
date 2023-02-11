package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.EntitySite;
import searchengine.model.Status;
import searchengine.services.AllServiceForRepository;
import searchengine.services.StatisticsService;

import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final AllServiceForRepository allService;

    private final SitesList sites;

    @Override
    public StatisticsResponse getStatistics() {
        TotalStatistics total = new TotalStatistics();
        total.setSites(sites.getSites().size());
        List<Site> sitesList = sites.getSites();
        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        total.setIndexing(false);
        for (Site site : sitesList) {
            Status statusSiteByUrl = allService.findStatusSiteByUrl(site.getUrl());
            if (statusSiteByUrl != null && statusSiteByUrl.equals(Status.INDEXING)) {
                total.setIndexing(true);
            }
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            EntitySite entitySite = allService.findEntitySiteByUrl(site.getUrl());
            int pages = allService.findCountPageBySite(entitySite) == null ? 0 : allService.findCountPageBySite(entitySite);
            item.setPages(pages);
            int lemmas = allService.countLemmasBySite(entitySite);
            item.setLemmas(lemmas);

            item.setStatus(statusSiteByUrl == null ? "" : statusSiteByUrl.toString());
            Constable errorSiteByUrl = allService.findErrorSiteByUrl(site.getUrl());
            item.setError(errorSiteByUrl == null ? "" : errorSiteByUrl.toString());
            item.setStatusTime(new Date().getTime());
            total.setPages(total.getPages() + pages);
            total.setLemmas(total.getLemmas() + lemmas);
            detailed.add(item);
        }

        return getResponse(total, detailed);
    }

    private StatisticsResponse getResponse(TotalStatistics totalStatistics, List<DetailedStatisticsItem> items) {
        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(totalStatistics);
        data.setDetailed(items);
        response.setStatistics(data);
        response.setResult(true);
        return response;
    }
}
