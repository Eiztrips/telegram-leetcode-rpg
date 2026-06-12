package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisLeetCodeTaskCacheRepositoryTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RedisLeetCodeTaskCacheRepository repository;

    private static final String TASKS_HASH_KEY = "leetcode:tasks:difficulty";
    private static final String METADATA_SYNC_KEY = "leetcode:metadata:last_sync";

    @BeforeEach
    void setUp() {
        repository = new RedisLeetCodeTaskCacheRepository(redisTemplate);
    }

    @Test
    void getDifficulty_ShouldReturnDifficulty_WhenKeyExists() {
        String taskSlug = "two-sum";
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(TASKS_HASH_KEY, taskSlug)).thenReturn("Easy");

        String result = repository.getDifficulty(taskSlug);

        assertEquals("Easy", result);
    }

    @Test
    void getDifficulty_ShouldReturnDefaultMedium_WhenKeyDoesNotExist() {
        String taskSlug = "unknown-task";
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(TASKS_HASH_KEY, taskSlug)).thenReturn(null);

        String result = repository.getDifficulty(taskSlug);

        assertEquals("MEDIUM", result);
    }

    @Test
    void getCacheSize_ShouldReturnCorrectSize() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.size(TASKS_HASH_KEY)).thenReturn(42L);

        Long size = repository.getCacheSize();

        assertEquals(42L, size);
    }

    @Test
    void getLastSync_ShouldReturnTimestampString() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(METADATA_SYNC_KEY)).thenReturn("1718192400000");

        String lastSync = repository.getLastSync();

        assertEquals("1718192400000", lastSync);
    }

    @Test
    void saveAllTasks_ShouldInvokePutAll() {
        Map<String, String> tasks = Map.of("two-sum", "Easy");
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        repository.saveAllTasks(tasks);

        verify(hashOperations).putAll(TASKS_HASH_KEY, tasks);
    }

    @Test
    void updateLastSyncData_ShouldSetCurrentTimestamp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        repository.updateLastSyncData();

        verify(valueOperations).set(eq(METADATA_SYNC_KEY), anyString());
    }
}