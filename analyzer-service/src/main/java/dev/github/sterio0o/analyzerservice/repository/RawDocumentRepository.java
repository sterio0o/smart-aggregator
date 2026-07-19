package dev.github.sterio0o.analyzerservice.repository;

import dev.github.sterio0o.common.util.AggregateContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RawDocumentRepository extends MongoRepository<AggregateContent, String> {

    List<AggregateContent> findAllByCategoriesIn(Collection<String> categories);

    List<AggregateContent> findAllByCategoriesInAndSourceNameIn(Collection<String> categories, Collection<String> types);

}
