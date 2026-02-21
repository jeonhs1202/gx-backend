package org.escape.gx.api.membership;

import org.escape.gx.api.membership.dto.MembershipResponse;
import org.escape.gx.domain.membership.Membership;
import org.escape.gx.domain.membership.MembershipRepository;
import org.escape.gx.service.MembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 회원권 조회 API.
 *
 * @author gx
 * @since 1.0
 */
@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipRepository membershipRepository;

    public MembershipController(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<List<MembershipResponse>> myMemberships(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        List<Membership> list = membershipRepository.findByUserIdOrderByIssuedAtDesc(userId);
        List<MembershipResponse> responses = list.stream()
                .map(m -> new MembershipResponse(
                        m.getMembershipId(),
                        m.getUserId(),
                        m.getStatus() != null ? m.getStatus().name() : null,
                        m.getStartDate(),
                        m.getEndDate(),
                        m.getRemainingCount(),
                        m.getInitialCount(),
                        m.getIssuedAt()
                ))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
