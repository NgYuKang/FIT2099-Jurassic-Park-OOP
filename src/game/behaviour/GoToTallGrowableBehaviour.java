package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.dinosaur.DinosaurStatus;
import game.growable.GrowableStatus;

/**
 * Behaviour for Flying Dinosaurs which have no objective and are on the ground, which will then find a
 * Tall Growable to perch on.
 *
 * @author Lin Chen Xiang
 * @see GoToLocation
 * @see game.growable.Tree
 * @see Location
 * @since 20/05/2021
 * @version 1.0
 */

public class GoToTallGrowableBehaviour extends MovingBehaviour implements Behaviour {

    /**
     * Optional action to do when reached Growable, default set to null
     */
    private Action action = null;

    /**
     * Empty Constructor
     */
    public GoToTallGrowableBehaviour() {}

    /**
     * Constructor if there is Action to be done when reached Growable
     * @param action the action to do when at Growable
     */
    public GoToTallGrowableBehaviour(Action action) {
        this.action = action;
    }

    /**
     * Scans the entire map to find the closest Tall Growable to go to, if actor is on land
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return null if no such Growable found, an Action to from GoToLocation if found nearest growable
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        // if actor is still flying and has no objective, don't use this behaviour
        if (actor.hasCapability(DinosaurStatus.FLYING)) {
            if (action == null) {
                return null;
            }
        }
        Location here = map.locationOf(actor);
        Location there;
        Location currentClosest = here;  // set current location of actor as default
        int distance;
        int closestDistance = Integer.MAX_VALUE;
        // start scanning map
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                there = map.at(x, y);
                // found Tall Growable
                if (there.getGround().hasCapability(GrowableStatus.TALL)) {
                    distance = distance(here, there);
                    // nearer than previously found Growable, set this as current closest Location
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        currentClosest = there;
                    }
                }
            }
        }
        if (closestDistance < Integer.MAX_VALUE) {
            // actor is just moving to that Location, doesn't do anything else when reaching there
            Behaviour startMoving = new GoToLocation(currentClosest, action);
            // start moving towards Location
            return startMoving.getAction(actor, map);
        }

        // actor is still flying, or did not find any of such Growable
        return null;
    }
}
