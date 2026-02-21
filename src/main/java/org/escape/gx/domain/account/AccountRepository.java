package org.escape.gx.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 계정 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
