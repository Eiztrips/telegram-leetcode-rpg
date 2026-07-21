package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import lombok.Getter;

/**
 * Ранги пользователей по XP.
 */
@Getter
public enum Rank {

	SCRIPT_KIDDIE("Микро-скриптик", 0), INTERN_BUG_HUNTER("Стажер баг хантер", 300), JUNIOR_RESOLVER("Почти джун",
			1_000), MIDDLE_ALGO_CRAFTER("Полнейший мидл", 3_000), SENIOR_CODE_ARCHITECT("Сеньорище",
					8_000), STAFF_LEETCODER("Элитный литкодер",
							20_000), PRINCIPAL_REFACTORER("Лидер", 50_000), O1_GOD("O(1)", 100_000);

	private final String title;
	private final int requiredXp;

	Rank(String title, int requiredXp) {
		this.title = title;
		this.requiredXp = requiredXp;
	}

	public static Rank fromXp(int xp) {
		Rank current = SCRIPT_KIDDIE;
		for (Rank rank : values()) {
			if (xp >= rank.requiredXp) {
				current = rank;
			} else {
				break;
			}
		}
		return current;
	}
}
