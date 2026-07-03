package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionTest {

    @Test
    void submissionCreationTest() {
        Submission submission = Submission.builder()
                .taskSlug("two-sum")
                .taskDifficulty(Difficulty.EASY)
                .completedAt(java.time.Instant.now())
                .build();

        assertEquals("two-sum", submission.taskSlug());
        assertEquals(Difficulty.EASY, submission.taskDifficulty());
        assertNotNull(submission.completedAt());
    }

    @Test
    void submissionCreationWithNullTaskSlug() {
        assertThrows(GlobalExceptions.ArgumentEmptyException.class,
                () -> Submission.builder()
                        .taskDifficulty(Difficulty.EASY)
                        .completedAt(java.time.Instant.now())
                        .build());
    }

    @Test
    void submissionCreationWithNullTaskDifficulty() {
        assertThrows(GlobalExceptions.ArgumentEmptyException.class,
                () -> Submission.builder()
                        .taskSlug("two-sum")
                        .completedAt(java.time.Instant.now())
                        .build());
    }

    @Test
    void submissionCreationWithNullCompletedAt() {
        assertThrows(GlobalExceptions.ArgumentEmptyException.class,
                () -> Submission.builder()
                        .taskSlug("two-sum")
                        .taskDifficulty(Difficulty.EASY)
                        .build());
    }
}
