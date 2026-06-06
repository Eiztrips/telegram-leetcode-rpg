package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final User u = new User(
            1L,
            "test_1",
            "https://leetcode.com/u/test/",
            0,
            null
    );

    @Test
    void createUserTest() {
        var user = new User(
                1L,
                "test_1",
                "https://leetcode.com/u/test/",
                0,
                null
        );

        assertNotNull(user.lastCheckTime());
    }

    @Test
    void createUserWithBadUsername() {
        assertThrows(
                UserExceptions.ArgumentEmptyException.class,
                () -> new User(
                        1L,
                        null,
                        "https://leetcode.com/u/test/",
                        0,
                        null
                )
        );
    }

    @Test
    void createUserWithBadLink() {
        assertThrows(
                UserExceptions.ArgumentEmptyException.class,
                () -> new User(
                        1L,
                        "test",
                        null,
                        0,
                        null
                )
        );
    }

    @Test
    void createUserWithBadPatternLink() {
        assertThrows(
                UserExceptions.InvalidLeetCodeUrlException.class,
                () -> new User(
                        1L,
                        "test",
                        "https://ya.ru/",
                        0,
                        null
                )
        );
    }

    @Test
    void takeRewardForSolveTaskTest() {
        User user = u.takeRewardForSolveTask(List.of(
                new Submission("two_poooo", Difficulty.EASY, Instant.now()),
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
}
