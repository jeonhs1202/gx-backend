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
import org.escape.gx.common.enums.TransactionType;

import java.time.Instant;

/**
 * 회원권 원장 (거래 이력).
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "membership_ledger")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MembershipLedger {

    @Id
    @Column(name = "ledger_id", length = 255)
    private String ledgerId;

    @Column(name = "membership_id", length = 255)
    private String membershipId;

    @Column(name = "user_id", length = 255)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 255)
    private TransactionType transactionType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "balance_after")
    private Integer balanceAfter;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @Builder
    public MembershipLedger(String ledgerId, String membershipId, String userId,
                            TransactionType transactionType, Integer quantity, Integer balanceAfter,
                            String description, String createdBy, String sessionId) {
        this.ledgerId = ledgerId;
        this.membershipId = membershipId;
        this.userId = userId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.createdBy = createdBy;
        this.sessionId = sessionId;
        this.createdAt = Instant.now();
    }
}
