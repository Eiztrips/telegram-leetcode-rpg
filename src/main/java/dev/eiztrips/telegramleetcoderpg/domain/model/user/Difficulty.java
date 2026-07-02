package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import lombok.Getter;

@Getter
public enum Difficulty {

	EASY(20), MEDIUM(50), HARD(100);

	private final int reward;

	Difficulty(int reward) {
		this.reward = reward;
	}
}
