package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.EatItemAction;
import game.action.EatPreyAction;
import game.dinosaur.*;
import game.items.Corpse;

/**
 * A class that decides whether the Actor can attack a nearby prey
 *
 * @author Lin Chen Xiang
 * @see Behaviour
 * @see Actor
 * @see EatPreyAction
 * @see Allosaur
 * @see game.dinosaur.Stegosaur
 * @see GameMap
 * @see Location
 * @since 03/05/2021
 */

public class PredatorBehaviour implements Behaviour {

    /**
     * Empty Constructor
     */
    public PredatorBehaviour() {
    }

    /**
     * Checks all exits of this actor to see if there are prey or corpse to be eaten
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return null if no prey nor corpse found, new EatPreyAction if there are prey that can be attacked
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        // not Allosaur, shouldn't have this behaviour
        if (!(actor instanceof Allosaur)) {
            return null;
        }

        Location here = map.locationOf(actor);

        // find surrounding prey
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();

            // if this exit contains an Actor that can be attacked by Allosaur
            if (destination.containsAnActor()
                    && destination.getActor().hasCapability(DinosaurStatus.ALLOSAUR_CAN_ATTACK)
                    && destination.getActor().hasCapability(DinosaurStatus.ON_LAND)) {

                // false if Allosaur already attacked this Dinosaur within 20 turns, true otherwise
                boolean canAttack = ((CarnivoreDinosaur)actor).canAttack((Dinosaur) destination.getActor());
                // if canAttack is true means not attacked before
                if (canAttack) {
                    return new EatPreyAction(destination.getActor());
                }
            }
            else { // no prey at this exit, check for corpse
                // access all items on this location
                for (Item item : destination.getItems()) {
                    // found corpse, immediately eat
                    if (item instanceof Corpse) {
                        return new EatItemAction(item);
                    }
                }
            }
        }

        // no prey found
        return null;

    }
}
