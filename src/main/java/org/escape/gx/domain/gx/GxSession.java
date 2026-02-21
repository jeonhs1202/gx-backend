package org.escape.gx.domain.gx;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.escape.gx.common.enums.ClassStatus;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * GX 클래스 세션 (실제 수업 일정).
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "gx_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GxSession {

    @Id
    @Column(name = "gx_session_id", length = 255)
    private String gxSessionId;

    @Column(name = "gx_class_info_id", nullable = false, length = 255)
    private String gxClassInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gx_class_info_id", insertable = false, updatable = false)
    private GxClassInfo gxClassInfo;

    /** 세션의 실제 시작 일시 (반복 강의의 경우 각 회차 날짜). */
    @Column(name = "session_start_at")
    private LocalDateTime sessionStartAt;

    @Column(name = "reserved_count")
    private Integer reservedCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 255)
    private ClassStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public GxSession(String gxSessionId, String gxClassInfoId, LocalDateTime sessionStartAt) {
        this.gxSessionId = gxSessionId;
        this.gxClassInfoId = gxClassInfoId;
        this.sessionStartAt = sessionStartAt;
        this.reservedCount = 0;
        this.status = ClassStatus.BEFORE_RESV;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void incrementReservedCount() {
        this.reservedCount = (this.reservedCount != null ? this.reservedCount : 0) + 1;
        this.updatedAt = Instant.now();
    }

    public void decrementReservedCount() {
        int current = this.reservedCount != null ? this.reservedCount : 0;
        if (current > 0) {
            this.reservedCount = current - 1;
            this.updatedAt = Instant.now();
        }
    }

    public void setStatus(ClassStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public boolean canReserve(Integer maxCapacity) {
        if (status != ClassStatus.BEFORE_RESV && status != ClassStatus.ON_RESV) {
            return false;
        }
        int max = maxCapacity != null ? maxCapacity : 20;
        int reserved = reservedCount != null ? reservedCount : 0;
        return reserved < max;
    }
}
