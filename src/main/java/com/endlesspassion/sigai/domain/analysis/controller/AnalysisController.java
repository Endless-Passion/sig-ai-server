package com.endlesspassion.sigai.domain.analysis.controller;

import com.endlesspassion.sigai.domain.analysis.dto.request.MarketAnalysisReq;
import com.endlesspassion.sigai.domain.analysis.service.MarketAnalysisService;
import com.endlesspassion.sigai.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "분석 API", description = "소상공인 상권 분석 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AnalysisController {

    private final MarketAnalysisService marketAnalysisService;

    @Operation(
            summary = "가게 상권 분석",
            description = """
                    특정 가게의 상권을 분석합니다.

                    **분석 기준:**
                    - 가게 ID를 통해 업종 및 상권 정보 조회
                    - 지정된 분기의 매출 데이터 분석
                    - 과거 분기 트렌드 비교 (기본 8분기)

                    **요청 본문 (MarketAnalysisReq):**
                    - `storeId`: 분석할 가게 ID (필수)
                    - `quarter`: 분석 기준 분기, YYYYQQ 형식 (예: 202401 = 2024년 1분기)
                    - `count`: 조회할 과거 분기 수 (1-20, 기본값: 8)
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "상권 분석 성공",
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
                                              "message": "상권 분석이 완료되었습니다"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "fail",
                                              "data": {
                                                "field": "quarter",
                                                "message": "분기는 YYYYQQ 형식이어야 합니다"
                                              },
                                              "message": "잘못된 요청입니다"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "가게를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
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
    @PostMapping("/analysis")
    public ResponseEntity<ApiResponse<?>> analysis(
            @RequestBody MarketAnalysisReq req
    ) {
        marketAnalysisService.analyze(req);
        return ResponseEntity.ok(ApiResponse.success("상권 분석이 완료되었습니다"));
    }
}
