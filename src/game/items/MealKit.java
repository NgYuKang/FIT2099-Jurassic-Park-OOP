package game.items;

import edu.monash.fit2099.engine.Actor;

/**
 * Represents a meal that a dinosaur can eat
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @since 05/05/2021
 * @version 1.0
 */
public abstract class MealKit extends EdibleItem implements Purchasable{

    /**
     * Constructor.
     *
     * @param name        The name of the item
     * @param displayChar What it should show on the map
     */
    public MealKit(String name, char displayChar) {
        super(name, displayChar);
    }

    @Override
    public int getHealAmount(Actor actor) {
        return 99999;
    }

}
