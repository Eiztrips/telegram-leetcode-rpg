package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

public class TelegramPrivateMessagePresenter {
	private TelegramPrivateMessagePresenter() {
	}

	public static String formatRegisterInfo(String token) {
		return """
				<b>Ваш токен регистрации:</b> %n<code>%s</code> %n%n
				<b>Введите его в описание своего профиля на leetcode (readme): </b>%s %n%n
				<b>После смены описания обязательно пришлите /verify!</b>
				""".formatted(token, "https://leetcode.com/settings/profile");
	}
}
