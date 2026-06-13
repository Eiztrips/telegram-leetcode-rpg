package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild;

/**
 * Сценарий обновления еженедельного босса гильдии.
 */
public interface RespawnWeeklyBossUseCase {
	/**
	 * обновить еженедельного босса
	 *
	 * @param name
	 *            имя босса
	 * @param hp
	 *            здоровье босса
	 * @param guildId
	 *            id гильдии
	 */
	void respawnWeeklyBoss(String name, int hp, Long guildId);
}
