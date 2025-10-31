package com.endlesspassion.sigai.domain.batch.config;

import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.batch.processor.ProfitDataProcessor;
import com.endlesspassion.sigai.domain.batch.processor.StoreDataProcessor;
import com.endlesspassion.sigai.domain.batch.reader.ProfitDataReader;
import com.endlesspassion.sigai.domain.batch.reader.StoreDataReader;
import com.endlesspassion.sigai.domain.batch.writer.ProfitDataWriter;
import com.endlesspassion.sigai.domain.batch.writer.StoreDataWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PublicDataSyncJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ProfitDataReader profitDataReader;
    private final StoreDataReader storeDataReader;

    private final ProfitDataProcessor profitDataProcessor;
    private final StoreDataProcessor storeDataProcessor;

    private final ProfitDataWriter profitDataWriter;
    private final StoreDataWriter storeDataWriter;

    // ========== 매출 데이터 동기화 작업 ==========

    @Bean
    public Job profitDataSyncJob() {
        return new JobBuilder("profitDataSyncJob", jobRepository)
                .start(profitDataSyncStep())
                .build();
    }

    @Bean
    public Step profitDataSyncStep() {
        return new StepBuilder("profitDataSyncStep", jobRepository)
                .<String, List<PublicProfitData>>chunk(1, transactionManager)
                .reader(profitDataReader)
                .processor(profitDataProcessor)
                .writer(profitDataWriter)
                .build();
    }

    // ========== 점포 데이터 동기화 작업 ==========

    @Bean
    public Job storeDataSyncJob() {
        return new JobBuilder("storeDataSyncJob", jobRepository)
                .start(storeDataSyncStep())
                .build();
    }

    @Bean
    public Step storeDataSyncStep() {
        return new StepBuilder("storeDataSyncStep", jobRepository)
                .<String, List<PublicStoreData>>chunk(1, transactionManager)
                .reader(storeDataReader)
                .processor(storeDataProcessor)
                .writer(storeDataWriter)
                .build();
    }
}
