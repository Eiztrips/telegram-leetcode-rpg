package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeSubmissionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeetCodeClientAdapterTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private LeetCodeClientAdapter adapter;

    @BeforeEach
    void setUp() {
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        adapter = new LeetCodeClientAdapter(restClientBuilder, redisTemplate);
    }

    @Test
    void getTodaySubmissions_ShouldReturnFilteredSubmissions_WhenResponseIsValid() {
        String username = "testUser";
        String taskSlug = "two-sum";

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("leetcode:tasks:difficulty", taskSlug)).thenReturn("Easy");

        long nowSeconds = Instant.now().getEpochSecond();
        LeetCodeSubmissionResponse.GraphQlSubmission submission =
                new LeetCodeSubmissionResponse.GraphQlSubmission("111", "Two Sum", taskSlug, String.valueOf(nowSeconds - 10));

        LeetCodeSubmissionResponse responseDto = new LeetCodeSubmissionResponse(
                new LeetCodeSubmissionResponse.Data(List.of(submission))
        );

        mockRestClientPost(responseDto);

        List<SubmissionData> result = adapter.getTodaySubmissions(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        SubmissionData data = result.get(0);
        assertEquals(111L, data.submissionId());
        assertEquals("two-sum", data.taskSlug());
        assertEquals("EASY", data.taskDifficulty());
    }

    @Test
    void getTodaySubmissions_ShouldFilterOutOldSubmissions() {
        String username = "testUser";
        String taskSlug = "old-task";

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("leetcode:tasks:difficulty", taskSlug)).thenReturn("Medium");

        long oldTimestamp = Instant.now().minusSeconds(3 * 24 * 60 * 60).getEpochSecond();
        LeetCodeSubmissionResponse.GraphQlSubmission oldSubmission =
                new LeetCodeSubmissionResponse.GraphQlSubmission("222", "Old Task", taskSlug, String.valueOf(oldTimestamp));

        LeetCodeSubmissionResponse responseDto = new LeetCodeSubmissionResponse(
                new LeetCodeSubmissionResponse.Data(List.of(oldSubmission))
        );

        mockRestClientPost(responseDto);

        List<SubmissionData> result = adapter.getTodaySubmissions(username);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void initCacheOnStartup_ShouldNotSync_WhenCacheIsFresh() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(hashOperations.size("leetcode:tasks:difficulty")).thenReturn(100L);

        long freshTimestamp = Instant.now().minusSeconds(60).toEpochMilli();
        when(valueOperations.get("leetcode:metadata:last_sync")).thenReturn(String.valueOf(freshTimestamp));

        adapter.initCacheOnStartup();

        verify(hashOperations, never()).putAll(anyString(), anyMap());
    }

    private void mockRestClientPost(LeetCodeSubmissionResponse responseDto) {
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(LeetCodeSubmissionResponse.class)).thenReturn(responseDto);
    }
}