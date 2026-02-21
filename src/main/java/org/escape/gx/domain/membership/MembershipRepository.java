package org.escape.gx.domain.membership;

import org.escape.gx.common.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * 회원권 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface MembershipRepository extends JpaRepository<Membership, String> {

    List<Membership> findByUserIdOrderByIssuedAtDesc(String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Membership m where m.membershipId = :id")
    Optional<Membership> findByIdForUpdate(@Param("id") String membershipId);

    List<Membership> findByUserIdAndStatus(String userId, MembershipStatus status);
}
