package com.endlesspassion.sigai.batch.repository;

import com.endlesspassion.sigai.batch.domain.publicProfitData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommercialDataRepository extends MongoRepository<publicProfitData, String> {

    /**
     * startIndex와 endIndex로 데이터 조회 (중복 확인용)
     */
    Optional<publicProfitData> findByStartIndexAndEndIndex(Integer startIndex, Integer endIndex);

    /**
     * startIndex와 endIndex로 존재 여부 확인
     */
    boolean existsByStartIndexAndEndIndex(Integer startIndex, Integer endIndex);
}
