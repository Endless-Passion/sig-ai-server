package com.endlesspassion.sigai.batch.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPublicDataProcessor<T> implements ItemProcessor<String, List<T>> {

    private final ObjectMapper objectMapper;

    @Override
    public List<T> process(String enrichedJsonResponse) throws Exception {
        if (enrichedJsonResponse == null || enrichedJsonResponse.trim().isEmpty()) {
            log.warn("Empty or null JSON response received, skipping processing");
            return new ArrayList<>();
        }

        try {
            JsonNode wrapperNode = objectMapper.readTree(enrichedJsonResponse);

            JsonNode rootNode = wrapperNode.get("data");

            if (rootNode == null || rootNode.isNull()) {
                log.warn("No 'data' node found in enriched JSON response");
                return new ArrayList<>();
            }

            String serviceName = getServiceName();
            JsonNode serviceNode = rootNode.get(serviceName);

            if (serviceNode == null) {
                log.warn("Service node '{}' not found in JSON response", serviceName);
                return new ArrayList<>();
            }

            JsonNode resultNode = serviceNode.get("RESULT");
            if (resultNode != null) {
                String resultCode = resultNode.get("CODE").asText();
                if (!"INFO-000".equals(resultCode)) {
                    String resultMessage = resultNode.get("MESSAGE").asText();
                    log.error("API error response: {} - {}", resultCode, resultMessage);
                    return new ArrayList<>();
                }
            }

            JsonNode rowsNode = serviceNode.get("row");
            if (rowsNode == null || !rowsNode.isArray()) {
                log.warn("No 'row' array found in service node '{}'", serviceName);
                return new ArrayList<>();
            }

            List<T> entities = new ArrayList<>();
            for (JsonNode rowNode : rowsNode) {
                try {
                    T entity = parseRowToEntity(rowNode);
                    if (entity != null) {
                        entities.add(entity);
                    }
                } catch (Exception e) {
                    log.error("Failed to parse row to entity: {}", rowNode.toString(), e);
                }
            }

            log.info("Successfully processed {} entities from JSON response", entities.size());
            return entities;

        } catch (Exception e) {
            log.error("Failed to process JSON response", e);
            throw e;
        }
    }

    protected abstract String getServiceName();

    protected abstract T parseRowToEntity(JsonNode rowNode) throws Exception;
}
