package com.endlesspassion.sigai.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPublicDataWriter<T> implements ItemWriter<List<T>> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void write(Chunk<? extends List<T>> chunk) throws Exception {
        List<T> allEntities = chunk.getItems().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (allEntities.isEmpty()) {
            log.info("No entities to write, skipping");
            return;
        }

        Class<T> entityClass = getEntityClass();
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entityClass);

        int upsertCount = 0;
        for (T entity : allEntities) {
            Query query = buildUniqueQuery(entity);

            Update update = buildUpdate(entity);

            bulkOps.upsert(query, update);
            upsertCount++;
        }

        bulkOps.execute();

        log.info("Successfully executed Bulk Upsert for {} {} entities to MongoDB",
                upsertCount,
                getEntityName());
    }

    protected abstract String getEntityName();

    protected abstract Class<T> getEntityClass();

    protected abstract Query buildUniqueQuery(T entity);

    protected abstract Update buildUpdate(T entity);
}
