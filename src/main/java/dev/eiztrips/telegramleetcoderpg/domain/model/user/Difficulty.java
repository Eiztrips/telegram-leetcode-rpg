package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Difficulty {

	EASY(20), MEDIUM(50), HARD(100);

	private final int reward;
}
