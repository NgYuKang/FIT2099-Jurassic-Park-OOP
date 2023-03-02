package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.action.FeedAction;
import game.behaviour.CarniHungerBehaviour;
import game.items.ItemStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Carnivore dinosaur.
 * Has a hashmap to check all the dinosaurs it has attacked.
 *
 * @author NgYuKang
 * @version 1.0
 * @see DinosaurStatus
 * @see Gender
 * @see Dinosaur
 * @since 25/04/2021
 */
public abstract class CarnivoreDinosaur extends Dinosaur {

    /**
     * Used to store all the Stegosaur that this Allosaur has attacked.
     */
    private final Map<Dinosaur, Integer> attackedDinosaur;

    /**
     * Constructor for an adult Carnivore Dinosaur.
     *
     * @param name        Name of the dinosaur.
     * @param displayChar Character used to represent the dinosaur on the map.
     * @param hitPoints   Max HP of the dinosaur.
     * @param gender      Gender of the dinosaur.
     * @param thirstMax   Maximum thirst the dinosaur should have
     */
    public CarnivoreDinosaur(String name, char displayChar, int hitPoints, Enum<Gender> gender, int thirstMax) {
        super(name, displayChar, hitPoints, gender, thirstMax);
        attackedDinosaur = new HashMap<>();
        addCapability(DinosaurStatus.TEAM_CARNIVORE);
        behaviourList.add(1, new CarniHungerBehaviour());
    }

    /**
     * Constructor for a baby Carnivore Dinosaur, randomised gender.
     *
     * @param name        Name of the dinosaur.
     * @param displayChar Character used to represent the dinosaur on the map.
     * @param hitPoints   Max HP of the dinosaur.
     * @param thirstMax   Maximum thirst the dinosaur should have
     */
    public CarnivoreDinosaur(String name, char displayChar, int hitPoints, int thirstMax) {
        super(name, displayChar, hitPoints, thirstMax);
        attackedDinosaur = new HashMap<>();
        addCapability(DinosaurStatus.TEAM_CARNIVORE);
        behaviourList.add(2, new CarniHungerBehaviour());

    }

    /**
     * @return the Enum that allows this Dinosaur to attack another Dinosaur
     */
    public abstract Enum<DinosaurStatus> getAttackableEnum();

    /**
     * Generates action that other actors can do to this carnivore dinosaur
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return actions that the other actor can do to a carnivore dinosaur.
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = super.getAllowableActions(otherActor, direction, map);
        for (Item item : otherActor.getInventory()) {
            if (item.hasCapability(ItemStats.CARNIVORE_CAN_EAT)) {
                actions.add(new FeedAction(this, item));
            }
        }
        return actions;
    }

    /**
     * @param target the Dinosaur to check
     * @return Whether this Allosaur can attack the target (Is it in the list of attacked stegosaur?)
     */
    public boolean canAttack(Dinosaur target) {
        boolean ret;
        if (attackedDinosaur.get(target) == null) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * Adds the target Stegosaur to the hashmap
     *
     * @param target the Stegosaur that was attacked
     */
    public void insertDinosaurAttacked(Dinosaur target) {
        attackedDinosaur.put(target, 0);
    }

    /**
     * Lets a CarnivoreDinosaur have its turn.
     * Will iterate through the entire hashmap to make update how long since
     * last attack on a dinosaur it has attacked
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return Action to take during this turn, defaults to DoNothingAction if can't find any.
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        // Loop through all attacked stegosaur
        // Temp Array: Can't remove while in this loop
        ArrayList<Dinosaur> dinosaurToRemove = new ArrayList<>();
        for (Map.Entry<Dinosaur, Integer> stegosaurIntegerEntry : attackedDinosaur.entrySet()) {
            Dinosaur dinosaur = stegosaurIntegerEntry.getKey();
            int timeElapsed = stegosaurIntegerEntry.getValue();
            timeElapsed++;
            // Remove if is 20 turns or more already. Current game rule (Allosaur's)
            // else update value
            if (timeElapsed >= 2) {
                dinosaurToRemove.add(dinosaur);
            } else {
                attackedDinosaur.put(dinosaur, timeElapsed);
            }
        }

        // Remove operation
        for (Dinosaur dinosaur : dinosaurToRemove) {
            attackedDinosaur.remove(dinosaur);
        }

        return super.playTurn(actions, lastAction, map, display);
    }

}
