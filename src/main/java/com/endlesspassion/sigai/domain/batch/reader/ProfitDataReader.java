package com.endlesspassion.sigai.domain.batch.reader;

import com.endlesspassion.sigai.domain.batch.service.GetPublicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfitDataReader extends AbstractPublicApiReader {
    public ProfitDataReader(
            GetPublicDataService publicDataService,
            @Value("${seoul.api.sales-key}") String apiKey,
            @Value("${seoul.api.sales-service}") String serviceName
    ) {
        super(publicDataService, apiKey, serviceName);
        log.info("ProfitDataReader 생성 완료: serviceName={}", serviceName);
    }

}
