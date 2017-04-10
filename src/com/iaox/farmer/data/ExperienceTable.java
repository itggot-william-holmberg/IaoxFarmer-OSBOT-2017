package com.iaox.farmer.data;
import java.util.Arrays;

/**
 * @author 		Bjorn Krols (Botre)
 * @version		0.1
 * @since		March 6, 2015
 */

public final class ExperienceTable {
	/**
	 * The maximum skill level in OSRS.
	 */
	private static final int MAXIMUM_LEVEL = 99;
	
	/**
	 * A table holding the experience amounts for a specific number of levels.
	 */
	private static final int[] TABLE = create(MAXIMUM_LEVEL);

	private ExperienceTable() { 
		// This class should never be instantiated.
		// Do not delete or make accessible.
	}
	
	/**
	 * @return The total amount of experience required for the level.
	 */
	public static int getXp(int level) {
		return level == 0 ? 0 : TABLE[level - 1];
	}

	/**
	 * @return The level acquired by the amount of experience.
	 */
	public static int getLevel(int experience) {
		return Math.abs(Arrays.binarySearch(TABLE, experience) + 1);
	}

	/**
	 * Creates the experience table.
	 */
	private static int[] create(int maximumLevel) {
		int[] table = new int[maximumLevel];
		int experience = 0;
		for (int i = 1; i < maximumLevel; i++) {
			experience += Math.floor(i + 300 * Math.pow(2, i / 7.0));
			table[i] = (int) Math.floor(experience / 4);
		}
		return table;
	}

}