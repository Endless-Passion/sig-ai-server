package com.endlesspassion.sigai.domain.member.dto;

import com.endlesspassion.sigai.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class MemberReq {

    private Long memberId;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phoneNumber;

    public Member to() {
        return Member.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
