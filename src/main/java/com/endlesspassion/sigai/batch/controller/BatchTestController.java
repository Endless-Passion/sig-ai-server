package com.endlesspassion.sigai.batch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchTestController {

    private final JobLauncher jobLauncher;
    private final Job seoulDataSyncJob;

    /**
     * 서울시 데이터 동기화 배치 수동 실행
     * POST http://localhost:8080/api/batch/seoul-data-sync
     */
    @PostMapping("/seoul-data-sync")
    public String runSeoulDataSyncJob() {
        try {
            log.info("===== 배치 수동 실행 요청 =====");

            // JobParameters에 현재 시간을 추가 (매번 새로운 Job Instance 생성)
            JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestTime", LocalDateTime.now().toString())
                .toJobParameters();

            jobLauncher.run(seoulDataSyncJob, jobParameters);

            log.info("===== 배치 실행 완료 =====");
            return "배치 실행 성공: " + LocalDateTime.now();

        } catch (Exception e) {
            log.error("배치 실행 실패", e);
            return "배치 실행 실패: " + e.getMessage();
        }
    }
}
