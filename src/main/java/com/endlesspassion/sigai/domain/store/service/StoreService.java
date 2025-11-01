package com.endlesspassion.sigai.domain.store.service;

import com.endlesspassion.sigai.domain.member.entity.Member;
import com.endlesspassion.sigai.domain.member.repository.MemberRepository;
import com.endlesspassion.sigai.domain.store.dto.request.StoreReq;
import com.endlesspassion.sigai.domain.store.dto.respose.StoreRes;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreRes get(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다. ID: " + storeId));
        return StoreRes.from(store);
    }

    /**
     * Member의 phoneNumber로 해당 사장님의 모든 가게를 조회
     * JPA 1:N 관계 활용
     */
    public List<StoreRes> getStoresByPhoneNumber(String phoneNumber) {
        List<Store> stores = storeRepository.findByMember_PhoneNumber(phoneNumber);

        if (stores.isEmpty()) {
            throw new IllegalArgumentException("해당 전화번호로 등록된 가게가 없습니다: " + phoneNumber);
        }

        return stores.stream()
                .map(StoreRes::from)
                .collect(Collectors.toList());
    }

    public void create(String phoneNumber, StoreReq req) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 Member을 찾을 수 없음. phoneNumber: " + phoneNumber));
        storeRepository.save(req.to(member));
    }

    public void update(String phoneNumber, Long storeId, StoreReq req) {

        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 Member을 찾을 수 없음. phoneNumber: " + phoneNumber));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Store을 찾을 수 없음. storeId: " + storeId));

        if (!store.getMember().getId().equals(member.getId())) {
            // 일치하지 않으면 예외 발생
            throw new SecurityException("가게 정보를 수정할 권한이 없습니다."); // (혹은 403 Forbidden Error)
        }

        store.update(req);
    }

    public void delete(String phoneNumber, Long storeId) {

        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 Member을 찾을 수 없음. phoneNumber: " + phoneNumber));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Store을 찾을 수 없음. storeId: " + storeId));

        if (!store.getMember().getId().equals(member.getId())) {
            throw new SecurityException("가게 정보를 삭제할 권한이 없습니다.");
        }
        storeRepository.deleteById(storeId);
    }
}
