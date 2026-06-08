package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LeetCodeSubmissionResponse(Data data) {

	public record Data(@JsonProperty("recentAcSubmissionList") List<GraphQlSubmission> recentAcSubmissionList) {
	}

	public record GraphQlSubmission(@JsonProperty("id") String id, @JsonProperty("title") String title,
			@JsonProperty("titleSlug") String titleSlug, @JsonProperty("timestamp") String timestamp) {
	}
}
