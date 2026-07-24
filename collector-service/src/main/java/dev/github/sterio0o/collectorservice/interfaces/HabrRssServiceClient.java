package dev.github.sterio0o.collectorservice.interfaces;

import dev.github.sterio0o.collectorservice.dto.habr.HabrRss;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = "application/xml")
public interface HabrRssServiceClient {

    @GetExchange("/{hubName}/articles/all/")
    HabrRss getHabrRssFeeds(@PathVariable String hubName);

}
