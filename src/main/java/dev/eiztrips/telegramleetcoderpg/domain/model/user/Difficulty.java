package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import lombok.Getter;

@Getter
public enum Difficulty {

	EASY(10), MEDIUM(20), HARD(30);

	private final int reward;

	Difficulty(int reward) {
		this.reward = reward;
	}
}
