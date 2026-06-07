package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.SubmissionExceptions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final User u = User.builder()
            .telegramId(1L)
            .username("test_1")
            .leetcodeURL("https://leetcode.com/u/test/")
            .build();

    @Test
    void createUserTest() {
        var user = User.builder()
                .telegramId(1L)
                .username("test_1")
                .leetcodeURL("https://leetcode.com/u/test/")
                .build();

        assertEquals(1L, user.telegramId());
        assertEquals("test_1", user.username());
        assertEquals("https://leetcode.com/u/test/", user.leetcodeURL());
        assertEquals(0, user.xp());
        assertNotNull(user.lastCheckTime());
    }

    @Test
    void createUserWithBadTelegramId() {
        assertThrows(
                GlobalExceptions.ArgumentEmptyException.class,
                () -> User.builder()
                        .username("test_1")
                        .leetcodeURL("https://leetcode.com/u/test/")
                        .build()
        );
    }

    @Test
    void createUserWithBadUsername() {
        assertThrows(
                GlobalExceptions.ArgumentEmptyException.class,
                () -> User.builder()
                        .telegramId(1L)
                        .leetcodeURL("https://leetcode.com/u/test/")
                        .build()
        );
    }

    @Test
    void createUserWithBadLink() {
        assertThrows(
                GlobalExceptions.ArgumentEmptyException.class,
                () -> User.builder()
                        .telegramId(1L)
                        .username("test_1")
                        .build()
        );
    }

    @Test
    void createUserWithBadPatternLink() {
        assertThrows(
                GlobalExceptions.ArgumentInvalidException.class,
                () -> User.builder()
                        .telegramId(1L)
                        .username("test_1")
                        .leetcodeURL("https://ya.ru/u/test")
                        .build()
        );
    }

    @Test
    void takeRewardForSolveTaskTest() {
        User user = u.takeRewardForSolveTask(List.of(
                Submission.builder().taskSlug("one_pooooppoooo").taskDifficulty(Difficulty.EASY).completedAt(Instant.now()).build(),
                new Submission("three_poooo", Difficulty.HARD, Instant.now())
        ));
        assertEquals(40, user.xp());

        user = user.takeRewardForSolveTask(
                List.of(
                        new Submission("two_pooooppoooo", Difficulty.MEDIUM, Instant.now())
                )
        );
        assertEquals(60, user.xp());
    }

    @Test
    void withLastCheckTimeTest() {
        Instant last = u.lastCheckTime().minusSeconds(5);
        User user = u.withLastCheckTime();
        assertNotEquals(last, user.lastCheckTime());
    }

    @Test
    void validateCheckRateLimitTest() {
        User user1 = new User(
                1L,
                "name",
                "https://leetcode.com/u/test/",
                0,
                Instant.now().minus(Duration.ofHours(1))
        );

        assertThrows(
                SubmissionExceptions.SubmissionCheckRateLimitException.class,
                user1::validateCheckRateLimit
        );

        User user2 = new User(
                1L,
                "name",
                "https://leetcode.com/u/test/",
                0,
                Instant.now().minus(Duration.ofDays(1))
        );

        assertDoesNotThrow(user2::validateCheckRateLimit);

    }
}
