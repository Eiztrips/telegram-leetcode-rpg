package dev.eiztrips.telegramleetcoderpg.core.domain.model.boss;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.WeeklyBossExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeeklyBossTest {
    @Test
    void takeDamageTest() {
        WeeklyBoss boss = new WeeklyBoss(1L, "Test Boss", 1000, 10);
        boss = boss.takeDamage(1);
        assertEquals(9, boss.currentHp());
        boss = boss.takeDamage(10);
        assertEquals(0, boss.currentHp());
        assertThrows(
                WeeklyBossExceptions.WeeklyBossAlreadyDefeated.class,
                () -> new WeeklyBoss(1L, "Test Boss", 1000, 0).takeDamage(1)
        );
        assertThrows(
                WeeklyBossExceptions.InvalidDamageException.class,
                () -> new WeeklyBoss(1L, "Test Boss", 1000, 10).takeDamage(-1)
        );
    }
}
