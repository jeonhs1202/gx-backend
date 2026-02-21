package org.escape.gx.domain.membership;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 회원권 원장 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface MembershipLedgerRepository extends JpaRepository<MembershipLedger, String> {

    List<MembershipLedger> findByUserIdOrderByCreatedAtDesc(String userId, org.springframework.data.domain.Pageable pageable);

    List<MembershipLedger> findByMembershipIdOrderByCreatedAtDesc(String membershipId);
}
