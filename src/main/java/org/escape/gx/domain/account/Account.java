package org.escape.gx.domain.account;

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
import org.escape.gx.common.enums.AccountCode;
import org.escape.gx.common.enums.SubscriptionActive;
import org.escape.gx.common.enums.UserStatus;

import java.time.Instant;

/**
 * 계정 정보 (계정관리).
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @Column(name = "user_id", length = 255)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_code", length = 255)
    private AccountCode accountCode;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "password_fail_count", length = 255)
    private String passwordFailCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", length = 255)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_active", length = 255)
    private SubscriptionActive subscriptionActive;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "payment_at")
    private Instant paymentAt;

    @Column(name = "payment_method", length = 255)
    private String paymentMethod;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public Account(String userId, AccountCode accountCode, String email, String passwordHash,
                   UserStatus userStatus, SubscriptionActive subscriptionActive) {
        this.userId = userId;
        this.accountCode = accountCode != null ? accountCode : AccountCode.USER;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordFailCount = "0";
        this.userStatus = userStatus != null ? userStatus : UserStatus.ACTIVE;
        this.subscriptionActive = subscriptionActive != null ? subscriptionActive : SubscriptionActive.N;
        this.lastLoginAt = null;
        this.paymentAt = null;
        this.paymentMethod = null;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void updateLastLogin() {
        this.lastLoginAt = Instant.now();
        this.updatedAt = Instant.now();
        this.passwordFailCount = "0";
    }

    public void updatePassword(String passwordHash) {
        this.passwordHash = passwordHash;
        this.updatedAt = Instant.now();
        this.passwordFailCount = "0";
    }

    public void incrementPasswordFailCount() {
        int count = Integer.parseInt(this.passwordFailCount != null ? this.passwordFailCount : "0");
        this.passwordFailCount = String.valueOf(count + 1);
        this.updatedAt = Instant.now();
    }

    public boolean isActive() {
        return this.userStatus == UserStatus.ACTIVE;
    }
}
