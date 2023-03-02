package game.items;

import edu.monash.fit2099.engine.Location;

/**
 * Represents a food that can rot.
 *
 * @author NgYuKang
 * @version 1.0
 * @see EdibleItem
 * @since 05/05/2021
 */
public abstract class PerishableFoodItem extends EdibleItem {

    /**
     * Used to represent freshness of the item.
     */
    private int rotTimer;

    /**
     * Used to represent how long it takes for it to rot.
     */
    private final int rotTime;

    /**
     * Constructor.
     *
     * @param name        The name of the item
     * @param displayChar What it should show on the map
     */
    public PerishableFoodItem(String name, char displayChar, int timeTakenRot) {
        super(name, displayChar);
        rotTimer = 0;
        rotTime = timeTakenRot;
    }

    /**
     * Inform an Item on the ground of the passage of time.
     * This method is called once per turn, if the item rests upon the ground.
     * Checks the rotting progress of the food.
     *
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        rotTimer++;
        checkRot(currentLocation);
    }

    /**
     * Checks if the food has reached its time.
     *
     * @param location Location the item is in.
     */
    private void checkRot(Location location) {
        if (rotTimer >= rotTime) {
            location.removeItem(this);
        }
    }

}
