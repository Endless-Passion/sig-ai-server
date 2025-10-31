package com.endlesspassion.sigai.batch.processor;

import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfitDataProcessor extends AbstractPublicDataProcessor<PublicProfitData> {

    @Value("${seoul.api.sales-service}")
    private String serviceName;

    public ProfitDataProcessor(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String getServiceName() {
        return serviceName;
    }

    @Override
    protected PublicProfitData parseRowToEntity(JsonNode rowNode) throws Exception {
        PublicProfitData data = new PublicProfitData();

        data.setStdrYyquCd(getTextValue(rowNode, "STDR_YYQU_CD"));
        data.setTrdarSeCd(getTextValue(rowNode, "TRDAR_SE_CD"));
        data.setTrdarSeCdNm(getTextValue(rowNode, "TRDAR_SE_CD_NM"));
        data.setTrdarCd(getTextValue(rowNode, "TRDAR_CD"));
        data.setTrdarCdNm(getTextValue(rowNode, "TRDAR_CD_NM"));
        data.setSvcIndutyCd(getTextValue(rowNode, "SVC_INDUTY_CD"));
        data.setSvcIndutyCdNm(getTextValue(rowNode, "SVC_INDUTY_CD_NM"));

        data.setThsmonSelngAmt(getDoubleValue(rowNode, "THSMON_SELNG_AMT"));
        data.setMlSelngAmt(getDoubleValue(rowNode, "ML_SELNG_AMT"));
        data.setFmlSelngAmt(getDoubleValue(rowNode, "FML_SELNG_AMT"));

        data.setThsmonSelngCo(getDoubleValue(rowNode, "THSMON_SELNG_CO"));
        data.setMdwkSelngCo(getDoubleValue(rowNode, "MDWK_SELNG_CO"));
        data.setMlSelngCo(getDoubleValue(rowNode, "ML_SELNG_CO"));
        data.setFmlSelngCo(getDoubleValue(rowNode, "FML_SELNG_CO"));

        data.setAgrde10SelngCo(getDoubleValue(rowNode, "AGRDE_10_SELNG_CO"));
        data.setAgrde20SelngCo(getDoubleValue(rowNode, "AGRDE_20_SELNG_CO"));
        data.setAgrde30SelngCo(getDoubleValue(rowNode, "AGRDE_30_SELNG_CO"));
        data.setAgrde40SelngCo(getDoubleValue(rowNode, "AGRDE_40_SELNG_CO"));
        data.setAgrde50SelngCo(getDoubleValue(rowNode, "AGRDE_50_SELNG_CO"));
        data.setAgrde60AboveSelngCo(getDoubleValue(rowNode, "AGRDE_60_ABOVE_SELNG_CO"));

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
