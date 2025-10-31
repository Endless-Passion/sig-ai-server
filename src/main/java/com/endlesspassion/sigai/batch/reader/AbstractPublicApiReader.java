package com.endlesspassion.sigai.batch.reader;

import com.endlesspassion.sigai.batch.service.GetPublicDataService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;

@Slf4j
public abstract class AbstractPublicApiReader implements ItemReader<String> {

    protected final GetPublicDataService publicDataService;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected final String apiKey;
    protected final String serviceName;

    protected int totalCount = 0;
    protected int totalPages = 0;
    protected int currentPage = 0;
    protected static final int PAGE_SIZE = 1000;

    public AbstractPublicApiReader(
            GetPublicDataService publicDataService,
            String apiKey,
            String serviceName
    ) {
        this.publicDataService = publicDataService;
        this.apiKey = apiKey;
        this.serviceName = serviceName;
    }

    @BeforeStep
    public void retrieveTotalCount(StepExecution stepExecution) throws Exception {
        try {
            String jsonResponse = publicDataService.fetchData(apiKey, serviceName, 1, 1);
            JsonNode dataRoot = objectMapper.readTree(jsonResponse).path(this.serviceName);

            if (dataRoot.isMissingNode() || dataRoot.isNull()) {
                throw new RuntimeException("JSON에서 '" + this.serviceName + "' 노드를 찾을 수 없습니다.");
            }

            this.totalCount = dataRoot.path("list_total_count").asInt();
            if (this.totalCount == 0) {
                throw new RuntimeException("list_total_count가 0이거나 존재하지 않습니다.");
            }

            // 테스트 모드: 10개만 가져오기
            String testMode = System.getProperty("batch.test.mode", "false");
            if ("true".equalsIgnoreCase(testMode)) {
                this.totalCount = Math.min(this.totalCount, 10);
                log.info("===== ({}) 테스트 모드: 데이터 10개로 제한 =====", this.serviceName);
            }

            this.totalPages = (int) Math.ceil((double) this.totalCount / PAGE_SIZE);
            log.info("===== ({}) 총 개수: {}, 총 페이지: {} =====", this.serviceName, totalCount, totalPages);

        } catch (Exception e) {
            log.error("({}) 총 개수 조회 실패: {}", this.serviceName, e.getMessage());
            throw e;
        }
    }

    @Override
    public String read() throws Exception {
        if (currentPage >= totalPages) {
            return null;
        }

        int startIndex = currentPage * PAGE_SIZE + 1;
        int endIndex = Math.min((currentPage + 1) * PAGE_SIZE, this.totalCount);

        try {
            String jsonResponse = publicDataService.fetchData(apiKey, serviceName, startIndex, endIndex);

            String enrichedJson = String.format(
                    "{\"startIndex\":%d,\"endIndex\":%d,\"data\":%s}",
                    startIndex, endIndex, jsonResponse
            );

            currentPage++;
            Thread.sleep(100);
            return enrichedJson;

        } catch (Exception e) {
            currentPage++;
            return read();
        }
    }
}
