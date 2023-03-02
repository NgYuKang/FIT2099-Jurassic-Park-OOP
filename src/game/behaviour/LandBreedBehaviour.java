package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.BreedAction;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.dinosaur.Gender;

/**
 * Class that decides whether a ground type actor is ready to breed and find a breed partner within a certain radius
 *
 * @author Lin Chen Xiang
 * @see BreedBehaviour
 * @see FollowBehaviour
 * @see BreedAction
 * @see Location
 * @see Dinosaur
 * @see DinosaurStatus
 * @see Gender
 * @since 03/05/2021
 */

public class LandBreedBehaviour extends BreedBehaviour {

    /**
     * Set radius for ground actors to find a partner within
     */
    private final int MAX_RADIUS = 3;

    /**
     * Goes through each radius to check for suitable breeding partner, then do action if found
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return Action to be done this turn (MoveActorAction, BreedAction)
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        // cast to Dinosaur
        Dinosaur dinosaur = (Dinosaur) actor;

        // Dinosaur not eligible for breeding
        if (!passConditions(dinosaur)) {
            return null;
        }

        Location here = map.locationOf(dinosaur);
        Action action;
        Gender gender;

        if (dinosaur.hasCapability(Gender.MALE)) {
            gender = Gender.FEMALE;
        }
        else {
            gender = Gender.MALE;
        }

        // innermost radius -- if partner found immediately breed
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();

            // if exit has an actor and is the same type of Dinosaur
            if (destination.containsAnActor() && destination.getActor().getClass() == dinosaur.getClass()) {
                // if actor and target are opposite gender
                if (destination.getActor().hasCapability(gender)) {
                    // if target is not underage nor pregnant
                    if (!(destination.getActor().hasCapability(DinosaurStatus.BABY)
                            || destination.getActor().hasCapability(DinosaurStatus.PREGNANT))) {
                        return new BreedAction(destination.getActor());
                    }
                }
            }
        }

        // Set radius for land actors to detect a breed partner
        for (int r=2; r<=MAX_RADIUS; r++) {
            action = findPartnerInRadius(here, r, dinosaur, gender, map);
            if (action!=null) { // findPartnerInRadius returns moveactoraction
                return action;
            }
        }

        // no partner found
        return null;
    }

    /**
     * Check if this tile has a suitable partner
     * For Dinosaurs who cannot fly
     * @param there Location of this tile
     * @param dinosaur the Dinosaur finding a partner
     * @param map World map
     * @return null if no suitable partner, follow.getAction if found partner
     */
    @Override
    public Action hasPartner(Location there, Dinosaur dinosaur, Gender targetGender, GameMap map) {

        Behaviour follow;

        // if tile has an Actor and is the same type of Dinosaur
        if (there.containsAnActor() && there.getActor().getClass() == dinosaur.getClass()) {

            // if they are opposite gender
            if (there.getActor().hasCapability(targetGender)) {
                // if target is not underage nor pregnant
                if (!(there.getActor().hasCapability(DinosaurStatus.BABY)
                        || there.getActor().hasCapability(DinosaurStatus.PREGNANT))) {
                    follow = new FollowBehaviour(there.getActor(), new BreedAction(there.getActor()));
                    return follow.getAction(dinosaur, map); // start following
                }
            }
        }
        return null; // no partner found
    }

}
