package game.behaviour;

import edu.monash.fit2099.engine.*;

/**
 * Class that decides whether an actor is hungry and finds food if so
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see Action
 * @see Location
 * @see GameMap
 * @see Behaviour
 * @see CarniHungerBehaviour
 * @see HerbHungerBehaviour
 * @since 05/05/2021
 */

public interface HungerBehaviour extends Behaviour{

    /**
     * Finds the nearest suitable food source in the map.
     * @param here location of this actor
     * @param actor the actor looking for food
     * @param map World map
     * @return null if no food source found, Action to start moving towards food
     */
    Action findFood(Location here, Actor actor, GameMap map);

}
