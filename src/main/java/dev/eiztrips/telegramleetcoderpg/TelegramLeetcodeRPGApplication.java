package dev.eiztrips.telegramleetcoderpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TelegramLeetcodeRPGApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramLeetcodeRPGApplication.class, args);
	}

}
