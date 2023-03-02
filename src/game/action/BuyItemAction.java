package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import game.VendingMachine;

/**
 * Represents an action to buy an item.
 * The check on whether we can buy it is done in vending machine.
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @version 1.0
 * @see game.items.Purchasable
 * @see VendingMachine
 * @since 05/05/2021
 **/
public class BuyItemAction extends Action {

    /**
     * Item we are selling / Item that the player is buying
     */
    private Item itemToSell;
    /**
     * How much is this being sold for.
     */
    private int sellingPrice;

    /**
     * Constructor.
     *
     * @param item Item we are selling / Item that the player is buying
     * @param price Price to sell the item for
     */
    public BuyItemAction(Item item, int price) {
        itemToSell = item;
        sellingPrice = price;
    }


    /**
     * Executes the action. Adds item to the actor (Player's inventory)
     * and deduct money.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return Result.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // add to inv (item)
        // minus EcoPoints (price)
        // returns message that player bought something
        actor.addItemToInventory(itemToSell);
        VendingMachine.decreaseEcoPoint(sellingPrice);
        return String.format("%s bought %s for %s EcoPoints", actor.toString(), itemToSell.toString(), sellingPrice);
    }

    /**
     * Describes what are we going
     *
     * @param actor The actor performing the action.
     * @return The description
     */
    @Override
    public String menuDescription(Actor actor) {
        // sysmenu player buys smth
        return String.format("%s buys %s for %s EcoPoints", actor.toString(), itemToSell.toString(), sellingPrice);
    }
}