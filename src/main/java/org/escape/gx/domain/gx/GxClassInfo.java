package org.escape.gx.domain.gx;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * GX 클래스 기준정보.
 *
 * @author gx
 * @since 1.0
 */
@Entity
@Table(name = "gx_class_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GxClassInfo {

    @Id
    @Column(name = "gx_class_info_id", length = 255)
    private String gxClassInfoId;

    @Column(name = "session_name", length = 255)
    private String sessionName;

    @Column(name = "instructor_user_id", length = 255)
    private String instructorUserId;

    @Column(name = "session_start_at")
    private LocalDateTime sessionStartAt;

    @Column(name = "session_end_at")
    private LocalDateTime sessionEndAt;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "min_reservation_count")
    private Integer minReservationCount;

    @Column(name = "required_membership_count")
    private Integer requiredMembershipCount;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Builder
    public GxClassInfo(String gxClassInfoId, String sessionName, String instructorUserId,
                       LocalDateTime sessionStartAt, LocalDateTime sessionEndAt,
                       Integer maxCapacity, Integer minReservationCount, Integer requiredMembershipCount,
                       Integer dayOfWeek) {
        this.gxClassInfoId = gxClassInfoId;
        this.sessionName = sessionName;
        this.instructorUserId = instructorUserId;
        this.sessionStartAt = sessionStartAt;
        this.sessionEndAt = sessionEndAt;
        this.maxCapacity = maxCapacity != null ? maxCapacity : 20;
        this.minReservationCount = minReservationCount != null ? minReservationCount : 1;
        this.requiredMembershipCount = requiredMembershipCount != null ? requiredMembershipCount : 1;
        this.dayOfWeek = dayOfWeek;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public int getRequiredCount() {
        return requiredMembershipCount != null ? requiredMembershipCount : 1;
    }
}
