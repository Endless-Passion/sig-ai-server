package com.endlesspassion.sigai.batch.config;
import com.endlesspassion.sigai.batch.domain.PublicStoreData; // '점포' 엔티티
import com.endlesspassion.sigai.batch.processor.StoreDataProcessor; // '점포' 프로세서
import com.endlesspassion.sigai.batch.reader.StoreDataReader; // '점포' 리더
import com.endlesspassion.sigai.batch.writer.StoreDataWriter; // '점포' 라이터
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
public class StoreDataSyncJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StoreDataReader storeDataReader;
    private final StoreDataProcessor storeDataProcessor;
    private final StoreDataWriter storeDataWriter;

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
