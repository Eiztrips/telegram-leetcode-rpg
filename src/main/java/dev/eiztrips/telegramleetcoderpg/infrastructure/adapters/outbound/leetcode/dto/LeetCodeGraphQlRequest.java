package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode.dto;

public record LeetCodeGraphQlRequest(String query, Variables variables) {
	public record Variables(String username, int limit) {
	}
}
