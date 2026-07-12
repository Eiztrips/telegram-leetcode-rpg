package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.eiztrips;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.BossCredentialsData;

/**
 * Порт клиента: Eiztrips API.
 */
public interface EiztripsClientPort {
	/**
	 * Получить случайные данные для генерации босса
	 * 
	 * @return данные
	 */
	BossCredentialsData fetchRandomBossCredentials();
}
