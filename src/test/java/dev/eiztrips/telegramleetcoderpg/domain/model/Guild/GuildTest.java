package dev.eiztrips.telegramleetcoderpg.domain.model.Guild;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GuildTest {

    @Test
    void createGuildTest() {
        Guild guild = Guild.builder().id(0L).build();
        assertNotNull(guild);
    }

    @Test
    void createWithBadIdTest() {
        assertThrows(GlobalExceptions.ArgumentEmptyException.class, () -> Guild.builder().build());
    }
}
