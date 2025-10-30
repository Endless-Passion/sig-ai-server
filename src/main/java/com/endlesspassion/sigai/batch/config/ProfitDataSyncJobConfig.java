package com.endlesspassion.sigai.batch.config;
import com.endlesspassion.sigai.batch.domain.publicProfitData; // '매출' 엔티티
import com.endlesspassion.sigai.batch.processor.ProfitDataProcessor; // '매출' 프로세서
import com.endlesspassion.sigai.batch.reader.ProfitDataReader; // '매출' 리더
import com.endlesspassion.sigai.batch.writer.ProfitDataWriter; // '매출' 라이터
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
public class ProfitDataSyncJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ProfitDataReader profitDataReader;
    private final ProfitDataProcessor profitDataProcessor;
    private final ProfitDataWriter profitDataWriter;

    @Bean
    public Job profitDataSyncJob() {
        return new JobBuilder("profitDataSyncJob", jobRepository)
                .start(profitDataSyncStep())
                .build();
    }

    @Bean
    public Step profitDataSyncStep() {
        return new StepBuilder("profitDataSyncStep", jobRepository)
                .<String, List<publicProfitData>>chunk(1, transactionManager)
                .reader(profitDataReader)
                .processor(profitDataProcessor)
                .writer(profitDataWriter)
                .build();
    }
}
