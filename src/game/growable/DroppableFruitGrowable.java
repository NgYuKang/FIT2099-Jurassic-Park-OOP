package game.growable;

import edu.monash.fit2099.engine.Location;
import game.items.Fruit;

/**
 * Represents a growable that has a chance for its fruit to drop.
 *
 * @author NgYuKang
 * @version 1.0
 * @see Growable
 * @since 25/04/2021
 */
public abstract class DroppableFruitGrowable extends Growable {

    /**
     * Constructor.
     *
     * @param displayChar How to display this on the map
     */
    public DroppableFruitGrowable(char displayChar) {
        super(displayChar);
    }

    /**
     * @return Chance that a fruit will drop from a tree every turn.
     */
    public abstract double dropFruitChance();

    /**
     * Checks each fruit the tree has if it would drop.
     */
    private void canDropFruit(Location location) {
        // RNGesus
        int temp = getNumberOfRipeFruit(); // use a temp variable since it might change
        for (int i = 0; i < temp; i++) {
            double chance = Math.random();
            if (chance < dropFruitChance()) {
                decrementNumberOfRipeFruit();
                location.addItem(new Fruit());
            }
        }

    }

    /**
     * And now the tree will grow older.
     * Informs the tree the passage of time so that it will run its method every turn.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        super.tick(location);
        canDropFruit(location);
    }
}
