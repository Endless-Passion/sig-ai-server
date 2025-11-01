package com.endlesspassion.sigai.domain.member;

import com.endlesspassion.sigai.domain.member.dto.MemberReq;
import com.endlesspassion.sigai.domain.member.dto.MemberRes;
import com.endlesspassion.sigai.domain.member.service.MemberService;
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

@Tag(name = "사장님 관리 API", description = "사장님 정보 관리")
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "사장님 등록",
            description = """
                    사장님 정보를 등록합니다.

                    - name: 사장님 이름
                    - phoneNumber: 전화번호 (010-1234-5678 형식)

                    **참고:** 전화번호는 중복될 수 없습니다.
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
                                              "message": "사장님 정보가 성공적으로 등록되었습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (중복된 전화번호 또는 형식 오류)",
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
                                              "name": "김사장",
                                              "phoneNumber": "010-1234-5678"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody MemberReq req
    ) {
        memberService.create(req);
        return ApiResponse.success("사장님 정보가 성공적으로 등록되었습니다.");
    }

    @Operation(
            summary = "사장님 정보 조회 (ID)",
            description = "사장님 ID로 정보를 조회합니다."
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
                                                "name": "김사장",
                                                "phoneNumber": "010-1234-5678"
                                              },
                                              "message": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사장님을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{memberId}")
    public ApiResponse<MemberRes> get(@PathVariable Long memberId) {
        return ApiResponse.success(memberService.get(memberId));
    }

    @Operation(
            summary = "사장님 정보 조회 (전화번호)",
            description = "전화번호로 사장님 정보를 조회합니다."
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
                                                "name": "김사장",
                                                "phoneNumber": "010-1234-5678"
                                              },
                                              "message": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 전화번호로 등록된 사장님을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/phone/{phoneNumber}")
    public ApiResponse<MemberRes> getByPhoneNumber(@PathVariable String phoneNumber) {
        return ApiResponse.success(memberService.getByPhoneNumber(phoneNumber));
    }

    @Operation(
            summary = "사장님 정보 수정",
            description = "사장님 정보를 수정합니다."
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
                                              "message": "사장님 정보가 성공적으로 수정되었습니다."
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사장님을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{memberId}")
    public ApiResponse<?> update(
            @PathVariable Long memberId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "memberId": 1,
                                              "name": "이사장",
                                              "phoneNumber": "010-9876-5432"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody MemberReq req
    ) {
        memberService.update(req);
        return ApiResponse.success("사장님 정보가 성공적으로 수정되었습니다.");
    }

    @Operation(
            summary = "사장님 정보 삭제",
            description = "사장님 정보를 삭제합니다."
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
                                              "message": "사장님 정보가 성공적으로 삭제되었습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{memberId}")
    public ApiResponse<?> delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
        return ApiResponse.success("사장님 정보가 성공적으로 삭제되었습니다.");
    }
}