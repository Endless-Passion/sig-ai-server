package com.endlesspassion.sigai.domain.publicdata.service;

import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.repository.PublicProfitDataRepository;
import com.endlesspassion.sigai.domain.publicdata.repository.PublicStoreDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 공공 데이터 조회 서비스
 *
 * MongoDB에서 매출 및 점포 데이터를 조회하고 집계하는 책임
 *
 * 설계 원칙:
 * - 단일 책임: 오직 데이터 조회와 기본 필터링만 담당
 * - 비즈니스 로직은 상위 서비스에서 처리
 * - Repository 패턴을 통한 데이터 접근 추상화
 */

@RequiredArgsConstructor
@Service
public class PublicDataService {

    private final PublicProfitDataRepository publicProfitDataRepository;
    private final PublicStoreDataRepository publicStoreDataRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 특정 분기, 상권, 업종의 매출 데이터 조회
     *
     * @param quarter 분기 코드 (예: "202401")
     * @param trdarCd 상권 코드
     * @param svcIndutyCd 업종 코드
     * @return 매출 데이터 (없으면 null)
     */
    public PublicProfitData getProfitData(String quarter, String trdarCd, String svcIndutyCd) {
        return publicProfitDataRepository
                .findByStdrYyquCdAndTrdarCdAndSvcIndutyCd(quarter, trdarCd, svcIndutyCd)
                .orElse(null); // 이후 예외처리 진행
    }

    /**
     * 특정 분기, 상권, 업종의 점포 데이터 조회
     *
     * @param quarter 분기 코드
     * @param trdarCd 상권 코드
     * @param svcIndutyCd 업종 코드
     * @return 점포 데이터 (없으면 null)
     */
    public PublicStoreData findStoreData(String quarter, String trdarCd, String svcIndutyCd) {
        return publicStoreDataRepository
                .findByStdrYyquCdAndTrdarCdAndSvcIndutyCd(quarter, trdarCd, svcIndutyCd)
                .orElse(null);
    }

    /**
     * 동일 상권, 동일 업종의 모든 매출 데이터 조회 (분기 무관)
     *
     * 용도: 전체 경쟁 매장의 매출 분포를 파악하여 순위 계산
     *
     * @param quarter 분기 코드
     * @param trdarCd 상권 코드
     * @param svcIndutyCd 업종 코드
     * @return 매출 데이터 리스트 (매출 내림차순 정렬)
     */
    public List<PublicProfitData> findAllProfitDataByMarketAndIndustry(
            String quarter, String trdarCd, String svcIndutyCd) {

        Query query = new Query();
        query.addCriteria(Criteria.where("stdrYyquCd").is(quarter));
        query.addCriteria(Criteria.where("trdarCd").is(trdarCd));
        query.addCriteria(Criteria.where("svcIndutyCd").is(svcIndutyCd));
        query.with(Sort.by(Sort.Direction.DESC, "thsmonSelngAmt")); // 매출 내림차순

        return mongoTemplate.find(query, PublicProfitData.class);
    }

    /**
     * 여러 분기의 매출 데이터를 한 번에 조회
     *
     * @param quarters 분기 코드 리스트
     * @param trdarCd 상권 코드
     * @param svcIndutyCd 업종 코드
     * @return 매출 데이터 리스트 (분기 내림차순 정렬)
     */
    public List<PublicProfitData> findProfitDataByQuarters(
            List<String> quarters, String trdarCd, String svcIndutyCd) {

        Query query = new Query();
        query.addCriteria(Criteria.where("stdrYyquCd").in(quarters));
        query.addCriteria(Criteria.where("trdarCd").is(trdarCd));
        query.addCriteria(Criteria.where("svcIndutyCd").is(svcIndutyCd));
        query.with(Sort.by(Sort.Direction.DESC, "stdrYyquCd")); // 최근 분기 우선

        return mongoTemplate.find(query, PublicProfitData.class);
    }

    /**
     * 여러 분기의 점포 데이터를 한 번에 조회
     *
     * @param quarters 분기 코드 리스트
     * @param trdarCd 상권 코드
     * @param svcIndutyCd 업종 코드
     * @return 점포 데이터 리스트 (분기 내림차순 정렬)
     */
    public List<PublicStoreData> findStoreDataByQuarters(
            List<String> quarters, String trdarCd, String svcIndutyCd) {

        Query query = new Query();
        query.addCriteria(Criteria.where("stdrYyquCd").in(quarters));
        query.addCriteria(Criteria.where("trdarCd").is(trdarCd));
        query.addCriteria(Criteria.where("svcIndutyCd").is(svcIndutyCd));
        query.with(Sort.by(Sort.Direction.DESC, "stdrYyquCd")); // 최근 분기 우선

        return mongoTemplate.find(query, PublicStoreData.class);
    }
    /**
     * 특정 분기의 동일 상권, 동일 업종 매출 데이터 개수 조회
     *
     * 용도: 전체 경쟁 매장 수 파악
     */
    public long countProfitDataByMarketAndIndustry(
            String quarter, String trdarCd, String svcIndutyCd) {

        Query query = new Query();
        query.addCriteria(Criteria.where("stdrYyquCd").is(quarter));
        query.addCriteria(Criteria.where("trdarCd").is(trdarCd));
        query.addCriteria(Criteria.where("svcIndutyCd").is(svcIndutyCd));

        return mongoTemplate.count(query, PublicProfitData.class);
    }

}
