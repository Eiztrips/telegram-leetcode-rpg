package dev.eiztrips.telegramleetcoderpg;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootTest
@TestPropertySource(properties = {
        "telegram.bot.username=test_bot_name",
        "telegram.bot.token=test_bot_token"
})
class TelegramLeetcodeRpgApplicationTests {

    @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
    private RedisTemplate<String, String> redisTemplate;

    @MockitoBean
    private TelegramBotsApi telegramBotsApi;

    @Test
    void contextLoads() {
    }

}
