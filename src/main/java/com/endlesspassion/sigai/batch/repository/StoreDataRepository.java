package com.endlesspassion.sigai.batch.repository;

import com.endlesspassion.sigai.batch.domain.PublicStoreData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDataRepository extends MongoRepository<PublicStoreData, String> {
}
