package com.endlesspassion.sigai.batch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/batch")
public class BatchTestController {

    private final JobLauncher jobLauncher;
    private final Job profitDataSyncJob;
    private final Job storeDataSyncJob;

    public BatchTestController(
            JobLauncher jobLauncher,
            @Qualifier("profitDataSyncJob") Job profitDataSyncJob,
            @Qualifier("storeDataSyncJob") Job storeDataSyncJob
    ) {
        this.jobLauncher = jobLauncher;
        this.profitDataSyncJob = profitDataSyncJob;
        this.storeDataSyncJob = storeDataSyncJob;
    }

    @PostMapping("/seoul-data-sync")
    public String runSeoulDataSyncJob() {
        try {
            log.info("===== 서울 공공데이터 배치 수동 실행 요청 =====");

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestTime", LocalDateTime.now().toString())
                .toJobParameters();

            log.info(">>> 1. ProfitData 동기화 시작");
            jobLauncher.run(profitDataSyncJob, jobParameters);
            log.info(">>> 1. ProfitData 동기화 완료");

            log.info(">>> 2. StoreData 동기화 시작");
            jobLauncher.run(storeDataSyncJob, jobParameters);
            log.info(">>> 2. StoreData 동기화 완료");

            log.info("===== 서울 공공데이터 배치 실행 완료 =====");
            return "배치 실행 성공: " + LocalDateTime.now() + "\n- ProfitData 동기화 완료\n- StoreData 동기화 완료";

        } catch (Exception e) {
            log.error("배치 실행 실패", e);
            return "배치 실행 실패: " + e.getMessage();
        }
    }

    @PostMapping("/profit-data-sync")
    public String runProfitDataSyncJob() {
        try {
            log.info("===== ProfitData 배치 수동 실행 요청 =====");

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestTime", LocalDateTime.now().toString())
                .toJobParameters();

            jobLauncher.run(profitDataSyncJob, jobParameters);

            log.info("===== ProfitData 배치 실행 완료 =====");
            return "ProfitData 배치 실행 성공: " + LocalDateTime.now();

        } catch (Exception e) {
            log.error("ProfitData 배치 실행 실패", e);
            return "ProfitData 배치 실행 실패: " + e.getMessage();
        }
    }

    @PostMapping("/store-data-sync")
    public String runStoreDataSyncJob() {
        try {
            log.info("===== StoreData 배치 수동 실행 요청 =====");

            JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestTime", LocalDateTime.now().toString())
                .toJobParameters();

            jobLauncher.run(storeDataSyncJob, jobParameters);

            log.info("===== StoreData 배치 실행 완료 =====");
            return "StoreData 배치 실행 성공: " + LocalDateTime.now();

        } catch (Exception e) {
            log.error("StoreData 배치 실행 실패", e);
            return "StoreData 배치 실행 실패: " + e.getMessage();
        }
    }
}
