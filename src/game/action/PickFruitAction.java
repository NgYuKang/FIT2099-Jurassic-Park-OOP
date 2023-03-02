package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.VendingMachine;
import game.growable.Growable;
import game.items.Fruit;

/**
 * Action that allows Player to attempt to pick a Fruit from a Growable.
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see Action
 * @see game.Player
 * @see Growable
 * @see Fruit
 * @see game.VendingMachine
 * @see GameMap
 * @since 05/05/2021
 */

public class PickFruitAction extends Action {

    /**
     * the growable that the player is picking fruits from
     */
    private Growable growable;

    /**
     * Constructor
     * @param growable the growable that the player is picking fruits from
     */
    public PickFruitAction(Growable growable){
        this.growable = growable;
    }

    /**
     * Adds one Fruit into Player's inventory and decrement fruit count of growable if Player succeeds
     * @param actor Player picking fruit
     * @param map The map the actor is on.
     * @return description of process of player picking fruit
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        if (failed()) {
            return actor + " searches the " + growable + " for fruit, but can't find any ripe ones.";
        }

        actor.addItemToInventory(new Fruit());
        growable.decrementNumberOfRipeFruit();
        VendingMachine.increaseEcoPoint(10);

        return actor + " gains one fruit and 10 EcoPoints.";
    }

    /**
     *
     * @param actor Player
     * @return description of player picking fruit from growable
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " picks fruit from " + growable;
    }

    /**
     * Player has a 60% chance of failing
     * @return 0.6 which is the chance of failing to find fruit
     */
    public double failChance() {return 0.6;}

    /**
     * See whether Player failed to pick fruit
     * @return true Player failed, false otherwise
     */
    public boolean failed() {
        double chance = Math.random();
        return chance < failChance();
    }
}
