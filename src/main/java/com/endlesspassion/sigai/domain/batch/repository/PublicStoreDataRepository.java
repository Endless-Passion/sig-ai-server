package com.endlesspassion.sigai.domain.batch.repository;

import com.endlesspassion.sigai.domain.batch.domain.PublicStoreData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicStoreDataRepository extends MongoRepository<PublicStoreData, String> {
    /**
     * [중복 확인용] 분기, 상권, 업종 코드로 점포 데이터 조회
     * (PublicStoreData 도메인의 고유 키)
     */
    Optional<PublicStoreData> findByStdrYyquCdAndTrdarCdAndSvcIndutyCd(
            String stdrYyquCd, String trdarCd, String svcIndutyCd);

    /**
     * [중복 확인용] 분기, 상권, 업종 코드로 점포 데이터 존재 여부 확인
     */
    boolean existsByStdrYyquCdAndTrdarCdAndSvcIndutyCd(
            String stdrYyquCd, String trdarCd, String svcIndutyCd);
}