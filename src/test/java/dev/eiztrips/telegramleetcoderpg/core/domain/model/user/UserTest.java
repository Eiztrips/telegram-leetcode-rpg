package dev.eiztrips.telegramleetcoderpg.core.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.task.Difficulty;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.task.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final User u = new User(
            1L,
            "test_1",
            "https://leetcode.com/u/test/",
            0,
            null,
            null
    );

    @Test
    void createUserTest() {
        var user = new User(
                1L,
                "test_1",
                "https://leetcode.com/u/test/",
                0,
                null,
                null
        );

        assertNotNull(user.submissions());
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
                        null,
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
                        null,
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
                        null,
                        null
                )
        );
    }

    @Test
    void completeTaskTest() {
        Task task = new Task("two_poooo", Difficulty.EASY);
        User user = u.completeTask(task);
        assertEquals(user.submissions().getLast().task(), task);
        assertEquals(10, user.xp());

        Task task2 = new Task("two_pooooppoooo", Difficulty.MEDIUM);
        user = user.completeTask(task2);
        assertEquals(user.submissions().getLast().task(), task2);
        assertEquals(2, user.submissions().size());
        assertEquals(30, user.xp());
    }

    @Test
    void withLastCheckTimeTest() {
        Instant last = u.lastCheckTime().minusSeconds(5);
        User user = u.withLastCheckTime();
        assertNotEquals(last, user.lastCheckTime());
    }

    @Test
    void getCompletedTaskLastWeekTest() {
        Task task1 = new Task("1", Difficulty.EASY),
                task2 = new Task("2", Difficulty.EASY),
                task3 = new Task("3", Difficulty.HARD);

        Submission s1 = new Submission(task1, Instant.now().minus(Duration.ofDays(1))),
                s2 = new Submission(task2, Instant.now().minus(Duration.ofDays(8))),
                s3 = new Submission(task3, Instant.now().minus(Duration.ofDays(5)));

        User user = new User(
                1L,
                "u",
                "https://leetcode.com/u/test/",
                0,
                new java.util.LinkedList<>(java.util.List.of(s1, s2, s3)),
                null
        );

        assertEquals(2, user.getCompletedTasksLastWeek().size());

    }
}
