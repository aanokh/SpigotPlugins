package online.allcraft.zombies;

import java.util.HashMap;

public class RoundData {
	public int enemyAmount;
	public HashMap<EnemyType, Float> enemyProbabilities;
	
	
	public RoundData(int enemyAmount, HashMap<EnemyType, Float> enemyProbabilities) {
		this.enemyAmount = enemyAmount;
		this.enemyProbabilities = enemyProbabilities;
	}
}
