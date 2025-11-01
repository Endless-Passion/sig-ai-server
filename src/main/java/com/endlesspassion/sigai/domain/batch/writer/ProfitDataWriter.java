package com.endlesspassion.sigai.domain.batch.writer;

import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfitDataWriter extends AbstractPublicDataWriter<PublicProfitData> {

    private final ObjectMapper objectMapper;

    public ProfitDataWriter(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        super(mongoTemplate);
        this.objectMapper = objectMapper;
    }

    @Override
    protected String getEntityName() {
        return "ProfitData";
    }

    @Override
    protected Class<PublicProfitData> getEntityClass() {
        return PublicProfitData.class;
    }

    @Override
    protected Query buildUniqueQuery(PublicProfitData entity) {
        return new Query(
            Criteria.where("stdr_yyqu_cd").is(entity.getStdrYyquCd())
                    .and("trdar_cd").is(entity.getTrdarCd())
                    .and("svc_induty_cd").is(entity.getSvcIndutyCd())
        );
    }

    /**
     * [성능 최적화] ObjectMapper를 사용해 객체를 Document로 한 번에 변환
     * - 기존: update.set() 20번 호출 (1,000개 처리 시 20,000번)
     * - 개선: $set Document로 일괄 변환 (1,000개 처리 시 1,000번)
     * - 예상 성능: 1페이지(1,000건) 처리 시간 50초 → 10초 이내
     */
    @Override
    protected Update buildUpdate(PublicProfitData entity) {
        // 객체를 BSON Document로 변환
        Document doc = objectMapper.convertValue(entity, Document.class);

        // Upsert 시 _id 충돌 방지를 위해 제거
        doc.remove("_id");
        doc.remove("id");

        // $set 연산자로 감싸서 Update 객체로 변환
        return Update.fromDocument(new Document("$set", doc));
    }
}
