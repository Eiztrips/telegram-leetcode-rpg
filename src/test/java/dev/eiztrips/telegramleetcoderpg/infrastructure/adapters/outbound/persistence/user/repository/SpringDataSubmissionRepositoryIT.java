package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.SubmissionEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpringDataSubmissionRepositoryIT {

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private SpringDataSubmissionRepository submissionRepository;

    @Test
    void findWeeklySubmissions_ShouldReturnOnlyRecentSubmissionsForSpecificUser() {
        Long targetUserId = 12345L;
        Long otherUserId = 67890L;
        Instant now = Instant.now();
        Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);

        UserEntity targetUser = UserEntity.builder()
                .telegramId(targetUserId)
                .username("target_user")
                .leetcodeURL("url1")
                .build();
        userRepository.save(targetUser);

        UserEntity otherUser = UserEntity.builder()
                .telegramId(otherUserId)
                .username("other_user")
                .leetcodeURL("url2")
                .build();
        userRepository.save(otherUser);

        SubmissionEntity validSubmission = SubmissionEntity.builder()
                .submissionId(101L)
                .taskTitle("Task 1")
                .taskSlug("task-1")
                .taskDifficulty("EASY")
                .completedAt(now.minus(2, ChronoUnit.DAYS))
                .user(targetUser)
                .build();

        SubmissionEntity oldSubmission = SubmissionEntity.builder()
                .submissionId(102L)
                .taskTitle("Task 2")
                .taskSlug("task-2")
                .taskDifficulty("MEDIUM")
                .completedAt(now.minus(10, ChronoUnit.DAYS))
                .user(targetUser)
                .build();

        SubmissionEntity otherUserSubmission = SubmissionEntity.builder()
                .submissionId(103L)
                .taskTitle("Task 3")
                .taskSlug("task-3")
                .taskDifficulty("HARD")
                .completedAt(now.minus(1, ChronoUnit.DAYS))
                .user(otherUser)
                .build();

        submissionRepository.saveAll(List.of(validSubmission, oldSubmission, otherUserSubmission));

        List<SubmissionEntity> result = submissionRepository.findWeeklySubmissions(targetUserId, oneWeekAgo);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubmissionId()).isEqualTo(101L);
    }
}