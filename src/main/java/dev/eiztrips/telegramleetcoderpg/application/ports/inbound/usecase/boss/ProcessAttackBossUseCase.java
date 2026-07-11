package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.boss;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.ProcessAttackBossResult;

/**
 * Сценарий использования: обработка атаки на клиенте.
 */
public interface ProcessAttackBossUseCase {
	/**
	 * Атака босса на клиенте.
	 * 
	 * @param userId
	 *            id пользователя телеграм
	 * @return результат атаки.
	 */
	ProcessAttackBossResult processAttackBoss(Long userId);
}
