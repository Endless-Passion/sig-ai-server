package com.endlesspassion.sigai.domain.member.controller;

import com.endlesspassion.sigai.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "가계 정보 입력 API", description = "사장님 가게 정보 입력")
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    @Operation(
            summary = "가게 정보 입력",
            description = """
                    가게 정보를 입력받아 저장합니다.

                    **필수 정보:**
                    - 가맹점명
                    - 가맹점 주소
                    - 업종
                    - 개업일
                    - 사업자 등록 번호
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
    @SuppressWarnings("unused")
    public ResponseEntity<ApiResponse<?>> registerStoreInfo(
            @RequestBody Object request // TODO: DTO 생성 후 가게 정보 입력
    ) {
        // TODO: 가게 정보 입력 로직 구현
        return ResponseEntity.ok(ApiResponse.success());
    }

}
