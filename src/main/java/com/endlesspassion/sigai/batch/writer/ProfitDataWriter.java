package com.endlesspassion.sigai.batch.writer;

import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfitDataWriter extends AbstractPublicDataWriter<PublicProfitData> {

    public ProfitDataWriter(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
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

    @Override
    protected Update buildUpdate(PublicProfitData entity) {
        Update update = new Update();

        update.set("stdr_yyqu_cd", entity.getStdrYyquCd());
        update.set("trdar_se_cd", entity.getTrdarSeCd());
        update.set("trdar_se_cd_nm", entity.getTrdarSeCdNm());
        update.set("trdar_cd", entity.getTrdarCd());
        update.set("trdar_cd_nm", entity.getTrdarCdNm());
        update.set("svc_induty_cd", entity.getSvcIndutyCd());
        update.set("svc_induty_cd_nm", entity.getSvcIndutyCdNm());

        update.set("thsmon_selng_amt", entity.getThsmonSelngAmt());
        update.set("ml_selng_amt", entity.getMlSelngAmt());
        update.set("fml_selng_amt", entity.getFmlSelngAmt());

        update.set("thsmon_selng_co", entity.getThsmonSelngCo());
        update.set("mdwk_selng_co", entity.getMdwkSelngCo());
        update.set("ml_selng_co", entity.getMlSelngCo());
        update.set("fml_selng_co", entity.getFmlSelngCo());

        update.set("agrde_10_selng_co", entity.getAgrde10SelngCo());
        update.set("agrde_20_selng_co", entity.getAgrde20SelngCo());
        update.set("agrde_30_selng_co", entity.getAgrde30SelngCo());
        update.set("agrde_40_selng_co", entity.getAgrde40SelngCo());
        update.set("agrde_50_selng_co", entity.getAgrde50SelngCo());
        update.set("agrde_60_above_selng_co", entity.getAgrde60AboveSelngCo());

        return update;
    }
}
