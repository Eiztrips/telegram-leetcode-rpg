package dev.eiztrips.telegramleetcoderpg;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class TelegramLeetcodeRpgApplicationTests {

    @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void contextLoads() {
    }

}
