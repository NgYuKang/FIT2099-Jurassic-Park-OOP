package game.items;

import edu.monash.fit2099.engine.Actor;
import game.dinosaur.DinosaurStatus;

/**
 * Represents a corpse.
 * Our version of corpse will not rot in inventory. We treat inventory as a portable fridge.
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @author Lin Chen Xiang
 * @version 1.1
 * @see PerishableFoodItem
 * @since 05/05/2021
 */
public class Corpse extends PerishableFoodItem {

    /**
     * "HitPoints" of a Corpse. Determines how much an actor can eat from this Corpse based on its HitPoints left.
     */
    private int CARCASS_HP;

    /**
     * Constructor
     * @param name name of the actor corpse
     * @param rotTime time taken for this corpse to rot
     * @param healAmount the total HP of this corpse
     */
    public Corpse(String name, int rotTime, int healAmount) {
        super(name+" corpse", 'X', rotTime);
        addCapability(ItemStats.CARNIVORE_CAN_EAT);
        addCapability(ItemStats.MULTI_TURN_EATING);
        this.CARCASS_HP = healAmount;
    }

    /**
     * @param actor Target of who we are healing
     * @return How much can we heal based on the target
     */
    @Override
    public int getHealAmount(Actor actor) {
        if (actor.hasCapability(DinosaurStatus.SMALL_BODY)) {
            return 10;
        }
        else {
            return CARCASS_HP;
        }
    }

    /**
     * @return current hitpoints of this Corpse
     */
    @Override
    public int getItemHitPoints() {
        return CARCASS_HP;
    }

    /**
     * @param healAmount amount of "hp" eaten by actor to be decreased in hitpoints for this Corpse
     */
    @Override
    public void decreaseHitPoints(int healAmount) {
        CARCASS_HP -= healAmount;
    }
}
