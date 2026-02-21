package org.escape.gx.domain.gx;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * GX 클래스 기준정보 저장소.
 *
 * @author gx
 * @since 1.0
 */
public interface GxClassInfoRepository extends JpaRepository<GxClassInfo, String> {

    List<GxClassInfo> findAllByOrderBySessionStartAtAsc();

    List<GxClassInfo> findByInstructorUserIdOrderBySessionStartAtAsc(String instructorUserId);
}
