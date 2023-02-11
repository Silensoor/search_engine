package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.services.NetworkService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NetworkServiceImpl implements NetworkService {

    private final SitesList sitesList;
    @Override
    public Connection.Response getConnection(String url) throws IOException {
        return Jsoup.connect(url).
                ignoreContentType(true).
                userAgent(sitesList.getName())
                .referrer(sitesList.getReferer()).
                timeout(sitesList.getTimeout()).
                followRedirects(false).
                execute();
    }
}
