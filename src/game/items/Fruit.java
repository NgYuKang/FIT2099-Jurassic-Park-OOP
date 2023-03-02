package game.items;

import edu.monash.fit2099.engine.Actor;
import game.dinosaur.DinosaurStatus;

/**
 * Represents a Fruit item.
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @version 1.0
 * @see PerishableFoodItem
 * @see Purchasable
 * @since 05/05/2021
 */
public class Fruit extends PerishableFoodItem implements Purchasable {
    public Fruit() {
        super("Fruit", 'a', 15);
        addCapability(ItemStats.HERBIVORE_CAN_EAT);
    }

    /**
     * Returns how much this item can heal the target depending on its stats.
     * Enter null if checking player feeding dinosaur.
     * Some item will return the same amount regardless of actor since it is a fixed int.
     *
     * @param actor Target
     * @return heal amount
     */
    @Override
    public int getHealAmount(Actor actor) {
        int ret;
        if (actor == null) {
            ret = 20;
        } else if (actor.hasCapability(DinosaurStatus.BAD_DIGESTION)) {
            ret = 5;
        } else {
            ret = 10;
        }
        return ret;
    }

    /**
     * @return Price of the item
     */
    @Override
    public int getPrice() {
        return 30;
    }

}
