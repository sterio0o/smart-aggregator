package dev.github.sterio0o.collectorservice.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "raw")
@Getter
@Setter
public class RawDocument {
    @Id
    private String id;
    private String userId;
    private String sourceName;
    private Map<String, Object> rawPayload;

    @Indexed(expireAfter = "24h")
    private Instant createdAt;
}
