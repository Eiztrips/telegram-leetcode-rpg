package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto;

import java.util.List;

public record LeetCodeAllQuestionsResponse(Data data) {
	public record Data(List<Question> allQuestions) {
	}

	public record Question(String titleSlug, String difficulty) {
	}
}
