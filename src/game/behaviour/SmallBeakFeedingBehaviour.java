package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.EatItemAction;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.items.EdibleItem;

/**
 * Special Behaviour class that allows small dinosaurs to keep eating the same item for multiple turns
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see EdibleItem
 * @see DinosaurStatus
 * @see Location
 * @see GameMap
 * @since 20/05/2021
 */

public class SmallBeakFeedingBehaviour implements Behaviour{

    /**
     * The item to be eaten for multiple turns
     */
    private Item item;

    /**
     * Used to override getNextAction to action chaining
     */
    private Behaviour self = this;

    /**
     * Constructor
     * @param item the item to be eaten for multiple turns
     */
    public SmallBeakFeedingBehaviour(Item item) {
        this.item = item;
    }

    /**
     * Continuously eat from the same Item until actor is no longer hungry
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return null if actor or item missing, action to eat if both are still on map
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        // flying dinosaur must land to eat
        if (actor.hasCapability(DinosaurStatus.FLYING)) {
            actor.removeCapability(DinosaurStatus.FLYING);
            actor.addCapability(DinosaurStatus.ON_LAND);
        }

        // if actor not missing and is still hungry
        if (map.contains(actor) && ((Dinosaur) actor).isHungry()) {
            Location here = map.locationOf(actor);
            // check if item still present
            for (Item item : here.getItems()) {
                if (item == this.item) {
                    return (new EatItemAction(item) {
                        @Override
                        public Action getNextAction() { // override next action as current action to keep eating this item
                            return self.getAction(actor, map);
                        }
                    });
                }
            }
        }
        // actor or item missing, or no longer hungry
        return null;
    }
}
