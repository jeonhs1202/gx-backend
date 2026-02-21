package org.escape.gx.domain.membership;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.escape.gx.common.enums.MembershipStatus;

import java.time.Instant;
import java.time.LocalDate;

/**
 * 회원권.
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "membership")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {

    @Id
    @Column(name = "membership_id", length = 255)
    private String membershipId;

    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "payment_id", length = 255)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 255)
    private MembershipStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "remaining_count")
    private Integer remainingCount;

    @Column(name = "initial_count")
    private Integer initialCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public Membership(String membershipId, String userId, String paymentId,
                      LocalDate startDate, LocalDate endDate, Integer initialCount) {
        this.membershipId = membershipId;
        this.userId = userId;
        this.paymentId = paymentId;
        this.status = MembershipStatus.ACTIVE;
        this.startDate = startDate;
        this.endDate = endDate;
        this.issuedAt = Instant.now();
        this.remainingCount = initialCount != null ? initialCount : 0;
        this.initialCount = initialCount != null ? initialCount : 0;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * 예약 시 회원권 차감. 잔여 횟수가 부족하거나 만료 시 false.
     *
     * @param deductCount 차감 횟수
     * @return 차감 가능 여부
     */
    public boolean deduct(int deductCount) {
        if (status != MembershipStatus.ACTIVE || remainingCount == null || remainingCount < deductCount) {
            return false;
        }
        if (endDate != null && LocalDate.now().isAfter(endDate)) {
            return false;
        }
        this.remainingCount -= deductCount;
        this.updatedAt = Instant.now();
        return true;
    }

    /**
     * 취소 시 회원권 복구.
     *
     * @param refundCount 복구 횟수
     */
    public void refund(int refundCount) {
        if (refundCount <= 0) return;
        this.remainingCount = (this.remainingCount != null ? this.remainingCount : 0) + refundCount;
        this.updatedAt = Instant.now();
    }

    public boolean isUsable() {
        return status == MembershipStatus.ACTIVE
                && remainingCount != null && remainingCount > 0
                && (endDate == null || !LocalDate.now().isAfter(endDate));
    }
}
