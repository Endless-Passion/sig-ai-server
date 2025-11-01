package com.endlesspassion.sigai.domain.store.controller;

import com.endlesspassion.sigai.domain.store.dto.request.StoreRevenueReq;
import com.endlesspassion.sigai.domain.store.dto.respose.StoreRevenueRes;
import com.endlesspassion.sigai.domain.store.service.StoreRevenueService;
import com.endlesspassion.sigai.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "월별 매출 데이터 API", description = "사장님 가게의 월별 매출 및 고객 통계 데이터 관리")
@RequiredArgsConstructor
@RequestMapping("/api/v1/revenue")
@RestController
public class StoreRevenueController {

    private final StoreRevenueService storeRevenueService;

    @Operation(
            summary = "월별 매출 데이터 등록",
            description = """
                    사장님이 특정 월의 매출 및 고객 통계 데이터를 입력합니다.
                   
                    - storeId: 가게 ID
                    - year: 년도 (1950~2100)
                    - month: 월 (1~12)
                    - monthlyRevenue: 월 매출액 (원)
                    - deliverySalesRatio: 배달 매출 비율 (%, 0.0~100.0)
                    - maleCustomer2030Ratio: 남성 2030대 고객 비중 (%, 0.0~100.0)
                    - maleCustomer40PlusRatio: 남성 40대 이상 고객 비중 (%, 0.0~100.0)
                    - returningCustomerRatio: 재방문 고객 비중 (%, 0.0~100.0)

                    **참고:**
                    - 남성 2030대 + 남성 40대 이상 비중의 합은 100% 이하여야 합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "매출 데이터 등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": null,
                                              "message": "매출 데이터가 성공적으로 저장되었습니다."
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
                                                "field": "year",
                                                "message": "년은 1950 이상이어야 합니다."
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
            )
    })
    @PostMapping
    public ApiResponse<?> createRevenue(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "월별 매출 데이터 입력 요청",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StoreRevenueReq.class),
                            examples = @ExampleObject(
                                    name = "매출 데이터 입력 예시",
                                    value = """
                                            {
                                              "storeId": 1,
                                              "year": 2024,
                                              "month": 11,
                                              "monthlyRevenue": 15000000,
                                              "deliverySalesRatio": 70.0,
                                              "maleCustomer2030Ratio": 35.5,
                                              "maleCustomer40PlusRatio": 25.0,
                                              "returningCustomerRatio": 45.0
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody StoreRevenueReq request
    ) {
        storeRevenueService.create(request);
        return ApiResponse.success("매출 데이터가 성공적으로 저장되었습니다.");
    }

    @Operation(
            summary = "가게의 월별 매출 데이터 조회",
            description = """
                    특정 가게의 모든 월별 매출 및 고객 통계 데이터를 조회합니다.
                    최신 월부터 과거 순으로 정렬되어 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "매출 데이터 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": {
                                                "revenues": [
                                                  {
                                                    "year": 2024,
                                                    "month": 11,
                                                    "monthlyRevenue": 15000000,
                                                    "deliverySalesRatio": 70.0,
                                                    "maleCustomer2030Ratio": 35.5,
                                                    "maleCustomer40PlusRatio": 25.0,
                                                    "returningCustomerRatio": 45.0
                                                  },
                                                  {
                                                    "year": 2024,
                                                    "month": 10,
                                                    "monthlyRevenue": 14000000,
                                                    "deliverySalesRatio": 65.0,
                                                    "maleCustomer2030Ratio": 40.0,
                                                    "maleCustomer40PlusRatio": 30.0,
                                                    "returningCustomerRatio": 50.0
                                                  }
                                                ]
                                              },
                                              "message": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "가게를 찾을 수 없거나 매출 데이터가 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/{storeId}")
    public ApiResponse<StoreRevenueRes> getRevenuesByStoreId(
            @PathVariable Long storeId
    ) {
        return ApiResponse.success(storeRevenueService.getAllByStoreId(storeId));
    }
}
