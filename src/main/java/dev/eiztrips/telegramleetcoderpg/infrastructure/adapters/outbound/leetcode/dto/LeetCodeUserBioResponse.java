package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LeetCodeUserBioResponse(Data data) {
	public record Data(@JsonProperty("matchedUser") MatchedUser matchedUser) {
	}
	public record MatchedUser(@JsonProperty("profile") Profile profile) {
	}
	public record Profile(@JsonProperty("aboutMe") String aboutMe) {
	}
}
