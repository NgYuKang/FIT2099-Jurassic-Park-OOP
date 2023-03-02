package game.behaviour;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.action.BreedAction;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.dinosaur.Gender;

/**
 * Class that decides whether an actor is ready to breed and find a breed partner
 *
 * @author Lin Chen Xiang
 * @see LandBreedBehaviour
 * @see FlyingBreedBehaviour
 * @see BreedAction
 * @since 20/05/2021
 */

public abstract class BreedBehaviour implements Behaviour {

    /**
     * Checks common conditions whether Dinosaur is eligible for breeding or not
     * @param dinosaur the dinosaur to check if pass all conditions to breed
     * @return true if pass all conditions, false otherwise
     */
    boolean passConditions(Dinosaur dinosaur) {
        boolean flag = true;

        // Dinosaur is underage or already pregnant, can't breed
        if (dinosaur.hasCapability(DinosaurStatus.BABY) || dinosaur.hasCapability(DinosaurStatus.PREGNANT)) {
            flag = false;
        }

        // not enough hitpoints or doesn't feel like breeding
        else if (!(dinosaur.isWellFed() && wantsToBreed())) {
            flag = false;
        }

        return flag;
    }

    /**
     * Find a suitable partner in set radius, return action if found
     *
     * @param here Location of actor looking for breeding partner
     * @param radius the radius of the actor to determine bound of actor surroundings
     * @param dinosaur the dinosaur searching for partner
     * @param map World map
     * @return null if no partner found in radius, follow if found partner (moveactoraction)
     */
    public Action findPartnerInRadius(Location here, int radius, Dinosaur dinosaur, Gender targetGender, GameMap map) {

        int counterX;
        int counterY = radius;
        Location there;
        Action follow;

        // diameter
        int length = radius*2;

        // go through each row if radius starting from here.y()+radius
        for (int i=0; i<=length; i++) {

            // start x at here.x()-radius
            counterX = radius*-1;

            // if at first or last row of radius, go through all tiles
            if (counterY == radius || counterY == (radius*-1)) {
                // go through all tiles of that row starting at (radius*-1, counterY)
                for (int j=0; j<=length; j++) {
                    // see if current tile is accessible
                    try {
                        there = map.at(here.x() + counterX, here.y() + counterY);
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                    follow = hasPartner(there, dinosaur, targetGender, map); // check if has partner
                    if (follow!=null) {// not null, return the action
                        return follow;
                    }

                    counterX++; // no partner at this tile, go to next
                }
            }
            // in between first and last row of radius
            else {
                // go through first and last column of this row
                for (int j=0; j<=length; j+=length) {
                    counterX += j; // 1st counterX = radius*-1, 2nd counterX = radius
                    // see if current tile is accessible
                    try {
                        there = map.at(here.x() + counterX, here.y() + counterY);
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                    follow = hasPartner(there, dinosaur, targetGender, map); // check if has partner
                    if (follow!=null) { // not null, return the action
                        return follow;
                    }
                }
            }

            counterY--; // no partner at this row, decrement
        }

        return null; // no partner found in radius
    }

    /**
     * Check if this tile has a suitable partner
     *
     * @param there Location of this tile
     * @param dinosaur the Dinosaur finding a partner
     * @param map World map
     * @return null if no suitable partner, follow.getAction if found partner
     */
    abstract Action hasPartner(Location there, Dinosaur dinosaur, Gender targetGender, GameMap map);

    /**
     * Dinosaur has a 70% chance of wanting to breed
     * @return 0.7 which is the chance of wanting to breed
     */
    double breedChance() {return 0.7;}

    /**
     * See whether Dinosaur feels like breeding
     * @return true if Dinosaur wants to breed, false otherwise
     */
    boolean wantsToBreed() {
        double chance = Math.random();
        return chance < breedChance();
    }

}
