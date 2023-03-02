package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.action.FeedAction;
import game.behaviour.HerbHungerBehaviour;
import game.items.ItemStats;


/**
 * Represents a Herbivore dinosaur.
 *
 * @author NgYuKang
 * @version 1.0
 * @see DinosaurStatus
 * @see Gender
 * @see Dinosaur
 * @since 25/04/2021
 */
public abstract class HerbivoreDinosaur extends Dinosaur {

    /**
     * Constructor for an adult Herbivore Dinosaur.
     *
     * @param name        Name of the dinosaur.
     * @param displayChar Character used to represent the dinosaur on the map.
     * @param hitPoints   Max HP of the dinosaur.
     * @param gender      Gender of the dinosaur.
     * @param thirstMax   Maximum thirst the dinosaur should have
     */
    public HerbivoreDinosaur(String name, char displayChar, int hitPoints, Enum<Gender> gender, int thirstMax) {
        super(name, displayChar, hitPoints, gender, thirstMax);
        addCapability(DinosaurStatus.TEAM_HERBIVORE);
        //TODO: Add other behaviour here
        behaviourList.add(2, new HerbHungerBehaviour());
    }

    /**
     * Constructor for a baby Herbivore Dinosaur.
     *
     * @param name        Name of the dinosaur.
     * @param displayChar Character used to represent the dinosaur on the map.
     * @param hitPoints   Max HP of the dinosaur.
     * @param thirstMax   Maximum thirst the dinosaur should have
     */
    public HerbivoreDinosaur(String name, char displayChar, int hitPoints, int thirstMax) {
        super(name, displayChar, hitPoints, thirstMax);
        addCapability(DinosaurStatus.TEAM_HERBIVORE);
        //TODO: Add other behaviour here
        behaviourList.add(2, new HerbHungerBehaviour());
    }


    /**
     * Generates action that other actors can do to this herbivore dinosaur
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return actions that the other actor can do to a carnivore dinosaur.
     */
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = super.getAllowableActions(otherActor, direction, map);
        // TODO: Replace placeholder once other parts are done
        for (Item item : otherActor.getInventory()) {
            if (item.hasCapability(ItemStats.HERBIVORE_CAN_EAT)) { // Placeholder
                actions.add(new FeedAction(this, item)); // Placeholder, replace with feed.
            }
        }
        return actions;
    }

}
