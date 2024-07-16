package Automobile.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Automobile.Entity.NewsApiResponse;
import Automobile.Entity.NewsArticle;

@Service
public class NewsService {
    @Value("${newsapi.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<NewsArticle> getUnifiedNews(String domain, String source) {
        List<NewsArticle> unifiedNews = new ArrayList<>();

        // Fetch third-party news
        String url = "https://newsapi.org/v2/everything?domains=" + domain + "&sources=" + source + "&apiKey=" + apiKey;
        NewsApiResponse response = restTemplate.getForObject(url, NewsApiResponse.class);

        if (response != null && response.getArticles() != null) {
            List<NewsArticle> thirdPartyNews = response.getArticles().stream()
                .map(article -> new NewsArticle(
                    article.getTitle(),
                    article.getDescription(),
                    article.getUrl(),
                    article.getUrlToImage(),
                    false
                ))
                .collect(Collectors.toList());
            unifiedNews.addAll(thirdPartyNews);
        }

        // Fetch custom news
        List<NewsArticle> customNews = getCustomNews();
        unifiedNews.addAll(customNews);

        return unifiedNews;
    }

    private List<NewsArticle> getCustomNews() {
        // For demonstration, return some dummy data. In a real application, fetch this data from your database.
        List<NewsArticle> customNews = new ArrayList<>();
        customNews.add(new NewsArticle("New Car Launch", "We have launched a new model...", "https://example.com/new-car", "https://example.com/new-car.jpg", true));
        customNews.add(new NewsArticle("Service Camp", "Join our free service camp...", "https://example.com/service-camp", "https://example.com/service-camp.jpg", true));
        return customNews;
    }
}


//
//@Service
//public class NewsService {
//
//    @Value("${newsapi.url}")
//    private String apiUrl;
//
//    
//
//    public String getNews() {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = apiUrl  ;
//        return restTemplate.getForObject(url, String.class);
//    }
//}