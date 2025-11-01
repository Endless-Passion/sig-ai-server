package com.endlesspassion.sigai.domain.analysis.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 인구 비교 분석 응답 DTO
 *
 * 현재는 구현하지 않음 (향후 확장 예정)
 * 프론트엔드에서 임시 데이터로 처리
 *
 * 향후 구현 예정 기능:
 * - 유동인구 추세
 * - 거주인구 추세
 * - 직장인구 추세
 * - 분기별 인구 변화 데이터
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PopulationComparison {

    // 현재는 빈 껍데기
    // 프론트에서 이 객체가 비어있으면 임시 데이터를 사용하도록 처리

    /**
     * 데이터 제공 여부 플래그
     * false로 고정하여 프론트에 구현되지 않았음을 알림
     */
    private final boolean available = false;

    /**
     * 기본 생성자 - 빈 객체 생성
     */
    public static PopulationComparison empty() {
        return PopulationComparison.builder().build();
    }
}