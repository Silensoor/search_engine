package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.Response.FalseResponse;
import searchengine.dto.Response.IndexResponse;
import searchengine.dto.Response.SearchResponse;
import searchengine.dto.statistics.SearchDto;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.model.repositories.RepositorySite;
import searchengine.services.IndexingService;
import searchengine.services.SearchService;
import searchengine.services.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {


    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final RepositorySite repositorySite;
    private final SearchService searchService;


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {

        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<IndexResponse> startIndexing() {
        IndexResponse indexResponse = indexingService.startIndexing();
        return ResponseEntity.ok(indexResponse);

    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexResponse> stopIndexing() {
        IndexResponse indexResponse = indexingService.stopIndexing();
        return ResponseEntity.ok(indexResponse);

    }

    @PostMapping("/indexPage")
    public ResponseEntity<IndexResponse> indexPage(@RequestParam("url") String url) {
        IndexResponse indexResponse = indexingService.indexPage(url);
        return ResponseEntity.ok(indexResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "query", required = false, defaultValue = "") String query,
                                         @RequestParam(name = "site", required = false, defaultValue = "") String site,
                                         @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(name = "limit", required = false, defaultValue = "20") int limit) {
        if (query.isEmpty()) {
            return ResponseEntity.ok(new IndexResponse(false, "Задан пустой поисковый запрос"));
        } else {
            List<SearchDto> searchData;
            if (!site.isEmpty()) {
                if (repositorySite.findByUrl(site) == null) {
                    return ResponseEntity.ok(new IndexResponse(false, "Указанная страница не найдена"));
                } else {
                    searchData = searchService.siteSearch(query, site, offset, limit);
                }
            } else {
                searchData = searchService.allSiteSearch(query, offset, limit);
            }

            return new ResponseEntity<>(new SearchResponse(true, searchData.size(), searchData),
                    HttpStatus.OK);
        }
    }

}