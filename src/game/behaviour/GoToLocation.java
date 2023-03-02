package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.dinosaur.DinosaurStatus;
import game.growable.Growable;
import game.items.ItemStats;

/**
 * A class that figures out a MoveAction that will move the actor one step
 * closer to a Location with target Growable/Item, or does something when the actor reaches the Location
 *
 * @author Lin Chen Xiang
 * @author NgYuKang
 * @see Behaviour
 * @see Actor
 * @see Growable
 * @see Item
 * @see Action
 * @see GameMap
 * @see Location
 * @since 16/05/2021
 * @version 1.1
 */

public class GoToLocation extends MovingBehaviour {

    /**
     * The place we are going
     */
    private Location there = null;
    /**
     * The target we might eat
     */
    private Growable growable = null;
    /**
     * The target we might eat
     */
    private Item item = null;
    /**
     * What we do to the target
     */
    private Action action = null;
    /**
     * Used in overriding getNextAction
     */
    private Behaviour self = this;

    /**
     * Constructor that takes in Growable
     *
     * @param there    the Location of the Growable
     * @param growable the Growable to be reached
     * @param action   the Action to be done when reaching there
     */
    public GoToLocation(Location there, Growable growable, Action action) {
        this.there = there;
        this.growable = growable;
        this.action = action;
    }

    /**
     * Consturctor that takes in only a location.
     *
     * @param there Place to go
     * @param action What to do afterwards
     */
    public GoToLocation(Location there, Action action){
        this.there = there;
        this.action = action;
    }

    /**
     * Constructor that takes in Item
     *
     * @param there  the Location of the Item
     * @param item   the Item to be reached
     * @param action the Action to be done when reaching there
     */
    public GoToLocation(Location there, Item item, Action action) {
        this.there = there;
        this.item = item;
        this.action = action;
    }

    /**
     * Decides the current Action for this Actor
     *
     * @param actor the Actor acting
     * @param map   the GameMap containing the Actor
     * @return MoveActorAction if Actor has not reached target, or action if Actor reached target
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        if (map.contains(actor)) {
            boolean flag = false;
            // check if growable is set in constructor, if it is null means item is set
            if (growable != null) {
                // check if growable still exists at that location, if so check if it still has any fruit on it
                if (there.getGround() != growable || growable.getNumberOfRipeFruit() < 1) {
                    return null; // no such growable/no more fruit, reset behaviour
                } else {
                    flag = true;
                }
            } else if (item != null) {
                // check if item still exists at that location
                for (Item item : there.getItems()) {
                    if (item == this.item) {
//                    if (item.getClass() == this.item.getClass()) // check if such an item still exists
//                        this.item = item;
                        flag = true;
                        break;
                    }
                }
            } else {
                // If we are only going to a location, no need for check
                flag = true;
            }
            if (!flag) { // item doesn't exist, reset behaviour
                return null;
            }
        } else { // actor missing, can't do anything
            return null;
        }

        Location here = map.locationOf(actor);

        // actor is at goal
        if (here.x() == there.x() && here.y() == there.y()) {

            if (item != null && item.hasCapability(ItemStats.MULTI_TURN_EATING)) {

                if (actor.hasCapability(DinosaurStatus.FLYING)) {
                    for (Exit exit : here.getExits()) {
                        // there is something around the goal, flying dinosaur cannot land
                        if (exit.getDestination().containsAnActor()) {
                            return (new DoNothingAction() {
                                @Override
                                public Action getNextAction() { // override next action as current action to hover on the item
                                    return self.getAction(actor, map);
                                }
                            });
                        }
                    }
                }

            }
            return action; // actor is already at target, do action
        }

        int currentDistance = distance(here, there); // no change
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = distance(destination, there);
                // actor hasn't reached its goal yet, keep going
                if (newDistance < currentDistance) {
                    return (new MoveActorAction(destination, exit.getName()) {
                        @Override
                        public Action getNextAction() { // override next action as current action to chain follow
                            return self.getAction(actor, map);
                        }
                    });
                }
            }
        }
        return null;
    }
}


