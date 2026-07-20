package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

public class TelegramPrivateMessagePresenter {
	private TelegramPrivateMessagePresenter() {
	}

	public static String formatRegisterInfo(String token) {
		return """
				🔐 <b>Регистрация</b>
				<blockquote>
				Шаг 1: скопируйте ваш токен:
				<code>%s</code>

				Шаг 2: вставьте его в описание профиля:
				<a href="%s">leetcode.com/settings/profile</a>

				Шаг 3: завершите регистрацию командой /verify
				</blockquote>
				""".formatted(token, "https://leetcode.com/settings/profile");
	}
}
