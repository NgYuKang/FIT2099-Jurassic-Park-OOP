package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.behaviour.LandBreedBehaviour;
import game.dinosaur.DinosaurStatus;
import game.dinosaur.Gender;

/**
 * Special Action breeds with an Actor
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see Action
 * @see LandBreedBehaviour
 * @see DinosaurStatus
 * @see Gender
 * @see GameMap
 * @since 03/05/2021
 */

public class  BreedAction extends Action {

    /**
     * Target to breed with.
     */
    private Actor target;

    /**
     * Constructor.
     * @param target target to breed with
     */
    public BreedAction(Actor target) {
        this.target = target;
    }


    /**
     * executes breeding process, checks if actor is female, if so, make it pregnant, otherwise do nothing
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return null if actor is MALE, or a description that indicates actor is now pregnant
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        String result=actor + " mates with " + target + System.lineSeparator();

        // remove the WANTS_TO_BREED enum so target will do something next turn instead of staying in place
        // only applies for flying dinosaurs
        if (target.hasCapability(DinosaurStatus.CAN_FLY)) {
            if (target.hasCapability(DinosaurStatus.WANTS_TO_BREED)) {
                target.removeCapability(DinosaurStatus.WANTS_TO_BREED);
            }
            else {
                result += target + " no longer wants to be with " + actor + ".";
                return result;
            }
        }

        if (actor.hasCapability(Gender.FEMALE)) {
            result += actor + " is ";
            if (actor.hasCapability(DinosaurStatus.PREGNANT)) {
                result += "already ";
            }
            else {
                actor.addCapability(DinosaurStatus.PREGNANT);
            }
            return result + "pregnant.";
            }

        else {
            result += target + " is ";
            if (target.hasCapability(DinosaurStatus.PREGNANT)) {
                result += "already ";
            }
            else {
                target.addCapability(DinosaurStatus.PREGNANT);
            }

            return result + "pregnant.";
        }
    }


    /**
     * indicates that actor is mating on log
     * @param actor The actor performing the action.
     * @return description that actor is mating
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " mates with " + target;
    }
}
