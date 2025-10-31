package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RevenueComparisonService {

    private final PublicDataService publicDataService;


    // 공공 데이터로부터 다음 정보 가져오기
    // 매출 비교 그래프
    //-동일 상권, 동일 업종 내 내 가게가 상위% (매출 상대지표)
    //-순위 변화
    //경쟁강도 = 동일업종 점포수 / 상권 면적(면적 미존재 시 점포수 지표만)
    public void alalysis() {
    }
}
