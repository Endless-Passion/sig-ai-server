package com.endlesspassion.sigai.batch.processor;

import com.endlesspassion.sigai.batch.domain.PublicStoreData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StoreDataProcessor extends AbstractPublicDataProcessor<PublicStoreData> {

    @Value("${seoul.api.store-service}")
    private String serviceName;

    public StoreDataProcessor(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String getServiceName() {
        return serviceName;
    }

    @Override
    protected PublicStoreData parseRowToEntity(JsonNode rowNode) throws Exception {
        PublicStoreData data = new PublicStoreData();

        data.setStdrYyquCd(getTextValue(rowNode, "STDR_YYQU_CD"));
        data.setTrdarSeCd(getTextValue(rowNode, "TRDAR_SE_CD"));
        data.setTrdarSeCdNm(getTextValue(rowNode, "TRDAR_SE_CD_NM"));
        data.setTrdarCd(getTextValue(rowNode, "TRDAR_CD"));
        data.setTrdarCdNm(getTextValue(rowNode, "TRDAR_CD_NM"));
        data.setSvcIndutyCd(getTextValue(rowNode, "SVC_INDUTY_CD"));
        data.setSvcIndutyCdNm(getTextValue(rowNode, "SVC_INDUTY_CD_NM"));

        data.setStorCo(getDoubleValue(rowNode, "STOR_CO"));
        data.setSimilrIndutyStorCo(getDoubleValue(rowNode, "SIMILR_INDUTY_STOR_CO"));
        data.setOpbizRt(getDoubleValue(rowNode, "OPBIZ_RT"));
        data.setOpbizStorCo(getDoubleValue(rowNode, "OPBIZ_STOR_CO"));
        data.setClsbizRt(getDoubleValue(rowNode, "CLSBIZ_RT"));
        data.setClsbizStorCo(getDoubleValue(rowNode, "CLSBIZ_STOR_CO"));
        data.setFrcStorCo(getDoubleValue(rowNode, "FRC_STOR_CO"));

        return data;
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText() : null;
    }

    private Double getDoubleValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        try {
            return fieldNode.asDouble();
        } catch (Exception e) {
            log.warn("Failed to parse double value for field '{}': {}", fieldName, fieldNode.asText());
            return null;
        }
    }
}
