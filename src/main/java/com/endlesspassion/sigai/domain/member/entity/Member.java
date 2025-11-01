package com.endlesspassion.sigai.domain.member.entity;

import com.endlesspassion.sigai.domain.member.dto.MemberReq;
import com.endlesspassion.sigai.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String phoneNumber;

    public void update(MemberReq req) {
        this.name = req.getName();
        this.phoneNumber = req.getPhoneNumber();
    }
}
