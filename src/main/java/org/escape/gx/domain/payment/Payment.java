package org.escape.gx.domain.payment;

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
import org.escape.gx.common.enums.PaymentStatus;

import java.time.Instant;

/**
 * 결제 정보.
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "payment_id", length = 255)
    private String paymentId;

    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "payment_method", length = 255)
    private String paymentMethod;

    @Column(name = "amount", length = 255)
    private String amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 255)
    private PaymentStatus paymentStatus;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "membership_count", length = 255)
    private String membershipCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public Payment(String paymentId, String userId, String paymentMethod, String amount,
                   PaymentStatus paymentStatus, String membershipCount) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.COMPLETED;
        this.paidAt = Instant.now();
        this.membershipCount = membershipCount;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}
