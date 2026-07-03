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
            .leetcodeUsername("test_1")
            .lastCheckTime(Instant.now())
            .build();

    @Test
    void createUserTest() {
        var user = User.builder()
                .telegramId(1L)
                .leetcodeUsername("test_1")
                .build();

        assertEquals(1L, user.telegramId());
        assertEquals("test_1", user.leetcodeUsername());
        assertEquals(0, user.xp());
        assertNull(user.lastCheckTime());
    }

    @Test
    void createUserWithBadTelegramId() {
        assertThrows(
                GlobalExceptions.ArgumentEmptyException.class,
                () -> User.builder()
                        .leetcodeUsername("test_1")
                        .build()
        );
    }

    @Test
    void createUserWithBadLeetCodeUsername() {
        assertThrows(
                GlobalExceptions.ArgumentEmptyException.class,
                () -> User.builder()
                        .telegramId(1L)
                        .build()
        );
    }

    @Test
    void takeRewardForSolveTaskTest() {
        User user = u.takeRewardForSolveTask(List.of(
                Submission.builder().taskSlug("one_pooooppoooo").taskDifficulty(Difficulty.EASY).completedAt(Instant.now()).build(),
                new Submission("three_poooo", Difficulty.HARD, Instant.now())
        ));
        assertEquals(Difficulty.HARD.getReward()+Difficulty.EASY.getReward(), user.xp());

        user = user.takeRewardForSolveTask(
                List.of(
                        new Submission("two_pooooppoooo", Difficulty.MEDIUM, Instant.now())
                )
        );
        assertEquals(Difficulty.HARD.getReward()+Difficulty.EASY.getReward()+Difficulty.MEDIUM.getReward(), user.xp());
    }

    @Test
    void withLastCheckTimeTest() {
        Instant last = u.lastCheckTime().minusSeconds(5);
        User user = u.withLastCheckTime();
        assertNotEquals(last, user.lastCheckTime());
    }

    @Test
    void withGuild() {
        User user = User.builder().telegramId(1L).leetcodeUsername("u").build();
        assertNull(user.guildId());

        user = user.withGuild(0L);
        assertEquals(0L, user.guildId());
    }

    @Test
    void withoutGuild() {
        User user = User.builder().telegramId(1L).guildId(0L).leetcodeUsername("u").build();
        user = user.withoutGuild();
        assertNull(user.guildId());
    }

    @Test
    void validateCheckRateLimitTest() {
        User user1 = new User(
                1L,
                "name",
                0,
                Instant.now().minus(Duration.ofMinutes(10)),
                null
        );

        assertThrows(
                SubmissionExceptions.SubmissionCheckRateLimitException.class,
                user1::validateCheckRateLimit
        );

        User user2 = new User(
                1L,
                "name",
                0,
                Instant.now().minus(Duration.ofDays(1)),
                null
        );

        assertDoesNotThrow(user2::validateCheckRateLimit);
    }
}
