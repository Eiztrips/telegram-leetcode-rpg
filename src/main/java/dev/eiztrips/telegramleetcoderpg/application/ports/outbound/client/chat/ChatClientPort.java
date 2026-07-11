package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.chat;

/**
 * Порт клиента. (клиент - чат бот в мессенжере)
 */
public interface ChatClientPort {
	/**
	 * Проверить существование чата
	 *
	 * @param chatId
	 *            id чата
	 */
	boolean chatExists(Long chatId);
}
