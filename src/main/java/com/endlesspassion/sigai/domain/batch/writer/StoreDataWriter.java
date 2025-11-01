package com.endlesspassion.sigai.domain.batch.writer;

import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StoreDataWriter extends AbstractPublicDataWriter<PublicStoreData> {

    public StoreDataWriter(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected String getEntityName() {
        return "StoreData";
    }

    @Override
    protected Class<PublicStoreData> getEntityClass() {
        return PublicStoreData.class;
    }

    @Override
    protected Query buildUniqueQuery(PublicStoreData entity) {
        return new Query(
            Criteria.where("stdr_yyqu_cd").is(entity.getStdrYyquCd())
                    .and("trdar_cd").is(entity.getTrdarCd())
                    .and("svc_induty_cd").is(entity.getSvcIndutyCd())
        );
    }

    @Override
    protected Update buildUpdate(PublicStoreData entity) {
        Update update = new Update();

        update.set("stdr_yyqu_cd", entity.getStdrYyquCd());
        update.set("trdar_se_cd", entity.getTrdarSeCd());
        update.set("trdar_se_cd_nm", entity.getTrdarSeCdNm());
        update.set("trdar_cd", entity.getTrdarCd());
        update.set("trdar_cd_nm", entity.getTrdarCdNm());
        update.set("svc_induty_cd", entity.getSvcIndutyCd());
        update.set("svc_induty_cd_nm", entity.getSvcIndutyCdNm());

        update.set("stor_co", entity.getStorCo());
        update.set("similr_induty_stor_co", entity.getSimilrIndutyStorCo());
        update.set("opbiz_rt", entity.getOpbizRt());
        update.set("opbiz_stor_co", entity.getOpbizStorCo());
        update.set("clsbiz_rt", entity.getClsbizRt());
        update.set("clsbiz_stor_co", entity.getClsbizStorCo());
        update.set("frc_stor_co", entity.getFrcStorCo());

        return update;
    }
}
