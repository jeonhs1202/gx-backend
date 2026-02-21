package org.escape.gx.service;

import org.escape.gx.api.auth.dto.LoginRequest;
import org.escape.gx.api.auth.dto.RegisterRequest;
import org.escape.gx.api.auth.dto.TokenResponse;
import org.escape.gx.common.enums.AccountCode;
import org.escape.gx.common.enums.UserStatus;
import org.escape.gx.config.JwtSupport;
import org.escape.gx.domain.account.Account;
import org.escape.gx.domain.account.AccountRepository;
import org.escape.gx.domain.account.UserProfile;
import org.escape.gx.domain.account.UserProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 인증 서비스. 회원가입, 로그인, 토큰 갱신.
 *
 * @author gx
 * @since 1.0
 */
@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSupport jwtSupport;

    public AuthService(AccountRepository accountRepository,
                       UserProfileRepository userProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtSupport jwtSupport) {
        this.accountRepository = accountRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSupport = jwtSupport;
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        AccountCode code = request.accountCode() != null ? request.accountCode() : AccountCode.USER;
        String userId = UUID.randomUUID().toString();
        Account account = Account.builder()
                .userId(userId)
                .accountCode(code)
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .userStatus(UserStatus.ACTIVE)
                .build();
        accountRepository.save(account);
        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .name(request.name())
                .phone(request.phone())
                .build();
        userProfileRepository.save(profile);

        account.updateLastLogin();
        accountRepository.save(account);
        String accessToken = jwtSupport.createAccessToken(account.getUserId(), account.getEmail(), code.name());
        String refreshToken = jwtSupport.createRefreshToken(account.getUserId());
        return new TokenResponse(accessToken, refreshToken, userId, account.getEmail(), code.name(), request.name());
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호를 확인해 주세요."));
        if (!passwordEncoder.matches(request.password(), account.getPasswordHash())) {
            account.incrementPasswordFailCount();
            accountRepository.save(account);
            throw new IllegalArgumentException("이메일 또는 비밀번호를 확인해 주세요.");
        }
        if (!account.isActive()) {
            throw new IllegalStateException("정지되었거나 탈퇴한 계정입니다.");
        }
        account.updateLastLogin();
        accountRepository.save(account);
        String accountCode = account.getAccountCode() != null ? account.getAccountCode().name() : AccountCode.USER.name();
        String name = userProfileRepository.findById(account.getUserId())
                .map(UserProfile::getName).orElse(null);
        String accessToken = jwtSupport.createAccessToken(account.getUserId(), account.getEmail(), accountCode);
        String refreshToken = jwtSupport.createRefreshToken(account.getUserId());
        return new TokenResponse(accessToken, refreshToken, account.getUserId(), account.getEmail(), accountCode, name);
    }

    public TokenResponse refreshToken(String refreshToken) {
        var claims = jwtSupport.parseToken(refreshToken);
        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
        String userId = claims.getSubject();
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        String accountCode = account.getAccountCode() != null ? account.getAccountCode().name() : AccountCode.USER.name();
        String name = userProfileRepository.findById(userId).map(UserProfile::getName).orElse(null);
        String accessToken = jwtSupport.createAccessToken(account.getUserId(), account.getEmail(), accountCode);
        String newRefreshToken = jwtSupport.createRefreshToken(account.getUserId());
        return new TokenResponse(accessToken, newRefreshToken, userId, account.getEmail(), accountCode, name);
    }
}
