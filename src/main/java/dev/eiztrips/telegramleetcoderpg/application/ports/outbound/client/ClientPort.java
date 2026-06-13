package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client;

/**
 * Порт клиента. (клиент - чат бот в мессенжере)
 */
public interface ClientPort {
	/**
	 * Проверить существование чата
	 *
	 * @param chatId
	 *            id чата
	 */
	boolean chatExists(Long chatId);
}
