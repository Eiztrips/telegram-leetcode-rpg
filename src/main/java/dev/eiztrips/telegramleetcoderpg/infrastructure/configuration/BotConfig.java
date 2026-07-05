package dev.eiztrips.telegramleetcoderpg.infrastructure.configuration;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat.GroupChatHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.PrivateChatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfig {

	@Bean
	public List<CommandHandler> privateCommandHandlers(List<CommandHandler> allHandlers) {
		return allHandlers.stream().filter(PrivateChatHandler.class::isInstance).toList();
	}

	@Bean
	public List<CommandHandler> groupCommandHandlers(List<CommandHandler> allHandlers) {
		return allHandlers.stream().filter(GroupChatHandler.class::isInstance).toList();
	}
}
