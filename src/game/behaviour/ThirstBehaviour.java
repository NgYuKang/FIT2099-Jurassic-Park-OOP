package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.DrinkFromWaterTileAction;
import game.dinosaur.Dinosaur;
import game.watertile.WaterTile;
import game.watertile.WaterTileStatus;

/**
 * A behaviour class used to find water if thirsty.
 * Code very similar to HungerBehaviour. Technical debt I'm willing to accept.
 *
 * @author NgYuKang
 * @version 1.0
 * @see Dinosaur
 * @see WaterTile
 * @see WaterTileStatus
 * @since 16/05/2021
 */
public class ThirstBehaviour implements Behaviour {

    /**
     * A factory for creating actions. Chaining these together can result in an actor performing more complex tasks.
     * <p>
     * A Behaviour represents a kind of objective that an Actor can have.  For example
     * it might want to seek out a particular kind of object, or follow another Actor,
     * or run away and hide.  Each implementation of Behaviour returns an Action that the
     * Actor could take to achieve its objective, or null if no useful options are available.
     * method that determines which Behaviour to perform.  This allows the Behaviour's logic
     * to be reused in other Actors via delegation instead of inheritance.
     * <p>
     * An Actor's {@code playTurn()} method can use Behaviours to help decide which Action to
     * perform next.  It can also simply create Actions itself, and for simpler Actors this is
     * likely to be sufficient.  However, using Behaviours allows
     * us to modularize the code that decides what to do, and that means that it can be
     * reused if (e.g.) more than one kind of Actor needs to be able to seek, follow, or hide.
     *
     * @param actor the Actor acting
     * @param map   the GameMap containing the Actor
     * @return an Action that actor can perform, or null if actor can't do this
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        // check if it's thirsty
        Dinosaur dinosaur = ((Dinosaur) actor);
        if (dinosaur.isThirsty()) {

            Enum<WaterTileStatus> capability = WaterTileStatus.WATER_TRAVERSE;
            Location here = map.locationOf(actor);

            // check surroundings if there is water source
            for (Exit exit : here.getExits()) {
                Location destination = exit.getDestination();
                // found water immediately drink from that water
                if (destination.getGround().hasCapability(capability)) {
                    WaterTile tile = ((WaterTile) destination.getGround());
                    if (tile.getSipCapacity() > 0) {
                        return new DrinkFromWaterTileAction(tile);
                    }
                }

                // no tile at surrounding
                return findWater(here, actor, map);
            }
        }
        return null; // If not thirsty
    }

    /**
     * Find water to drink.
     *
     * @param here Location we are at
     * @param dinosaur the Dinosaur we trying to find water for
     * @param map The map we are in
     * @return a DrinkFromWaterTileAction if we found something to drink, else null
     */
    public Action findWater(Location here, Actor dinosaur, GameMap map) {
        Behaviour startMoving; // GoToLocation
        Location there; // targeted location
        Location currentClosest = here; // default set to location of dinosaur
        WaterTile tile = null; // closest found food source
        int distance; // calculated distance between here and there
        int[] closestDist = {Integer.MAX_VALUE, -1};
        // index 0 represents: the calculated distance
        // index 1 represents: -1 -> no food source
        //                      0 -> found Water to go to

        Enum<WaterTileStatus> capability = WaterTileStatus.WATER_TRAVERSE;

        // go through entire map
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                // current tile
                there = map.at(x, y);
                // if found potential water
                if (there.getGround().hasCapability(capability)) {
                    // check if this water has any water on it
                    if (((WaterTile) there.getGround()).getSipCapacity() > 0) {
                        distance = distance(here, there);
                        // if this water is nearer than current nearest goal, overwrite nearest goal to this goal
                        if (distance < closestDist[0]) {
                            closestDist[0] = distance;
                            closestDist[1] = 0;
                            currentClosest = there;
                            tile = ((WaterTile) there.getGround());
                        }
                    }
                }
            }
        }

        switch (closestDist[1]) {
            case (-1): // no water
                return null;

            case (0):  // found water
                startMoving = new GoToLocation(currentClosest, new DrinkFromWaterTileAction(tile));
                return startMoving.getAction(dinosaur, map); // start going towards growable

        }
        // backup null
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
