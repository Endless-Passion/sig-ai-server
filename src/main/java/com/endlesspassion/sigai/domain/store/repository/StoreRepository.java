package com.endlesspassion.sigai.domain.store.repository;

import com.endlesspassion.sigai.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Long, Store> {

}
