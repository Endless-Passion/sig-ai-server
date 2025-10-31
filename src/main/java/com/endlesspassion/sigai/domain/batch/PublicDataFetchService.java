package com.endlesspassion.sigai.domain.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicDataFetchService {
    private final WebClient webClient;
    /**
     * @param apiKey       호출할 API의 고유 키 (예: "PROFIT_API_KEY")
     * @param serviceName  호출할 서비스명 (예: "VwsmTrdhlSelngQq")
     * @param startIndex   시작 인덱스
     * @param endIndex     종료 인덱스
     * @return API 응답 (JSON String)
     */
    public String fetchData(String apiKey, String serviceName, int startIndex, int endIndex) {

        String uri = String.format("/%s/json/%s/%d/%d/",
                apiKey, serviceName, startIndex, endIndex);

        try {
            String response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(java.time.Duration.ofSeconds(10))
                    .retry(3)
                    .block();

            log.info("API 응답 수신 완료: {}-{}, 크기: {} bytes",
                    startIndex, endIndex, response != null ? response.length() : 0);

            if (response == null) {
                throw new RuntimeException("API 응답이 null입니다.");
            }
            return response;

        } catch (Exception e) {
            log.error("API 호출 실패: service={}, {}-{}, 오류: {}",
                    serviceName, startIndex, endIndex, e.getMessage());
            throw new RuntimeException("서울시 API 호출 실패: " + serviceName, e);
        }
    }
}

