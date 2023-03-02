package game.action;

import edu.monash.fit2099.engine.*;
import game.VendingMachine;
import game.items.EdibleItem;
import game.items.Fruit;
import game.items.ItemStats;

/**
 * Action that allows Player to feed an Item to a Dinosaur and heal it
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see game.Player
 * @see game.dinosaur.Dinosaur
 * @see Item
 * @see game.VendingMachine
 * @see GameMap
 * @since 05/05/2021
 */

public class FeedAction extends Action {

    /**
     * the actor to be fed
     */
    private Actor target;

    /**
     * the item being fed to target
     */
    private Item item;

    /**
     * Constructor
     * @param target the actor to be fed
     * @param item the item being fed to target
     */
    public FeedAction(Actor target, Item item) {
        this.target = target;
        this.item = item;
    }

    /**
     * Heals target based on healing of item and removes the item from player inventory
     * @param actor Player who is feeding the target
     * @param map The map the actor is on.
     * @return description of process of feeding the target
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        if (!(item.hasCapability(ItemStats.IS_EDIBLE))) {
            return actor + " cannot feed with this " + item;
        }

        String result = actor + " feeds " + target + " with " + item;
        int healPoints = ((EdibleItem)item).getHealAmount(null); // for Fruit, feeding is always +20
        target.heal(healPoints);
        result += System.lineSeparator() + target + " heals for " + healPoints + " hitpoints.";

        // add EcoPoints if item being fed is a fruit
        if (item instanceof Fruit) {
            VendingMachine.increaseEcoPoint(10);
            result += System.lineSeparator() + actor + " gains 10 EcoPoints.";
        }

        result += System.lineSeparator() + actor + " loses one " + item + ".";
        actor.removeItemFromInventory(item);

        return result;
    }

    /**
     *
     * @param actor The actor performing the action.
     * @return description of player feeding target with this item
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " feeds " + target + " with " + item;
    }
}
