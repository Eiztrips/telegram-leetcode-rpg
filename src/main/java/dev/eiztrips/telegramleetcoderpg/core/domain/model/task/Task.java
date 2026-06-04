package dev.eiztrips.telegramleetcoderpg.core.domain.model.task;

/**
 * Модель LeetCode задачи
 */
public record Task(String title, Difficulty difficulty) {
	/**
	 * Расчет награждения за задачу.
	 *
	 * @return получаемый опыт
	 */
	public int reward() {
		return switch (difficulty) {
			case EASY -> 10;
			case MEDIUM -> 20;
			case HARD -> 30;
		};
	}
}
