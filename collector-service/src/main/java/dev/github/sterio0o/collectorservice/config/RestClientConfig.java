package dev.github.sterio0o.collectorservice.config;

import dev.github.sterio0o.collectorservice.exception.HabrRssException;
import dev.github.sterio0o.collectorservice.interfaces.HabrRssServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(5000);

        // Шаблон https://habr.com/ru/rss/hub/{название_хаба}/
        // https://habr.com/ru/rss/hubs/{название_хаба}/articles/all/
        return RestClient.builder()
                //.baseUrl("https://habr.com/ru/rss/articles/") хороший рабочий вариант
                .baseUrl("https://habr.com/ru/rss/hubs")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .defaultStatusHandler(HttpStatusCode::isError, (req, res) -> {
                    String status = res.getStatusText();
                    throw new HabrRssException("Ошибка Habr Rss: " + status);
                })
                .requestFactory(requestFactory)
                .build();
    }

    @Bean
    public HabrRssServiceClient rssServiceClient(RestClient restClient) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(HabrRssServiceClient.class);
    }

}
