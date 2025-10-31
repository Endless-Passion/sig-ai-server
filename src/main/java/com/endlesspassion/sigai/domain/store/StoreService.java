package com.endlesspassion.sigai.domain.store;

import com.endlesspassion.sigai.domain.store.dto.request.StoreReq;
import com.endlesspassion.sigai.domain.store.dto.respose.StoreRes;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreRes get(StoreReq req) {
        Store store = storeRepository.findById(req.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다. ID: " + req.getStoreId()));
        return StoreRes.from(store);
    }

    public void create(StoreReq req) {
        storeRepository.save(req.to());
    }

    public void update(StoreReq req) {
        Store store = storeRepository.findById(req.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다. ID: " + req.getStoreId()));
        // JPA 변경 감지(Dirty Checking)에 의해 트랜잭션 종료 시 자동으로 UPDATE 쿼리 실행
        // 따라서 save() 호출 불필요
        store.update(req);
    }

    public void delete(StoreReq req) {
        storeRepository.deleteById(req.getStoreId());
    }
}
