package org.escape.gx.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 프로필 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
