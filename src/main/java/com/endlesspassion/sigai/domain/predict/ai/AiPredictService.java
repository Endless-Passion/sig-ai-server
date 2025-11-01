package com.endlesspassion.sigai.domain.predict.ai;


import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictReq;
import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictRes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiPredictService {

    private final WebClient webClient;

    public AiPredictService(@Qualifier("seoulApiWebClient") WebClient webClient) {
        // "WebClient" 타입의 Bean이 두 개 있지만,
        // 이름이 "seoulApiWebClient"인 Bean을 주입하도록 명시
        this.webClient = webClient;
    }

    public AiPredictRes get(AiPredictReq req) {

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
