package com.endlesspassion.sigai.domain.predict.ai;


import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictReq;
import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictRes;
import com.endlesspassion.sigai.domain.predict.client.dto.PredictReq;
import com.endlesspassion.sigai.domain.predict.client.dto.PredictRes;
import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import com.endlesspassion.sigai.domain.store.entity.Store;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AiPredictService {

    private final WebClient webClient;

    public AiPredictService(@Qualifier("aiApiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public AiPredictRes predict(AiPredictReq req) {


        // webclient로 ai로부터 요청 후 응답 가져오기!
        AiPredictRes res = webClient.post()
                .uri("/predict")
                .bodyValue(req)
                .retrieve() // 응답 수신
                .bodyToMono(AiPredictRes.class)
                .block(); // 동기식으로 결과 기다림

        if (res == null) {
            throw new RuntimeException("AI 서버로부터 응답을 받지 못했습니다.");
        }

        return res;
    }
}