package org.escape.gx.config;

import org.escape.gx.domain.gx.GxClassInfo;
import org.escape.gx.domain.gx.GxClassInfoRepository;
import org.escape.gx.domain.gx.GxSession;
import org.escape.gx.domain.gx.GxSessionRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 데모용 초기 GX 클래스/세션 데이터.
 *
 * @author gx
 * @since 1.0
 */
@Configuration
public class InitialDataLoader {

    @Bean
    public ApplicationRunner loadInitialGxData(GxClassInfoRepository classInfoRepository,
                                               GxSessionRepository sessionRepository) {
        return args -> {
            if (classInfoRepository.count() > 0) {
                return;
            }
            LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
            for (int i = 0; i < 5; i++) {
                LocalDateTime start = base.plusDays(i);
                String classId = "class-yoga-" + (i + 1);
                GxClassInfo yoga = GxClassInfo.builder()
                        .gxClassInfoId(classId)
                        .sessionName("오전 요가")
                        .sessionStartAt(start)
                        .sessionEndAt(start.plusMinutes(60))
                        .maxCapacity(15)
                        .minReservationCount(1)
                        .requiredMembershipCount(1)
                        .dayOfWeek(start.getDayOfWeek().getValue())
                        .build();
                classInfoRepository.save(yoga);
                GxSession session = GxSession.builder()
                        .gxSessionId("session-yoga-" + (i + 1))
                        .gxClassInfoId(classId)
                        .build();
                sessionRepository.save(session);
            }
            LocalDateTime pilatesStart = base.plusDays(1).withHour(14).withMinute(0);
            GxClassInfo pilates = GxClassInfo.builder()
                    .gxClassInfoId("class-pilates-1")
                    .sessionName("필라테스")
                    .sessionStartAt(pilatesStart)
                    .sessionEndAt(pilatesStart.plusMinutes(60))
                    .maxCapacity(10)
                    .requiredMembershipCount(1)
                    .build();
            classInfoRepository.save(pilates);
            sessionRepository.save(GxSession.builder()
                    .gxSessionId("session-pilates-1")
                    .gxClassInfoId(pilates.getGxClassInfoId())
                    .build());
        };
    }
}
