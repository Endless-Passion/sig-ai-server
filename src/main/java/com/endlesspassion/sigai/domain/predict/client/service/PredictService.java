package com.endlesspassion.sigai.domain.predict.client.service;


import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class PredictService {

    private final PublicDataService publicDataService;

    private final StoreRepository storeRepository;



    // TODO: publicDataService로부터


    /**
     *
     * 결론적으로 AI한테 넘겨야 하는 값들! (publicDataService에서 webClientReq로 변환 후 넘기기)
     *{
     *     "TA_YM": 202410,  // 기준 년월
     *     "HPSN_MCT_ZCD_NM": "커피전문점", // 업종명
     *     "MCT_OPE_MS_CN": "12개월",  // 운영 개월 수
     *     "M12_SME_RY_SAA_PCE_RT": 80.0, // 동일 업종 매출 순위
     *     "M1SME_RY_SAA_RAT": 0.1,  // 동일 업종 매출 비율
     *     "M1_SME_RY_CNT_RAT": 0.1,  // 동일 업종 매출 건수 비율
     *     "M12_SME_RY_ME_MCT_RAT": 0.5,  // 폐업 가맹점 비율
     *     "MCT_BRD_NUM": null,  // 브랜드 번호
     *     "DLV_SAA_RAT": 0.0,  // 배달 매출 비율
     *     "MCT_UE_CLN_REU_RAT": 0.1,  // 재방문 고객 비중
     *     "RC_M1_SHC_FLP_UE_CLN_RAT": 0.05, // fix
     *     "M12_MAL_1020_RAT": 0.1, // 2030
     *     "M12_MAL_30_RAT": 0.0, // fix
     *     "M12_MAL_40_RAT": 0.0, // 40 over
     *     "M12_MAL_50_RAT": 0.0,  // fix
     *     "M12_MAL_60_RAT": 0.0 // fix
     * }
     */
}
