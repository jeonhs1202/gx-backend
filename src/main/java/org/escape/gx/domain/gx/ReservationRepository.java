package org.escape.gx.domain.gx;

import org.escape.gx.common.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * GX 예약 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Reservation> findByUserIdAndStatus(String userId, ReservationStatus status);

    List<Reservation> findByGxSessionIdAndStatus(String gxSessionId, ReservationStatus status);

    Optional<Reservation> findByUserIdAndGxSessionIdAndStatus(String userId, String gxSessionId, ReservationStatus status);

    boolean existsByUserIdAndGxSessionIdAndStatus(String userId, String gxSessionId, ReservationStatus status);
}
