package com.endlesspassion.sigai.domain.member.dto;

import com.endlesspassion.sigai.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRes {

    private Long id;
    private String name;
    private String phoneNumber;

    public static MemberRes from(Member member) {
        return MemberRes.builder()
                .id(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
