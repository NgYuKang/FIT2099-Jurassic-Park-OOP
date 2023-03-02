package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Used to let player quit the game.
 * @author NgYukang
 * @author Amos Leong Zheng Khang
 * @see Actor
 * @see Action
 * @see GameMap
 * @since 22/05/2021
 */
public class QuitGameAction extends Action {

    /**
     * Performs the quit game action.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        map.removeActor(actor);
        return actor.toString() + " quits the game";
    }

    /**
     * Returns a descriptive string
     *
     * @param actor The actor performing the action.
     * @return the text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor.toString() + " quits the game";
    }
}
