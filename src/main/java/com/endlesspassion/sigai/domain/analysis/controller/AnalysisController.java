package com.endlesspassion.sigai.domain.analysis.controller;

import com.endlesspassion.sigai.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "분석 API", description = "소상공인 상권 분석 관련 API")
@RestController
@RequestMapping("/api/v1")
public class AnalysisController {

    @Operation(
            summary = "상권 분석",
            description = """
                    가맹점의 상세 정보를 받아 해당 가맹점의 상권을 분석합니다.

                    **필수 정보:**
                    - 가맹점 기본 정보 (주소, 업종, 개설일 등)
                    - 고객 연령대별/성별 비중
                    - 고객 행태 (재방문율, 신규고객율 등)
                    - 주변 상권 정보 (동일업종 폐업율 등)
                    - 매출 관련 구간 정보
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "분석 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": {
                                                "market_score": 75.5,
                                                "competition_level": "medium",
                                                "customer_analysis": {
                                                  "main_age_group": "30-40",
                                                  "revisit_rate": "65%"
                                                }
                                              },
                                              "message": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 필드 누락 또는 형식 오류)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "fail",
                                              "data": {
                                                "field": "MCT_BSE_AR",
                                                "message": "가맹점 주소는 필수입니다"
                                              },
                                              "message": "잘못된 요청입니다"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/analysis")
    @SuppressWarnings("unused")
    public ResponseEntity<ApiResponse<?>> analysis(
            @RequestBody Object request // TODO: AnalysisRequest DTO로 교체 예정
    ) {
        // TODO: 분석 서비스 로직 구현
        return ResponseEntity.ok(ApiResponse.success());
    }
}
