package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisLeetCodeTaskCacheRepository implements LeetCodeTaskCacheRepository {

	private final RedisTemplate<String, String> redisTemplate;
	private static final String TASKS_HASH_KEY = "leetcode:tasks:difficulty";
	private static final String METADATA_SYNC_KEY = "leetcode:metadata:last_sync";

	@Override
	public String getDifficulty(String taskSlug) {
		Object difficulty = redisTemplate.opsForHash().get(TASKS_HASH_KEY, taskSlug);
		return difficulty != null ? (String) difficulty : "MEDIUM";
	}

	@Override
	public Long getCacheSize() {
		return redisTemplate.opsForHash().size(TASKS_HASH_KEY);
	}

	@Override
	public String getLastSync() {
		return redisTemplate.opsForValue().get(METADATA_SYNC_KEY);
	}

	@Override
	public void saveAllTasks(Map<String, String> tasks) {
		redisTemplate.opsForHash().putAll(TASKS_HASH_KEY, tasks);
	}

	@Override
	public void updateLastSyncData() {
		redisTemplate.opsForValue().set(METADATA_SYNC_KEY, String.valueOf(Instant.now().toEpochMilli()));
	}

}
