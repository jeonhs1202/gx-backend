package org.escape.gx.api.demo;

import org.escape.gx.api.membership.dto.MembershipResponse;
import org.escape.gx.domain.membership.Membership;
import org.escape.gx.service.MembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 데모용 API. 로그인한 사용자에게 회원권 발급 (테스트용).
 *
 * @author gx
 * @since 1.0
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final MembershipService membershipService;

    public DemoController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping("/grant-membership")
    public ResponseEntity<MembershipResponse> grantMembership(Authentication auth,
                                                              @RequestParam(defaultValue = "10") int count,
                                                              @RequestParam(defaultValue = "30") int validDays) {
        String userId = (String) auth.getPrincipal();
        Membership m = membershipService.grant(userId, count, validDays);
        return ResponseEntity.ok(new MembershipResponse(
                m.getMembershipId(),
                m.getUserId(),
                m.getStatus().name(),
                m.getStartDate(),
                m.getEndDate(),
                m.getRemainingCount(),
                m.getInitialCount(),
                m.getIssuedAt()
        ));
    }
}
