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
		return difficulty.getReward();
	}
}
