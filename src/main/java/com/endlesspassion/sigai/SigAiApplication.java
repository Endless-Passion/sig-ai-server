package com.endlesspassion.sigai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화 (BaseTimeEntity의 @CreatedDate, @LastModifiedDate 자동 처리)
@SpringBootApplication
public class SigAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SigAiApplication.class, args);
    }

}
