package game.items;

import edu.monash.fit2099.engine.Actor;
import game.PortableItem;


/**
 * Represents a food item.
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @author Lin Chen Xiang
 * @version 1.1
 * @see PortableItem
 * @since 05/05/2021
 */
public abstract class EdibleItem extends PortableItem {

    /**
     * Default hp for all edible items set to 1
     */
    private int hitPoints = 1;

    /**
     * Constructor.
     *
     * @param name        The name of the item
     * @param displayChar What it should show on the map
     */
    public EdibleItem(String name, char displayChar) {
        super(name, displayChar);
        addCapability(ItemStats.IS_EDIBLE);
    }

    /**
     * Returns how much this item can heal the target depending on its stats.
     * Enter null if checking player feeding dinosaur.
     * Some item will return the same amount regardless of actor since it is a fixed int.
     *
     * @param actor Target
     * @return heal amount
     */
    public abstract int getHealAmount(Actor actor);

    /**
     * @return current "hp" of this item
     */
    public int getItemHitPoints() {
        return hitPoints;
    }

    /**
     * @param healAmount amount of "hp" eaten by actor to be decreased in hitpoints
     */
    public void decreaseHitPoints(int healAmount) {
        hitPoints -= healAmount;
    }

}
