package com.endlesspassion.sigai.batch.repository;

import com.endlesspassion.sigai.batch.domain.PublicProfitData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicProfitDataRepository extends MongoRepository<PublicProfitData, String> {

    /**
     * [수정] 분기, 상권, 업종 코드로 데이터 조회 (중복 확인용)
     * 이 세 가지 필드가 이 데이터의 고유 키(Unique Key)입니다.
     */
    Optional<PublicProfitData> findByStdrYyquCdAndTrdarCdAndSvcIndutyCd(
            String stdrYyquCd, String trdarCd, String svcIndutyCd);

    /**
     * [수정] 분기, 상권, 업종 코드로 존재 여부 확인
     */
    boolean existsByStdrYyquCdAndTrdarCdAndSvcIndutyCd(
            String stdrYyquCd, String trdarCd, String svcIndutyCd);
}