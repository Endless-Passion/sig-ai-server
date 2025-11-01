package com.endlesspassion.sigai.domain.store.controller;

import com.endlesspassion.sigai.domain.store.dto.request.StoreReq;
import com.endlesspassion.sigai.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "가계 정보 입력 API", description = "사장님 가게 정보 입력")
@RestController
@RequestMapping("/api/v1/store")
public class StoreController {
    @Operation(
            summary = "가게 정보 입력",
            description = """
                    사장님이 가게 기본 정보를 입력하여 저장합니다.

                    **필수 입력 정보:**
                    - storeId: 가게 고유 ID
                    - storeName: 가게 이름 (최대 100자)
                    - serviceIndustry: 업종 (KOREAN, CHINESE, JAPANESE, WESTERN, CAFE, CHICKEN, PIZZA, BURGER, BAKERY, OTHER)
                    - dong: 동 주소 (최대 50자, 예: "성수동")
                    - openingDate: 개업일 (YYYY-MM-DD 형식)

                    **선택 입력 정보:**
                    - gu: 구명 (최대 50자, 예: "성동구")
                    - brandCode: 브랜드 코드 (0: 일반, 1: 프랜차이즈)

                    **참고:**
                    - 매출 데이터(월별 매출, 재방문 고객 비율 등)는 별도 API로 등록합니다.
                    - 이 API는 가게의 기본 정보만 등록합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "가게 정보 입력 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": null,
                                              "message": "가게 정보가 성공적으로 저장되었습니다."
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
                                                "field": "storeName",
                                                "message": "가맹점명은 필수입니다"
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
    @PostMapping("/store-info")
    public ApiResponse<?> registerStoreInfo(
            @RequestBody(
                    description = "가게 정보 입력 요청",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StoreReq.class),
                            examples = @ExampleObject(
                                    name = "가게 정보 입력 예시",
                                    value = """
                                            {
                                              "storeId": 1,
                                              "storeName": "맛있는 치킨집",
                                              "serviceIndustry": "CHICKEN",
                                              "gu": "성동구",
                                              "dong": "성수동",
                                              "openingDate": "2024-01-15",
                                              "brandCode": 0
                                            }
                                            """
                            )
                    )
            )
            @Valid
            @org.springframework.web.bind.annotation.RequestBody
            StoreReq request
    ) {
        // TODO: 가게 정보 입력 로직 구현
        return ApiResponse.success();
    }

}
