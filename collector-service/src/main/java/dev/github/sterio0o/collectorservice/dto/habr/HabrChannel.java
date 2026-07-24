package dev.github.sterio0o.collectorservice.dto.habr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "channel")
public record HabrChannel(
        @JacksonXmlProperty(localName = "title")
        String title,

        @JacksonXmlProperty(localName = "link")
        String link,

        @JacksonXmlProperty(localName = "description")
        String description,

        @JacksonXmlProperty(localName = "language")
        String language,

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        List<HabrItem> items
) {
}
