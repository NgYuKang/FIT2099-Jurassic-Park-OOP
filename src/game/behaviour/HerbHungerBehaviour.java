package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.EatFromGrowableAction;
import game.action.EatItemAction;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.growable.Growable;
import game.growable.GrowableStatus;
import game.items.ItemStats;

/**
 * Class that decides whether a herbivore is hungry and finds food if so
 *
 * @author Lin Chen Xiang
 * @see Behaviour
 * @see Actor
 * @see Action
 * @see Dinosaur
 * @see game.dinosaur.HerbivoreDinosaur
 * @see Growable
 * @see Action
 * @see Location
 * @see HungerBehaviour
 * @see DinosaurStatus
 * @see GrowableStatus
 * @see ItemStats
 * @see EatFromGrowableAction
 * @see EatItemAction
 * @see GoToLocation
 * @see GameMap
 * @since 03/05/2021
 */

public class HerbHungerBehaviour implements HungerBehaviour{

    /**
     * Empty Constructor
     */
    public HerbHungerBehaviour(){}

    /**
     * Checks all exits whether there is food and eat it immediately if so,
     * Otherwise scan through entire map and find the closest food source and start moving towards it.
     *
     * @param actor the Actor acting
     * @param map   the GameMap containing the Actor
     * @return null if no food source found, or Action to be done if found food
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        // check if it is a herbivore and is hungry
        if (actor.hasCapability(DinosaurStatus.TEAM_HERBIVORE)  && ((Dinosaur) actor).isHungry()) {

            Enum<GrowableStatus> capability=null;

            // Dinosaur with SHORT NECK eats from SHORT growable, LONG NECK eats from TALL growable
            if (actor.hasCapability(DinosaurStatus.SHORT_NECK)) {
                capability = GrowableStatus.SHORT;
            }

            else if (actor.hasCapability(DinosaurStatus.LONG_NECK)) {
                capability = GrowableStatus.TALL;
            }

            Location here = map.locationOf(actor);

            // check surroundings if there is food source
            for (Exit exit : here.getExits()) {
                Location destination = exit.getDestination();
                // found food source from growable, immediately feed from that growable
                if (destination.getGround().hasCapability(capability)) {
                    if (((Growable)destination.getGround()).getNumberOfRipeFruit() > 0) {
                        return new EatFromGrowableAction((Growable) destination.getGround());
                    }

                }
                else if (actor.hasCapability(DinosaurStatus.SHORT_NECK)) { // Dinosaur with SHORT NECK can Item from ground
                    // access all items on this location
                    for (Item item : destination.getItems()) {
                        // found fruit, immediately eat
                        if (item.hasCapability(ItemStats.HERBIVORE_CAN_EAT)) {
                            return new EatItemAction(item);
                        }
                    }
                }
            }

            // no prey/food found in surroundings, start finding nearest food source in map
            return findFood(here, actor, map);
        }

        return null; // not herbivore or not hungry
    }


    /**
     * Finds the closest growable or item that this dinosaur can eat from and start going towards it.
     *
     * @param here     location of this actor
     * @param dinosaur the actor looking for food
     * @param map      World map
     * @return null if no food found, or Action to start moving towards food
     */
    @Override
    public Action findFood(Location here, Actor dinosaur, GameMap map) {

        Behaviour startMoving; // GoToLocation
        Location there; // targeted location
        Location currentClosest = here; // default set to location of dinosaur
        Growable closestGrowable = null; // closest found food source
        Item closestItem = null; // closest found food
        int distance; // calculated distance between here and there
        int[] closestDist = {Integer.MAX_VALUE, -1};
        // index 0 represents: the calculated distance
        // index 1 represents: -1 -> no food source
        //                      0 -> found Growable to go to
        //                      1 -> found Item to go to

        Enum<GrowableStatus> capability=null;
        // Dinosaur with SHORT NECK eats from SHORT growable, LONG NECK eats from TALL growable
        if (dinosaur.hasCapability(DinosaurStatus.SHORT_NECK)) {
            capability = GrowableStatus.SHORT;
        }

        else if (dinosaur.hasCapability(DinosaurStatus.LONG_NECK)) {
            capability = GrowableStatus.TALL;
        }

        // go through entire map
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                // current tile
                there = map.at(x, y);
                // if found potential food source
                if (there.getGround().hasCapability(capability)) {
                    // check if this growable has any fruit on it
                    if (((Growable)there.getGround()).getNumberOfRipeFruit() > 0) {
                        distance = distance(here, there);
                        // if this growable is nearer than current nearest goal, overwrite nearest goal to this goal
                        if (distance < closestDist[0]) {
                            closestDist[0] = distance;
                            closestDist[1] = 0;
                            currentClosest = there;
                            closestGrowable = ((Growable)there.getGround());
                        }
                    }
                }
                else if (dinosaur.hasCapability(DinosaurStatus.SHORT_NECK)){ // Dinosaur with SHORT NECK can eat Item from ground
                    for (Item item : there.getItems()) {
                        if (item.hasCapability(ItemStats.HERBIVORE_CAN_EAT)) {
                            distance = distance(here, there);
                            // if this food Item is nearer than current nearest goal, overwrite nearest target to this
                            if (distance < closestDist[0]) {
                                closestDist[0] = distance;
                                closestDist[1] = 1;
                                currentClosest = there;
                                closestItem = item;
                            }
                            break; // found food on this tile, continue to next tile
                        }
                    }
                }
            }
        }

        switch (closestDist[1]) {
            case (-1): // no food source
                return null;

            case (0):  // found Growable to go to
                startMoving = new GoToLocation(currentClosest, closestGrowable, new EatFromGrowableAction(closestGrowable));
                return startMoving.getAction(dinosaur, map); // start going towards growable

            case (1):  // found Item to go to
                startMoving = new GoToLocation(currentClosest, closestItem, new EatItemAction(closestItem));
                return startMoving.getAction(dinosaur, map); // start going towards item
        }

        return null;
    }

    /**
     * Compute the Manhattan distance between two locations.
     *
     * @param a the first location
     * @param b the first location
     * @return the number of steps between a and b if you only move in the four cardinal directions.
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

}