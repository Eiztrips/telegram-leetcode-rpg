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
        "telegram.bot.token=test_bot_token",
        "boss.adjectives.file=classpath:static/boss-adjectives.txt",
        "boss.hp.min=100",
        "boss.hp.max=500",
        "boss.fallback.name=Разрушитель api Eiztrips'а",
        "boss.random.name.api=https://eiztrips.dev/api/fwd/funny-word",
        "inactive.days.alarm=7",
        "inactive.days.delete=30"
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
