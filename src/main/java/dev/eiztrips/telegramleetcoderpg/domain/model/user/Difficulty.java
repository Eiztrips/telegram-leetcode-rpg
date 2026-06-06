package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import lombok.Getter;

/**
 * Уровень сложности задачи.
 */
@Getter
public enum Difficulty {
	/**
	 * Легкая сложность.
	 */
	EASY(10),
	/**
	 * Средняя сложность.
	 */
	MEDIUM(20),
	/**
	 * Высокая сложность.
	 */
	HARD(30);

	private final int reward;

	Difficulty(int reward) {
		this.reward = reward;
	}
}
