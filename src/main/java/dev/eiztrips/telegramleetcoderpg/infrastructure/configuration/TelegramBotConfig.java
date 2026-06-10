package dev.eiztrips.telegramleetcoderpg.infrastructure.configuration;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.TelegramBotAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

	@Bean
	public TelegramBotsApi telegramBotsApi(TelegramBotAdapter telegramBotAdapter) throws TelegramApiException {
		TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(telegramBotAdapter);
		return api;
	}
}
