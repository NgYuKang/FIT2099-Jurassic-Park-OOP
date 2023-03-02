package game;

import edu.monash.fit2099.engine.*;
import game.action.BuyItemAction;
import game.dinosaur.Allosaur;
import game.dinosaur.Brachiosaur;
import game.dinosaur.Pterodactyl;
import game.dinosaur.Stegosaur;
import game.items.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Represents a vending machine that you can buy items from.
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @version 1.0
 * @see BuyItemAction
 * @see Fruit
 * @see LaserGun
 * @see Egg
 * @see MealKit
 * @since 05/05/2021
 */
public class VendingMachine extends Ground {

    /**
     * The currency we are dealing with. Static variable, shared across vending machine.
     */
    private static int EcoPoints = 0;

    /**
     * Items that we will sell
     */
    private ArrayList<Item> sellableItem;


    /**
     * Constructor. Insert items into the arraylist here to sell them.
     * When creating an item to sell, make sure to implement Purchasable and its method.
     * Extend Portable/Edible/Weapon/Perishable as deem fit.
     */
    public VendingMachine() {
        super('V');
        sellableItem = new ArrayList<>();
        sellableItem.add(new Fruit());
        sellableItem.add(new LaserGun());
        sellableItem.add(new Egg(new Stegosaur()));
        sellableItem.add(new Egg(new Allosaur()));
        sellableItem.add(new Egg(new Brachiosaur()));
        sellableItem.add(new Egg(new Pterodactyl()));
        sellableItem.add(new HerbivoreMealKit());
        sellableItem.add(new CarnivoreMealKit());
    }

    /**
     * Vending machine is tall. Blocky. No Enter.
     *
     * @param actor the Actor to check
     * @return WE CANT ENTER! False.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Override this to implement terrain that blocks thrown objects but not movement, or vice versa
     * Vending machine Tall. Block things.
     *
     * @return true
     */
    @Override
    public boolean blocksThrownObjects() {
        return true;
    }

    /**
     * Creates an BuyItemAction from the inputs.
     *
     * @param item  Item to put on sale
     * @param price Price of the item
     * @return The action to buy said item
     */
    private Action returnBuyItemAction(Item item, int price) {
        Item freshItem;
        Action action = null;
        if (item instanceof Egg) {
            freshItem = ((Egg) item).getNewCopy();
            action = new BuyItemAction(freshItem, price);
        } else {
            try {
                Class<?> cls = item.getClass();
                Constructor<?> constructor;
                constructor = cls.getConstructor();
                freshItem = (Item) constructor.newInstance();
                action = new BuyItemAction(freshItem, price);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return action;
    }

    /**
     * Returns Actions that others can do to this.
     * Dinosaurs don't use Actions, so we are not doing a check to give it only
     * to players.
     * Will only show items that we can buy with the current points.
     *
     * @param actor     the Actor acting
     * @param location  the current Location
     * @param direction the direction of the Ground from the Actor
     * @return a new, empty collection of Actions
     */
    @Override
    public Actions allowableActions(Actor actor, Location location, String direction) {
        Actions actions = super.allowableActions(actor, location, direction);
        for (Item item : sellableItem) {
            Purchasable itemPurchase = (Purchasable) (item);
            int price = itemPurchase.getPrice();
            if (getEcoPoint() >= price) {
                actions.add(returnBuyItemAction(item, price));
            }
        }

        return actions;
    }

    /**
     * @param num How much to increase the ecopoints
     */
    public static void increaseEcoPoint(int num) {
        EcoPoints += num;
    }

    /**
     * @return Current amount of ecopoints
     */
    public static int getEcoPoint() {
        return EcoPoints;
    }

    /**
     * @param num How much to decrease ecopoints
     */
    public static void decreaseEcoPoint(int num) {
        EcoPoints -= num;
    }


}
