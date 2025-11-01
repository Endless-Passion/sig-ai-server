package com.endlesspassion.sigai.domain.store.controller;

import com.endlesspassion.sigai.domain.store.dto.request.StoreReq;
import com.endlesspassion.sigai.domain.store.dto.respose.StoreRes;
import com.endlesspassion.sigai.domain.store.service.StoreService;
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

@Tag(name = "가게 정보 API", description = "사장님 가게 정보 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;

    @Operation(
            summary = "가게 정보 등록",
            description = """
                    가게 기본 정보를 등록합니다.

                    - storeName: 가게 이름
                    - serviceIndustry: 업종 (KOREAN, CHINESE, JAPANESE, WESTERN, CAFE, CHICKEN, PIZZA, BURGER, BAKERY, OTHER)
                    - dong: 동 주소 (예: "성수동")
                    - openingDate: 개업일 (YYYY-MM-DD)
                    - gu: 구 주소 (선택, 예: "성동구")
                    - brandCode: 브랜드 코드 (선택, 0: 일반, 1: 프랜차이즈)

                    **참고:** 매출 데이터는 별도 API(/api/v1/revenue)로 등록합니다.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
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
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ApiResponse<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "storeId": 1,
                                              "storeName": "맛닭꼬끼오",
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
            @Valid @RequestBody StoreReq req
    ) {
        storeService.create(req);
        return ApiResponse.success("가게 정보가 성공적으로 저장되었습니다.");
    }

    @Operation(
            summary = "가게 정보 조회",
            description = "특정 가게의 기본 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": {
                                                "id": 1,
                                                "storeName": "맛있는 치킨집",
                                                "serviceIndustry": "CHICKEN",
                                                "gu": "성동구",
                                                "dong": "성수동",
                                                "openingDate": "2024-01-15",
                                                "isFranchise": false
                                              },
                                              "message": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "가게를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{storeId}")
    public ApiResponse<StoreRes> get(@PathVariable Long storeId) {
        return ApiResponse.success(storeService.get(storeId));
    }

    @Operation(
            summary = "가게 정보 수정",
            description = "가게 기본 정보를 수정합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": null,
                                              "message": "가게 정보가 성공적으로 수정되었습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "가게를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{storeId}")
    public ApiResponse<?> update(
            @PathVariable Long storeId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "storeId": 1,
                                              "storeName": "더맛있는 치킨집",
                                              "serviceIndustry": "CHICKEN",
                                              "gu": "성동구",
                                              "dong": "성수동",
                                              "openingDate": "2024-01-15",
                                              "brandCode": 1
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody StoreReq req
    ) {
        storeService.update(req);
        return ApiResponse.success("가게 정보가 성공적으로 수정되었습니다.");
    }

    @Operation(
            summary = "가게 정보 삭제",
            description = "가게 정보를 삭제합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "success",
                                              "data": null,
                                              "message": "가게 정보가 성공적으로 삭제되었습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{storeId}")
    public ApiResponse<?> delete(@PathVariable Long storeId) {
        StoreReq req = StoreReq.builder().storeId(storeId).build();
        storeService.delete(req);
        return ApiResponse.success("가게 정보가 성공적으로 삭제되었습니다.");
    }
}