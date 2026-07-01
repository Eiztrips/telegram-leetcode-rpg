package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeAllQuestionsResponse;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeGraphQlRequest;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeSubmissionResponse;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto.LeetCodeUserBioResponse;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.repository.LeetCodeTaskCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LeetCodeClientAdapter implements LeetCodeClientPort {

	private final RestClient restClient;
	private final LeetCodeTaskCacheRepository leetCodeTaskCacheRepository;

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

	private static final String USER_BIO_QUERY = """
			query getUserProfileDescription($username: String!) {
			  matchedUser(username: $username) {
			    profile {
			      aboutMe
			    }
			  }
			}
			""";

	private static final int LIMIT = 10;

	public LeetCodeClientAdapter(RestClient.Builder restClientBuilder,
			LeetCodeTaskCacheRepository leetCodeTaskCacheRepository) {
		this.restClient = restClientBuilder.baseUrl("https://leetcode.com/graphql")
				.defaultHeader("User-Agent", "Mozilla/5.0 (LeetQuestBot; Spring Boot 4)").build();
		this.leetCodeTaskCacheRepository = leetCodeTaskCacheRepository;
	}

	// при горизонтальном масштабирование, от мутекса будет мало толку
	public synchronized void syncAllTasksToRedis() {
		log.info("Запуск выкачивания всех задач с LeetCode API...");
		try {
			Map<String, String> fetchedTasks = fetchAllTasksFromLeetCode();

			if (!fetchedTasks.isEmpty()) {
				leetCodeTaskCacheRepository.saveAllTasks(fetchedTasks);
				leetCodeTaskCacheRepository.updateLastSyncData();
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
						.taskTitle(gql.title()).taskDifficulty(getTaskDifficulty(gql.titleSlug()).toUpperCase())
						.build())
				.filter(data -> data.completedAt().isAfter(oneDayAgo)).toList();
	}

	@Override
	public String getBio(String leetcodeUsername) {
		LeetCodeGraphQlRequest request = new LeetCodeGraphQlRequest(USER_BIO_QUERY,
				new LeetCodeGraphQlRequest.Variables(leetcodeUsername, null));
		LeetCodeUserBioResponse response = restClient.post().uri("").contentType(MediaType.APPLICATION_JSON)
				.body(request).retrieve().body(LeetCodeUserBioResponse.class);

		if (response == null || response.data() == null || response.data().matchedUser() == null
				|| response.data().matchedUser().profile() == null)
			return "";

		return response.data().matchedUser().profile().aboutMe();
	}

	private String getTaskDifficulty(String taskSlug) {
		String difficulty = leetCodeTaskCacheRepository.getDifficulty(taskSlug);

		if (difficulty != null)
			return difficulty;

		syncAllTasksToRedis();

		difficulty = leetCodeTaskCacheRepository.getDifficulty(taskSlug);

		return difficulty == null ? "Medium" : difficulty;
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
