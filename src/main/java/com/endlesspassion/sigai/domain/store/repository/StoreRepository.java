package com.endlesspassion.sigai.domain.store.repository;

import com.endlesspassion.sigai.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * Member의 phoneNumber로 해당 사장님의 모든 가게를 조회
     * JPA의 1:N 관계를 활용한 메서드
     */
    List<Store> findByMember_PhoneNumber(String phoneNumber);
}
