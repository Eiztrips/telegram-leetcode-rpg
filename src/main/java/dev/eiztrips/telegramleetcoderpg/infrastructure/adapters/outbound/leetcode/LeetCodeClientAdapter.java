package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeAllQuestionsResponse;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeGraphQlRequest;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LeetCodeClientAdapter implements LeetCodeClientPort {

	private static final Logger log = LoggerFactory.getLogger(LeetCodeClientAdapter.class);

	private final RestClient restClient;
	private final RedisTemplate<String, String> redisTemplate;
	private static final String TASKS_HASH_KEY = "leetcode:tasks:difficulty";
	private static final String METADATA_SYNC_KEY = "leetcode:metadata:last_sync";
	private static final Duration SYNC_PERIOD = Duration.ofDays(7);

	private static final String RECENT_SUBMISSIONS_QUERY = """
			query recentAcSubmissions($username: String!, $limit: Int!) {
			  recentAcSubmissionList(username: $username, limit: $limit) {
			    id
			    title
			    titleSlug
			    timestamp
			  }
			}
			""";

	private static final String ALL_PROBLEMS_QUERY = """
			query allQuestions {
			  allQuestions {
			    titleSlug
			    difficulty
			  }
			}
			""";

	private static final int LIMIT = 10;

	public LeetCodeClientAdapter(RestClient.Builder restClientBuilder, RedisTemplate<String, String> redisTemplate) {
		this.restClient = restClientBuilder.baseUrl("https://leetcode.com/graphql")
				.defaultHeader("User-Agent", "Mozilla/5.0 (LeetQuestBot; Spring Boot 4)").build();
		this.redisTemplate = redisTemplate;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initCacheOnStartup() {
		log.info("Проверка актуальности кэша задач LeetCode в Redis...");

		Long cacheSize = redisTemplate.opsForHash().size(TASKS_HASH_KEY);
		String lastSyncStr = redisTemplate.opsForValue().get(METADATA_SYNC_KEY);

		boolean isCacheEmpty = cacheSize == null || cacheSize == 0;
		boolean isExpired = true;

		if (lastSyncStr != null) {
			Instant lastSync = Instant.ofEpochMilli(Long.parseLong(lastSyncStr));
			isExpired = Instant.now().isAfter(lastSync.plus(SYNC_PERIOD));
		}

		if (isCacheEmpty || isExpired) {
			syncAllTasksToRedis();
		} else {
			log.info("Кэш задач в Redis актуален. Записей: {}", cacheSize);
		}
	}

	// при горизонтальном масштабирование, от мутекса будет мало толку
	public synchronized void syncAllTasksToRedis() {
		log.info("Запуск выкачивания всех задач с LeetCode API...");
		try {
			Map<String, String> fetchedTasks = fetchAllTasksFromLeetCode();

			if (!fetchedTasks.isEmpty()) {
				redisTemplate.opsForHash().putAll(TASKS_HASH_KEY, fetchedTasks);
				redisTemplate.opsForValue().set(METADATA_SYNC_KEY, String.valueOf(Instant.now().toEpochMilli()));
				log.info("Синхронизация завершена. Успешно закэшировано задач: {}", fetchedTasks.size());
			}
		} catch (Exception e) {
			log.error("Не удалось синхронизировать задачи с LeetCode", e);
		}
	}

	@Override
	public List<SubmissionData> getTodaySubmissions(String leetcodeUsername) {
		LeetCodeGraphQlRequest request = new LeetCodeGraphQlRequest(RECENT_SUBMISSIONS_QUERY,
				new LeetCodeGraphQlRequest.Variables(leetcodeUsername, LIMIT));

		LeetCodeSubmissionResponse response = restClient.post().uri("").contentType(MediaType.APPLICATION_JSON)
				.body(request).retrieve().body(LeetCodeSubmissionResponse.class);

		if (response == null || response.data() == null || response.data().recentAcSubmissionList() == null) {
			return Collections.emptyList();
		}

		Instant oneDayAgo = Instant.now().minus(Duration.ofDays(1));

		return response.data().recentAcSubmissionList().stream()
				.map(gql -> SubmissionData.builder().submissionId(Long.parseLong(gql.id()))
						.completedAt(Instant.ofEpochSecond(Long.parseLong(gql.timestamp()))).taskSlug(gql.titleSlug())
						.taskTitle(gql.title())
						// fixme: line_139
						.taskDifficulty(getTaskDifficulty(gql.titleSlug())).build())
				.filter(data -> data.completedAt().isAfter(oneDayAgo)).toList();
	}

	private String getTaskDifficulty(String taskSlug) {
		Object difficulty = redisTemplate.opsForHash().get(TASKS_HASH_KEY, taskSlug);
		if (difficulty != null)
			return (String) difficulty;
		log.warn("Сложность для таски {} не найдена в Redis кэше", taskSlug);
		// fixme: это не круто(
		return "Medium";
	}

	private Map<String, String> fetchAllTasksFromLeetCode() {
		LeetCodeGraphQlRequest request = new LeetCodeGraphQlRequest(ALL_PROBLEMS_QUERY, null);
		LeetCodeAllQuestionsResponse response = restClient.post().uri("").contentType(MediaType.APPLICATION_JSON)
				.body(request).retrieve().body(LeetCodeAllQuestionsResponse.class);

		if (response == null || response.data() == null || response.data().allQuestions() == null) {
			return Collections.emptyMap();
		}

		return response.data().allQuestions().stream().filter(q -> q.titleSlug() != null && q.difficulty() != null)
				.collect(Collectors.toMap(LeetCodeAllQuestionsResponse.Question::titleSlug,
						LeetCodeAllQuestionsResponse.Question::difficulty, (existing, replacement) -> replacement));
	}
}
