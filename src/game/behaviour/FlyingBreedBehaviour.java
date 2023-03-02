package game.behaviour;

import edu.monash.fit2099.engine.*;
import game.action.BreedAction;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.dinosaur.Gender;
import game.growable.GrowableStatus;

/**
 * Class that decides whether a flying type actor is ready to breed and find a breed partner within a certain radius
 *
 * @author Lin Chen Xiang
 * @see BreedBehaviour
 * @see GoToLocation
 * @see BreedAction
 * @see Location
 * @see Dinosaur
 * @see DinosaurStatus
 * @see Gender
 * @since 20/05/2021
 */

public class FlyingBreedBehaviour extends BreedBehaviour {

    /**
     * Set radius for flying actors to find a partner within
     */
    private final int MAX_RADIUS = 5;

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
        if (here.getGround().hasCapability(GrowableStatus.TALL)) {
            for (Exit exit : here.getExits()) {
                Location destination = exit.getDestination();

                // if exit has an actor and is the same type of Dinosaur
                if (destination.containsAnActor() && destination.getActor().getClass() == dinosaur.getClass()) {
                    // if actor and target are opposite gender
                    if (destination.getActor().hasCapability(gender)) {
                        // if target is not underage nor pregnant
                        if (!(destination.getActor().hasCapability(DinosaurStatus.BABY)
                                || destination.getActor().hasCapability(DinosaurStatus.PREGNANT))) {
                            // target is on a tall growable, immediately breed
                            if (destination.getGround().hasCapability(GrowableStatus.TALL)) {
                                return new BreedAction(destination.getActor());
                            }
                        }
                    }
                }
            }
        }
        // set radius for flying actors to find partner, starting from 2-tile radius to max radius set
        for (int r=2; r<=MAX_RADIUS; r++) {
            action = findPartnerInRadius(here, r, dinosaur, gender, map);
            if (action != null) {
                return action;
            }
        }
        // no potential partner found
        return null;
    }

    /**
     * Checks whether this Location has a suitable partner and its surroundings allows actor to breed with this partner
     * @param there Location of this tile
     * @param dinosaur the Dinosaur finding a partner
     * @param targetGender the gender of the target which should be opposite of dinosaur
     * @param map World map
     * @return null if no partner or no tall growable around partner, or action to move towards a location beside found partner
     */
    @Override
    Action hasPartner(Location there, Dinosaur dinosaur, Gender targetGender, GameMap map) {

        Behaviour approach;

        // if tile has an Actor and is the same type of Dinosaur
        if (there.containsAnActor() && there.getActor().getClass() == dinosaur.getClass()) {

            // if they are opposite gender and is on top of a tall growable
            if (there.getGround().hasCapability(GrowableStatus.TALL) && there.getActor().hasCapability(targetGender)) {
                // if target is not underage nor pregnant
                if (!(there.getActor().hasCapability(DinosaurStatus.BABY)
                        || there.getActor().hasCapability(DinosaurStatus.PREGNANT))) {
                    // find another tall growable around this target to go to
                    for (Exit exit : there.getExits()) {
                        Location destination = exit.getDestination();
                        if (destination.getGround().hasCapability(GrowableStatus.TALL)) {
                            // dinosaur makes a mating call, target will now stay in position and wait for dinosaur to breed with
                            there.getActor().addCapability(DinosaurStatus.WANTS_TO_BREED);
                            approach = new GoToLocation(destination, new BreedAction(there.getActor()));
                            return approach.getAction(dinosaur, map); // start approaching
                        }
                    }

                }
            }
        }
        // no potential target found
        return null;
    }
}
