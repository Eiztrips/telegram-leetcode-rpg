package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.scheduler;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.LeetCodeClientAdapter;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.repository.LeetCodeTaskCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeetCodeCacheSchedulerTest {

    @Mock
    private LeetCodeTaskCacheRepository leetCodeTaskCacheRepository;

    @Mock
    private LeetCodeClientAdapter leetCodeClientAdapter;

    @InjectMocks
    private LeetCodeCacheScheduler scheduler;

    @Test
    void initCacheOnStartup_ShouldNotSync_WhenCacheIsFresh() {
        when(leetCodeTaskCacheRepository.getCacheSize()).thenReturn(150L);

        long freshTimestamp = Instant.now().toEpochMilli();
        when(leetCodeTaskCacheRepository.getLastSync()).thenReturn(String.valueOf(freshTimestamp));

        scheduler.initCacheOnStartup();

        verify(leetCodeClientAdapter, never()).syncAllTasksToRedis();
    }

    @Test
    void initCacheOnStartup_ShouldSync_WhenCacheIsEmpty() {
        when(leetCodeTaskCacheRepository.getCacheSize()).thenReturn(0L);

        scheduler.initCacheOnStartup();

        verify(leetCodeClientAdapter).syncAllTasksToRedis();
    }

    @Test
    void initCacheOnStartup_ShouldSync_WhenCacheSizeIsNull() {
        when(leetCodeTaskCacheRepository.getCacheSize()).thenReturn(null);

        scheduler.initCacheOnStartup();

        verify(leetCodeClientAdapter).syncAllTasksToRedis();
    }

    @Test
    void initCacheOnStartup_ShouldSync_WhenCacheIsExpired() {
        when(leetCodeTaskCacheRepository.getCacheSize()).thenReturn(150L);

        long expiredTimestamp = Instant.now().minusSeconds(8 * 24 * 60 * 60).toEpochMilli();
        when(leetCodeTaskCacheRepository.getLastSync()).thenReturn(String.valueOf(expiredTimestamp));

        scheduler.initCacheOnStartup();

        verify(leetCodeClientAdapter).syncAllTasksToRedis();
    }
}