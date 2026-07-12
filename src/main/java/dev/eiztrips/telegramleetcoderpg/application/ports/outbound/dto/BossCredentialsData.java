package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto;

import lombok.Builder;

/**
 * DTO рандомных данных для генерации босса
 *
 * @param adjective
 *            прилагательное
 * @param name
 *            имя босса
 * @param hp
 *            жизнь
 */
public record BossCredentialsData(String adjective, String name, int hp) {
	/**
	 * @param adjective
	 *            прилагательное
	 * @param name
	 *            имя босса
	 * @param hp
	 *            жизнь
	 */
	@Builder
	public BossCredentialsData {
	}
}
