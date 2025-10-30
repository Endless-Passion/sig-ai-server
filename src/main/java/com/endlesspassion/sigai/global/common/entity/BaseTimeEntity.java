package com.endlesspassion.sigai.global.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성/수정 시각을 자동으로 관리하는 Base Entity
 * 모든 엔티티는 이 클래스를 상속받아 생성/수정 시각을 공통으로 관리합니다.
 *
 * @MappedSuperclass: JPA 엔티티 클래스가 상속받을 경우 필드를 컬럼으로 인식
 * @EntityListeners(AuditingEntityListener.class): JPA Auditing 기능 활성화
 *
 * 사용 예시:
 * @Entity
 * public class Store extends BaseTimeEntity {
 *     // 자동으로 createdAt, updatedAt 필드 상속
 * }
 *
 * 참고: Application 클래스에 @EnableJpaAuditing 어노테이션 필요
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}