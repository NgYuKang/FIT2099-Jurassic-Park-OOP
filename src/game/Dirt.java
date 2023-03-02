package game;

import edu.monash.fit2099.engine.Ground;
import game.growable.GrowableStatus;

/**
 * A class that represents bare dirt.
 */
public class Dirt extends Ground {

	/**
	 * Constructor. Adds an enum.
	 */
	public Dirt() {
		super('.');
		addCapability(GrowableStatus.DIRT);
	}
}
