package dev.github.sterio0o.collectorservice.dto.habr;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "item")
public record HabrItem(
        @JacksonXmlProperty(localName = "title")
        String title,

        @JacksonXmlProperty(localName = "link")
        String link,

        @JacksonXmlProperty(localName = "description")
        String description,

        @JacksonXmlProperty(localName = "pubData")
        String pubData,

        @JacksonXmlProperty(localName = "guid")
        String guid,

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "category")
        List<String> categories
) {
}
