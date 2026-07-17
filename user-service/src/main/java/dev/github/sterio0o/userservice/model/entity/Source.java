package dev.github.sterio0o.userservice.model.entity;

import dev.github.sterio0o.common.util.AdapterType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "sources")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private AdapterType name;

    @Column(name = "source_url", nullable = false, columnDefinition = "TEXT")
    private String  sourceUrl;

    @ManyToMany(mappedBy = "sources")
    private Set<User> users;

    @PrePersist
    public void onCreate() {

    }
}