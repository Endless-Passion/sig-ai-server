package com.endlesspassion.sigai.domain.predict.client.controller;

import com.endlesspassion.sigai.domain.predict.client.dto.PredictReq;
import com.endlesspassion.sigai.domain.predict.client.dto.PredictRes; // Import 추가
import com.endlesspassion.sigai.domain.predict.client.service.PredictService;
import com.endlesspassion.sigai.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예측 API", description = "소상공인 폐업 예측 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PredictController {

    private final PredictService predictService;

    @Operation(
            summary = "폐업 예측",
            description = """
                    가맹점의 상세 정보를 받아 해당 가맹점의 폐업 위험도를 예측합니다.

                    **필수 정보:**
                    - 가맹점 ID (storeId)
                    - 현재 분기 (quarter)
                    - 운영 개월 수 (monthsOfOperation)
                    
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "예측 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": {
                                                  "prediction_tier": "주의",
                                                  "xgb_probability": 0.1504,
                                                  "rf_probability": 0.4522,
                                                  "threshold_caution": 0.0997,
                                                  "threshold_danger": 0.5520,
                                                  "changedRevenue": {
                                                      "rankChange": null,
                                                      "percentileChange": null,
                                                      "countChange": -50
                                                  },
                                                  "changeClose": {
                                                      "closedRate": 5.5,
                                                      "rateChange": 1
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
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    // 예시: PredictRequest DTO에서 storeId가 누락된 경우
                                    value = """
                                            {
                                              "status": "fail",
                                              "data": {
                                                "field": "storeId",
                                                "message": "가맹점 ID는 필수입니다."
                                              },
                                              "message": "잘못된 요청입니다"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "가맹점을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "fail",
                                              "data": null,
                                              "message": "해당 가맹점을 찾을 수 없습니다."
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
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "error",
                                              "data": null,
                                              "message": "서버 내부 오류가 발생했습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/predict")
    @SuppressWarnings("unused")
    public ApiResponse<?> predict(
            @Valid @RequestBody PredictReq request // @Valid 추가, DTO 이름 수정 없음
    ) {
        // [수정] 서비스 로직 호출 및 응답 반환
        PredictRes response = predictService.predict(request);
        return ApiResponse.success(response);
    }
}