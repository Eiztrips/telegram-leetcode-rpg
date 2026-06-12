package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.scheduler;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.LeetCodeClientAdapter;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.repository.LeetCodeTaskCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeetCodeCacheScheduler {

	private final LeetCodeTaskCacheRepository leetCodeTaskCacheRepository;
	private final LeetCodeClientAdapter leetCodeClientAdapter;
	private static final Duration SYNC_PERIOD = Duration.ofDays(7);

	@EventListener(ApplicationReadyEvent.class)
	public void initCacheOnStartup() {
		log.info("Проверка актуальности кэша задач LeetCode в Redis...");

		Long cacheSize = leetCodeTaskCacheRepository.getCacheSize();
		String lastSyncStr = leetCodeTaskCacheRepository.getLastSync();

		boolean isCacheEmpty = cacheSize == null || cacheSize == 0;
		boolean isExpired = true;

		if (lastSyncStr != null) {
			Instant lastSync = Instant.ofEpochMilli(Long.parseLong(lastSyncStr));
			isExpired = Instant.now().isAfter(lastSync.plus(SYNC_PERIOD));
		}

		if (isCacheEmpty || isExpired) {
			leetCodeClientAdapter.syncAllTasksToRedis();
		} else {
			log.info("Кэш задач в Redis актуален. Записей: {}", cacheSize);
		}
	}
}
