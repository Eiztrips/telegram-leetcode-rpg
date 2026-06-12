package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LeetCodeTaskCacheRepository {
	String getDifficulty(String taskSlug);
	Long getCacheSize();
	String getLastSync();
	void saveAllTasks(Map<String, String> tasks);
	void updateLastSyncData();
}
