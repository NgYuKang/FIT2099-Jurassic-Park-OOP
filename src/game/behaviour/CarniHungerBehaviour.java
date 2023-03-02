package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.EatItemAction;
import game.action.EatPreyAction;
import game.dinosaur.CarnivoreDinosaur;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.items.ItemStats;

/**
 * Class that decides whether a carnivore is hungry and finds food if so
 *
 * @author Lin Chen Xiang
 * @see Behaviour
 * @see Actor
 * @see Action
 * @see Location
 * @see HungerBehaviour
 * @see Dinosaur
 * @see game.dinosaur.CarnivoreDinosaur
 * @see DinosaurStatus
 * @see ItemStats
 * @see EatPreyAction
 * @see EatItemAction
 * @see FollowBehaviour
 * @see GoToLocation
 * @see GameMap
 * @since 03/05/2021
 */

public class CarniHungerBehaviour implements HungerBehaviour {

    /**
     * Empty Constructor
     */
    public CarniHungerBehaviour() {
    }

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

        // check if it is a carnivore
        if (actor.hasCapability(DinosaurStatus.TEAM_CARNIVORE)  && ((Dinosaur) actor).isHungry()) {

            Location here = map.locationOf(actor);
            Enum<DinosaurStatus> capability = ((CarnivoreDinosaur) actor).getAttackableEnum();

            // check surroundings if there is food source
            for (Exit exit : here.getExits()) {
                Location destination = exit.getDestination();
                // found prey, immediately attack if havent attack before
                if (destination.containsAnActor() && destination.getActor().hasCapability(capability)
                        && destination.getActor().hasCapability(DinosaurStatus.ON_LAND)) {
                    // false if Allosaur already attacked this Dinosaur within 20 turns, true otherwise
                    boolean canAttack = ((CarnivoreDinosaur)actor).canAttack((Dinosaur) destination.getActor());
                    // if canAttack is true means not attacked before
                    if (canAttack) {
                        return new EatPreyAction(destination.getActor());
                    }

                } else {
                    // access all items on this location
                    for (Item item : destination.getItems()) {
                        // found food, immediately eat
                        if (item.hasCapability(ItemStats.CARNIVORE_CAN_EAT)) {
                            if (item.hasCapability(ItemStats.MULTI_TURN_EATING) && actor.hasCapability(DinosaurStatus.SMALL_BODY)) {
                                Behaviour eat = new SmallBeakFeedingBehaviour(item);
                                Behaviour goToItem = new GoToLocation(destination, eat.getAction(actor, map));
                                return goToItem.getAction(actor, map);
                            }
                            return new EatItemAction(item);
                        }
                    }
                }
            }

            // no prey/food found in surroundings, start finding nearest food source in map
            return findFood(here, actor, map);
        }

        return null; // not carnivore
    }


    /**
     * Finds the closest prey or carnivore food source in the map and start moving towards it.
     *
     * @param here     location of this actor
     * @param dinosaur the actor looking for food
     * @param map      World map
     * @return null if no food found, or Action to start moving towards food
     */
    @Override
    public Action findFood(Location here, Actor dinosaur, GameMap map) {

        Enum<DinosaurStatus> capability = ((CarnivoreDinosaur) dinosaur).getAttackableEnum();
        Behaviour startMoving; // FollowBehaviour/GoToLocation
        Location there; // targeted location
        Location currentClosest = here; // default set to location of dinosaur
        Item closestItem = null; // closest found food
        int distance; // calculated distance between here and there
        int[] closestDist = {Integer.MAX_VALUE, -1}; // index 0 represents the calculated distance
        // index 1 represents: -1 -> no food source
        //                      0 -> found Actor to follow
        //                      1 -> found Item to go to

        // go through entire map
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                // current tile
                there = map.at(x, y);
                // if found potential prey
                if (there.containsAnActor() && there.getActor().hasCapability(capability)
                        && there.getActor().hasCapability(DinosaurStatus.ON_LAND)) {
                    // false if Allosaur already attacked this Dinosaur within 20 turns, true otherwise
                    boolean canAttack = ((CarnivoreDinosaur)dinosaur).canAttack((Dinosaur) there.getActor());
                    // if canAttack is true means not attacked before
                    if (canAttack) {
                        distance = distance(here, there);
                        // if this prey is nearer than current nearest target, overwrite nearest target to this target
                        if (distance < closestDist[0]) {
                            closestDist[0] = distance;
                            closestDist[1] = 0;
                            currentClosest = there;
                        }
                    }

                } else { // no potential prey on this tile, find potential food Item
                    for (Item item : there.getItems()) {
                        if (item.hasCapability(ItemStats.CARNIVORE_CAN_EAT)) {
                            distance = distance(here, there);
                            // if this food Item is nearer than current nearest target, overwrite nearest target to this
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

            case (0):  // found Actor to follow
                startMoving = new FollowBehaviour(currentClosest.getActor(), new EatPreyAction(currentClosest.getActor()));
                return startMoving.getAction(dinosaur, map); // start following

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
