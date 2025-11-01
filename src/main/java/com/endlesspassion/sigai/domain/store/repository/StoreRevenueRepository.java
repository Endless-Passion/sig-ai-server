package com.endlesspassion.sigai.domain.store.repository;

import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRevenueRepository extends JpaRepository<StoreRevenue, Long> {
    Optional<StoreRevenue> findByStore(Store store);

    /**
     * 특정 가게의 모든 매출 데이터를 년도와 월 기준 내림차순으로 조회
     *
     * @param store 조회할 가게
     * @return 매출 데이터 리스트 (최신순)
     */
    List<StoreRevenue> findByStoreOrderByYearDescMonthDesc(Store store);
}
