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
import org.escape.gx.common.enums.ReservationStatus;

import java.time.Instant;

/**
 * GX 클래스 예약.
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @Column(name = "reservation_id", length = 255)
    private String reservationId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "gx_session_id", nullable = false, length = 255)
    private String gxSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gx_session_id", insertable = false, updatable = false)
    private GxSession gxSession;

    @Column(name = "membership_id", length = 255)
    private String membershipId;

    @Column(name = "deduct_count")
    private Integer deductCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 255)
    private ReservationStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public Reservation(String reservationId, String userId, String gxSessionId,
                       String membershipId, Integer deductCount) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.gxSessionId = gxSessionId;
        this.membershipId = membershipId;
        this.deductCount = deductCount != null ? deductCount : 1;
        this.status = ReservationStatus.RESERVED;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public boolean isReserved() {
        return this.status == ReservationStatus.RESERVED;
    }
}
