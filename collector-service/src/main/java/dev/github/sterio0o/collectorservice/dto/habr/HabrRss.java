package dev.github.sterio0o.collectorservice.dto.habr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "rss")
public record HabrRss(
    @JacksonXmlProperty(localName = "version", isAttribute = true)
    String version,

    @JacksonXmlProperty(localName = "channel")
    HabrChannel channel
) {
}
