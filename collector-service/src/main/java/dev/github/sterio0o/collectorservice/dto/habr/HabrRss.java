package dev.github.sterio0o.collectorservice.dto.habr;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "rss")
public record HabrRss(
    @JacksonXmlProperty(localName = "version", isAttribute = true)
    String version,

    @JacksonXmlProperty(localName = "channel")
    HabrChannel channel
) {
}
