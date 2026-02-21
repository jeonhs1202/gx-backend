package org.escape.gx.service;

import org.escape.gx.common.enums.TransactionType;
import org.escape.gx.domain.membership.Membership;
import org.escape.gx.domain.membership.MembershipLedger;
import org.escape.gx.domain.membership.MembershipLedgerRepository;
import org.escape.gx.domain.membership.MembershipRepository;
import org.escape.gx.common.enums.MembershipStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 회원권 차감·원복 및 원장 기록.
 *
 * @author gx
 * @since 1.0
 */
@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipLedgerRepository ledgerRepository;

    public MembershipService(MembershipRepository membershipRepository,
                              MembershipLedgerRepository ledgerRepository) {
        this.membershipRepository = membershipRepository;
        this.ledgerRepository = ledgerRepository;
    }

    /**
     * 예약 시 회원권 차감 및 원장 기록.
     *
     * @param userId       사용자 ID
     * @param deductCount  차감 횟수
     * @param sessionId    GX 세션 ID (원장 설명용)
     * @param description  거래 설명
     * @return 차감된 회원권. 실패 시 null
     */
    @Transactional
    public Membership deductForReservation(String userId, int deductCount, String sessionId, String description) {
        List<Membership> candidates = membershipRepository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE);
        for (Membership m : candidates) {
            if (!m.isUsable() || m.getRemainingCount() == null || m.getRemainingCount() < deductCount) {
                continue;
            }
            Membership membership = membershipRepository.findByIdForUpdate(m.getMembershipId()).orElse(null);
            if (membership == null || !membership.deduct(deductCount)) {
                continue;
            }
            membershipRepository.save(membership);
            int balanceAfter = membership.getRemainingCount();
            MembershipLedger ledger = MembershipLedger.builder()
                    .ledgerId(UUID.randomUUID().toString())
                    .membershipId(membership.getMembershipId())
                    .userId(userId)
                    .transactionType(TransactionType.DEDUCT)
                    .quantity(-deductCount)
                    .balanceAfter(balanceAfter)
                    .description(description != null ? description : "GX 예약 차감")
                    .createdBy(userId)
                    .sessionId(sessionId)
                    .build();
            ledgerRepository.save(ledger);
            return membership;
        }
        return null;
    }

    /**
     * 예약 취소 시 회원권 복구 및 원장 기록.
     *
     * @param membershipId 회원권 ID
     * @param userId       사용자 ID
     * @param refundCount  복구 횟수
     * @param sessionId    GX 세션 ID
     * @param description  거래 설명
     */
    @Transactional
    public void refundForCancellation(String membershipId, String userId, int refundCount,
                                      String sessionId, String description) {
        Membership membership = membershipRepository.findByIdForUpdate(membershipId).orElse(null);
        if (membership == null) return;
        membership.refund(refundCount);
        membershipRepository.save(membership);
        MembershipLedger ledger = MembershipLedger.builder()
                .ledgerId(UUID.randomUUID().toString())
                .membershipId(membershipId)
                .userId(userId)
                .transactionType(TransactionType.REFUND)
                .quantity(refundCount)
                .balanceAfter(membership.getRemainingCount())
                .description(description != null ? description : "예약 취소 복구")
                .createdBy(userId)
                .sessionId(sessionId)
                .build();
        ledgerRepository.save(ledger);
    }

    /**
     * 회원권 발급 (데모/관리자용). GRANT 원장 기록.
     *
     * @param userId     사용자 ID
     * @param count      발급 횟수
     * @param validDays  유효 일수
     * @return 발급된 회원권
     */
    @Transactional
    public Membership grant(String userId, int count, int validDays) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(validDays);
        String membershipId = UUID.randomUUID().toString();
        Membership membership = Membership.builder()
                .membershipId(membershipId)
                .userId(userId)
                .paymentId(null)
                .startDate(start)
                .endDate(end)
                .initialCount(count)
                .build();
        membershipRepository.save(membership);
        MembershipLedger ledger = MembershipLedger.builder()
                .ledgerId(UUID.randomUUID().toString())
                .membershipId(membershipId)
                .userId(userId)
                .transactionType(TransactionType.GRANT)
                .quantity(count)
                .balanceAfter(count)
                .description("회원권 발급")
                .createdBy(userId)
                .sessionId(null)
                .build();
        ledgerRepository.save(ledger);
        return membership;
    }

    /**
     * 사용 가능한 회원권 조회 (락 없음). 대시 등 조회용.
     */
    @Transactional(readOnly = true)
    public Optional<Membership> findOneUsable(String userId, int requiredCount) {
        return membershipRepository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE)
                .stream()
                .filter(m -> m.isUsable() && m.getRemainingCount() != null && m.getRemainingCount() >= requiredCount)
                .findFirst();
    }
}
