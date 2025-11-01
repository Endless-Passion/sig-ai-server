package com.endlesspassion.sigai.domain.member.service;

import com.endlesspassion.sigai.domain.member.dto.MemberReq;
import com.endlesspassion.sigai.domain.member.dto.MemberRes;
import com.endlesspassion.sigai.domain.member.entity.Member;
import com.endlesspassion.sigai.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 사장님 정보 조회 (ID)
     */
    public MemberRes get(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사장님을 찾을 수 없습니다. ID: " + memberId));
        return MemberRes.from(member);
    }

    /**
     * 사장님 정보 조회 (전화번호)
     */
    public MemberRes getByPhoneNumber(String phoneNumber) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 전화번호로 가입된 사장님을 찾을 수 없습니다: " + phoneNumber));
        return MemberRes.from(member);
    }

    /**
     * 사장님 등록
     */
    public void create(MemberReq req) {
        // 전화번호 중복 체크
        if (memberRepository.findByPhoneNumber(req.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다: " + req.getPhoneNumber());
        }
        memberRepository.save(req.to());
    }

    /**
     * 사장님 정보 수정
     */
    public void update(MemberReq req) {
        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사장님을 찾을 수 없습니다. ID: " + req.getMemberId()));

        // 전화번호가 변경되는 경우 중복 체크
        if (!member.getPhoneNumber().equals(req.getPhoneNumber())) {
            if (memberRepository.findByPhoneNumber(req.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("이미 등록된 전화번호입니다: " + req.getPhoneNumber());
            }
        }

        // JPA 변경 감지(Dirty Checking)에 의해 트랜잭션 종료 시 자동으로 UPDATE 쿼리 실행
        member.update(req);
    }

    /**
     * 사장님 정보 삭제
     */
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
