package dev.eiztrips.telegramleetcoderpg.domain.model.boss;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeeklyBossTest {

    @Test
    void weeklyBossCreationTest() {
        WeeklyBoss boss = WeeklyBoss.builder()
                .id(1L)
                .name("Test WeeklyBoss")
                .maxHp(1000)
                .currentHp(1000)
                .build();
        assertEquals(1L, boss.id());
        assertEquals("Test WeeklyBoss", boss.name());
        assertEquals(1000, boss.maxHp());
        assertEquals(1000, boss.currentHp());
    }

    @Test
    void weeklyBossCreationWithInvalidArguments() {
        assertThrows(GlobalExceptions.ArgumentEmptyException.class, () ->
                WeeklyBoss.builder()
                        .id(1L)
                        .maxHp(1000)
                        .currentHp(1000)
                        .build());
        assertThrows(GlobalExceptions.ArgumentEmptyException.class, () ->
                WeeklyBoss.builder()
                        .id(1L)
                        .name("")
                        .maxHp(1000)
                        .currentHp(1000)
                        .build());
        assertThrows(GlobalExceptions.ArgumentInvalidException.class, () ->
                WeeklyBoss.builder()
                        .id(1L)
                        .name("Test WeeklyBoss")
                        .maxHp(-1)
                        .currentHp(1000)
                        .build());
        assertThrows(GlobalExceptions.ArgumentInvalidException.class, () ->
                WeeklyBoss.builder()
                        .id(1L)
                        .name("Test WeeklyBoss")
                        .maxHp(1000)
                        .currentHp(-1)
                        .build());
        assertThrows(GlobalExceptions.ArgumentInvalidException.class, () ->
                WeeklyBoss.builder()
                        .id(1L)
                        .name("Test WeeklyBoss")
                        .maxHp(1000)
                        .currentHp(1500)
                        .build());
    }

    @Test
    void takeDamageTest() {
        WeeklyBoss boss = new WeeklyBoss(1L, "Test WeeklyBoss", 1000, 10, 0L);
        boss = boss.takeDamage(1);
        assertEquals(9, boss.currentHp());
        boss = boss.takeDamage(10);
        assertEquals(0, boss.currentHp());
        assertThrows(
                WeeklyBossExceptions.WeeklyBossAlreadyDefeated.class,
                () -> new WeeklyBoss(1L, "Test WeeklyBoss", 1000, 0, 0L).takeDamage(1)
        );
        assertThrows(
                GlobalExceptions.ArgumentInvalidException.class,
                () -> new WeeklyBoss(1L, "Test WeeklyBoss", 1000, 10, 0L).takeDamage(-1)
        );
    }
}
