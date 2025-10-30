package com.endlesspassion.sigai.batch.reader;

import com.endlesspassion.sigai.batch.service.GetPublicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StoreDataReader extends AbstractPublicApiReader{

    public StoreDataReader(
            GetPublicDataService publicDataService,
            @Value("${seoul.api.store-key}") String apiKey,
            @Value("${seoul.api.store-service}") String serviceName
    ) {
        super(publicDataService, apiKey, serviceName);
        log.info("StoreDataReader 생성 완료: serviceName={}", serviceName);
    }
}
