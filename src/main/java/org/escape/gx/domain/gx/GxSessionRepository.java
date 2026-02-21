package org.escape.gx.domain.gx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * GX 세션 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface GxSessionRepository extends JpaRepository<GxSession, String> {

    List<GxSession> findByGxClassInfoIdOrderByCreatedAtAsc(String gxClassInfoId);

    List<GxSession> findAllByOrderByCreatedAtAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from GxSession s where s.gxSessionId = :id")
    Optional<GxSession> findByIdForUpdate(@Param("id") String gxSessionId);

    @Query("select s from GxSession s join fetch s.gxClassInfo g where g.sessionStartAt > :now order by g.sessionStartAt asc")
    List<GxSession> findUpcomingWithClassInfo(@Param("now") LocalDateTime now);
}
