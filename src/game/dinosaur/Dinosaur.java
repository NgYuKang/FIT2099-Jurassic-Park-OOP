package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.action.AttackAction;
import game.action.DieFromNaturalCausesAction;
import game.action.LayEggAction;
import game.behaviour.*;
import game.growable.GrowableStatus;
import game.watertile.WaterTileStatus;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a dinosaur. This class is abstract and should still be extended into Herbivore and Carnivore dinosaurs.
 * Contains all the codes to check hunger, pregnancy progression, baby growth.
 * Also contains code to know what the player can do to ALL dinosaurs (dinosaur specific will be in their own classes)
 * Override abstract methods here to give the dinosaur its stats.
 * Add needed behaviour in constructor in extended classes as needed to give it other behaviours.
 *
 * @author NgYuKang
 * @version 1.0
 * @see DinosaurStatus
 * @see Gender
 * @since 25/04/2021
 */
public abstract class Dinosaur extends Actor {

    /**
     * Used to keep track of the dinosaur's age. Used in transitioning a baby to an adult
     */
    private int age;

    /**
     * Used to keep track of pregnancy.
     */
    private int pregnantAge = 0;

    /**
     * Used to keep track how long a dinosaur has been unconscious from hunger
     */
    private int unConsciousHungerElapsed = 0;

    /**
     * Used to keep track of how long a dinosaur has been unconscious from thirst.
     */
    private int unConsciousThirstElapsed = 0;

    /**
     * List of behaviours the dinosaur can do.
     * Will be looped through from start to end, representing behaviours with more priority at the front
     */
    protected final ArrayList<Behaviour> behaviourList = new ArrayList<>();

    /**
     * Used as water level, to know how thirsty a dinosaur is. 0 means very thirsty.
     */
    protected int thirst;
    /**
     * Maximum level of thirst a dinosaur can have
     */
    protected int maxThirst;
    /**
     * Used to keep track of flying "fuel"
     */
    protected int flyCounter;

    /**
     * Used to keep track of number of turns pass while waiting for something
     */
    protected int turnsWaited;

    /**
     * Constructor to initialise a dinosaur to the adult age. Requires gender input
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the display
     * @param hitPoints   the Actor's max hit points
     * @param gender      Gender of the dinosaur.
     * @param thirstMax   Maximum thirst the dinosaur should have
     */
    public Dinosaur(String name, char displayChar, int hitPoints, Enum<Gender> gender, int thirstMax) {
        // hitPoints here is used to initialise the character's max hp.
        // We will initialise the starting hp via another method.
        super(name, displayChar, hitPoints);
        // Stats
        addCapability(gender);
        age = getAdultAge();
        this.hitPoints = getStartingHP();
        maxThirst = thirstMax;
        thirst = getStartingThirst();
        flyCounter = 0;
        turnsWaited = 0;

        // Insert all behaviour
        behaviourList.add(0, new WanderBehaviour());
        behaviourList.add(0, new ThirstBehaviour());

        // Fly
        if (getMaxFlyingTile() > 0){
            addCapability(DinosaurStatus.CAN_FLY);
            addCapability(DinosaurStatus.FLYING);
            // TODO: Add go back to tree behaviour
            behaviourList.add(0, new FlyingBreedBehaviour());
            // only goes to tree if no current goal or chained action
            behaviourList.add(0, new GoToTallGrowableBehaviour());
        }
        else {
            addCapability(DinosaurStatus.ON_LAND);
            behaviourList.add(0, new LandBreedBehaviour());
        }
    }

    /**
     * Constructor to initialise a baby dinosaur
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the display
     * @param hitPoints   the Actor's starting hit points
     * @param thirstMax   Maximum thirst the dinosaur should have
     */
    public Dinosaur(String name, char displayChar, int hitPoints, int thirstMax) {
        // hitPoints here is used to initialise the character's max hp.
        // We will initialise the starting hp via another method.
        super(name, displayChar, hitPoints);

        // Stats
        Random random = new Random();
        boolean res = random.nextBoolean();
        Enum<Gender> gender;
        if (!res) {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }

        addCapability(gender);
        age = 0;
        this.hitPoints = getBabyStartingHP();
        addCapability(DinosaurStatus.BABY);
        maxThirst = thirstMax;
        thirst = getStartingThirst();
        flyCounter = 0;
        turnsWaited = 0;

        // Insert all behaviour
        behaviourList.add(0, new WanderBehaviour());
        behaviourList.add(0, new ThirstBehaviour());

        // Fly
        if (getMaxFlyingTile() > 0){
            addCapability(DinosaurStatus.CAN_FLY);
            addCapability(DinosaurStatus.FLYING);
            // TODO: Add go back to tree behaviour
            behaviourList.add(0, new FlyingBreedBehaviour());
            // only goes to tree if no current goal or chained action
            behaviourList.add(0, new GoToTallGrowableBehaviour());
        }
        else {
            addCapability(DinosaurStatus.ON_LAND);
            behaviourList.add(0, new LandBreedBehaviour());
        }
    }

    /**
     * Adds actions that other Actor (only player at this point) can do.
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return list of actions the other actor can do.
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        return new Actions(new AttackAction(this));
    }

    /**
     * @return How long it takes for this dinosaur's corpse to rot.
     */
    public abstract int getCorpseRotTime();

    /**
     * @return the HP(hunger) that the dinosaur is considered hungry.
     */
    public abstract int getHungerThreshold();

    /**
     * @return the age that the dinosaur is considered an adult
     */
    public abstract int getAdultAge();

    /**
     * @return The duration taken for the egg to hatch
     */
    public abstract int getIncubationPeriod();

    /**
     * @return how much the adult dinosaur's starting hp should be.
     */
    public abstract int getStartingHP();

    /**
     * @return how much hp a baby dinosaur of this type should have.
     */
    public abstract int getBabyStartingHP();

    /**
     * @return how long can this dinosaur be unconscious before it is considered dead.
     */
    public abstract int getHungerUnConsciousThreshold();

    /**
     * @return how long does the pregnancy of this dinosaur last.
     */
    public abstract int getPregnancyLength();

    /**
     * @return The integer where this dinosaur is considered well fed.
     */
    public abstract int getWellFeedHunger();

    /**
     * @return Whether the dinosaur is currently considered well fed
     */
    public boolean isWellFed() {
        return (hitPoints > getWellFeedHunger());
    }

    /**
     * @return Whether the dinosaur is hungry
     */
    public boolean isHungry() {
        return (hitPoints < getHungerThreshold());
    }

    /**
     * Override this for each unique dinosaur so that
     * we can generate a new dinosaur from a dinosaur object
     * Will be useful when a dinosaur lays an egg, we create
     * a new dinosaur and put the dinosaur in there.
     * An egg will still be ticked every turn but since the dinosaur is
     * not referenced by the world yet, its turn will not be processed.
     * The dinosaur will then only be placed onto the map, into the game
     * when the egg successfully hatches. If the egg doesn't hatch and gets
     * destroyed (not referenced anymore), so does the dinosaur.
     * <p>
     * This will just run the second constructor, basically. Easier polymorphism.
     *
     * @return A new instance of this dinosaur
     */
    public abstract Dinosaur getNewDinosaur();

    /**
     * @return How much HP does the corpse of this dinosaur heals
     */
    public abstract int getCorpseHealAmount();

    /**
     * @return How much this dinosaur's egg should cost.
     */
    public abstract int getEggPurchasePrice();

    /**
     * @return How much eco points are gained when a dinosaur of this kind hatches.
     */
    public abstract int getEggHatchEcoPoint();

    /**
     * Override this to give custom starting thirst other than the default 60
     *
     * @return How much the dinosaur's thirst should start with
     */
    public int getStartingThirst(){
        return 60;
    }

    /**
     * Drinks water. Increases Thirst (Higher means less thirsty)
     *
     * @param amountDrank How much to drink
     */
    public void drink(int amountDrank) {
        thirst += amountDrank;
        thirst = Math.min(thirst, maxThirst);
    }

    /**
     * Decreases the dinosaur's thirst
     */
    private void decreaseThirst() {
        thirst--;
        thirst = Math.max(thirst, 0);
    }

    /**
     * @return At what point the dinosaur is considered thirsty (Universally shared across dinosaurs)
     */
    public int getThirstThreshold() {
        return 40;
    }

    /**
     * @return Is the dinosaur thirsty
     */
    public boolean isThirsty() {
        return (thirst < getThirstThreshold());
    }

    /**
     * Is this Actor conscious?
     * Returns true if the current Actor has positive hit points.
     * Actors on zero hit points are deemed to be unconscious.
     * New Rule: Thirst too. If their thirst hits 0, they become unconscious (Dinosaur)
     * <p>
     * Depending on the game client, this status may be interpreted as either
     * unconsciousness or death, or inflict some other kind of status.
     *
     * @return True if both HitPoints and Thirst are above 0
     */
    @Override
    public boolean isConscious() {
        return (super.isConscious() && thirst > 0);
    }

    /**
     * @return How much the dinosaur can drink in one go
     */
    public abstract int getMaxDrinkAmount();

    /**
     * Code to run pregnancy check. Override the default one if a dinosaur
     * has special pregnancy checks.
     *
     * @param map The map the dinosaur is on
     * @return Action to return, null if cannot lay egg
     */
    protected Action pregnancyLayEggCheck(GameMap map) {
        if (hasCapability(DinosaurStatus.PREGNANT)) {
            if (pregnantAge >= getPregnancyLength()) {
                pregnantAge = 0;
                // if is flying dinosaur, go to tall growable first before laying egg
                if (hasCapability(DinosaurStatus.CAN_FLY)) {
                    Behaviour goToLocation = new GoToTallGrowableBehaviour(new LayEggAction());
                    return goToLocation.getAction(this, map);
                }
                // else return just lay egg on the ground
                return new LayEggAction();
            }
        }
        return null;
    }

    /**
     * How long can the dinosaur fly for. Return 0 if cant fly.
     * Override this and give value above 0 to allow a certain dinosaur to fly.
     * Default will be 0 to not let dinosaurs fly.
     *
     * @return how long can the dinosaur for for.
     */
    public int getMaxFlyingTile(){
        return 0;
    }

    /**
     * Override this to give certain dinosaurs unique threshold.
     *
     * @return How long this dinosaur can be unconscious from thirst before dying
     */
    public int getThirstUnconsciousThreshold() {
        return 15;
    }

    /**
     * Override to set a custom waiting cap for certain dinosaurs
     * @return Number of turns a Dinosaur is willing to spend to wait for something
     */
    public int getWaitingPatience() {
        return 7;
    }


    /**
     * Prints hunger or thirst message when hungry or thirsty.
     *
     * @param display the I/O object to which messages may be written
     * @param here    the location of the dinosaur
     */
    private void hungerThirstMessage(Display display, Location here) {
        // Check hunger
        // Makes sure to print it only once when it becomes hungry
        // When it is no longer hungry, it is indicated via enum so the hunger message is printed again
        if (isHungry()) {
            if (!(hasCapability(DinosaurStatus.HUNGRY))) {
                display.println(String.format("%s at (%s,%s) is getting hungry!", name, here.x(), here.y()));
                addCapability(DinosaurStatus.HUNGRY);
            }
        } else {
            if (hasCapability(DinosaurStatus.HUNGRY)) {
                removeCapability(DinosaurStatus.HUNGRY);
            }
        }

        // Check thirsty
        if (isThirsty()) {
            if (!(hasCapability(DinosaurStatus.THIRSTY))) {
                display.println(String.format("%s at (%s,%s) is getting thirsty!", name, here.x(), here.y()));
                addCapability(DinosaurStatus.THIRSTY);
            }
        } else {
            if (hasCapability(DinosaurStatus.THIRSTY)) {
                removeCapability(DinosaurStatus.THIRSTY);
            }
        }
    }

    /**
     * Does operation related to attribute that happens every turn, like decreasing thirst and hunger.
     */
    protected void attributeCheck() {
        // Turn-related attribute change
        hurt(1);
        age++;
        decreaseThirst();
        // Pregnancy Progression
        if (hasCapability(DinosaurStatus.PREGNANT)) {
            pregnantAge++;
        }
        // Remove baby status if adult age
        if (hasCapability(DinosaurStatus.BABY) && (age >= getAdultAge())) {
            removeCapability(DinosaurStatus.BABY);
        }
    }

    /**
     * Allow the dinosaur to have its turn.
     * Will decrement its HP (hunger) and increment the age every turn.
     * Will check if it's hungry and print suitable message.
     * Will check if it has fainted from hunger, if yes, check if it has passed the game rule of 20 turns.
     * If so, kill it with DieFromHungerAction (Could have been just implemented here to be honest)
     * But letting death be handled by an action could be better.
     * Will also run pregnancy related code. Lays egg if it is past/equal to incubation period.
     * Will also check if the dinosaur is a baby, if yes, check if it has become an adult and adjust accordingly
     * Finally, we will find something for the dinosaur to do via behaviour.
     * This is done by looping through an ArrayList of behaviours, where the position
     * indicates the priority of the behaviour. Being at the front means highest priority.
     * If there's no good action to find from the behaviours, just do nothing
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return Action to take during this turn, defaults to DoNothingAction if can't find any.
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        attributeCheck();

        // To avoid repeated codes, to reference current location
        Location here = map.locationOf(this);

        // Check hunger and thirst and print message
        hungerThirstMessage(display, here);

        // Flying dinosaur code
        // TODO: Something that gives the dinosaur its can fly back
        if (getMaxFlyingTile() != 0) {
            // TODO: IDK if increase when traversing or flying
            // If we are still flying, increment the counter
            if (hasCapability(DinosaurStatus.FLYING)) {
                flyCounter++;
            }
            // Flying dinosaur reached its flying limit, can't fly until it reaches a tree
            if (flyCounter >= getMaxFlyingTile() && hasCapability(DinosaurStatus.FLYING)) {
                removeCapability(DinosaurStatus.FLYING);
            }
            // Flying dinosaur no longer flying for some reason
            if (!hasCapability(DinosaurStatus.FLYING)) {
                // Landed in a Lake and drowned, unfortunate
                if (here.getGround().hasCapability(WaterTileStatus.WATER_TRAVERSE)) {
                    return new DieFromNaturalCausesAction("drowned");
                }
                // Landed on the ground
                else {
                    addCapability(DinosaurStatus.ON_LAND);
                }
            }

            // TODO: Improve this instead of using instanceof
            // if on a tall growable, reset flycounter and allow actor to fly
            if (here.getGround().hasCapability(GrowableStatus.TALL)) {
                addCapability(DinosaurStatus.FLYING);
                removeCapability(DinosaurStatus.ON_LAND);
                flyCounter = 0;
            }
        }

        // Check if starving to death or thirsting to death
        if (!(isConscious())) {
            // flying dinosaur is unconscious, no longer flying
            if (hasCapability(DinosaurStatus.FLYING)) {
                removeCapability(DinosaurStatus.FLYING);
            }
            boolean doNothing = false;
            // Check unconsciousness from hunger
            if (hitPoints <= 0) {
                unConsciousHungerElapsed++;
                // If reached threshold
                if (unConsciousHungerElapsed >= getHungerUnConsciousThreshold()) {
                    return new DieFromNaturalCausesAction("starved to death");
                } else {
                    // Still unconscious, not yet dead
                    doNothing = true;
                }
            }
            // Check unconsciousness from thirst
            if (thirst <= 0) {
                unConsciousThirstElapsed++;
                // If unconscious, and rain, revive them
                if (here.getGround().hasCapability(WaterTileStatus.RAIN)) {
                    drink(10);
                    display.println(this.toString() + " drinks for 10 water level from the rain.");
                    unConsciousThirstElapsed = 0;
                } else if (unConsciousThirstElapsed >= getThirstUnconsciousThreshold()) {
                    // Else they die
                    return new DieFromNaturalCausesAction("died from thirst");
                } else {
                    // Still unconscious, not yet dead
                    doNothing = true;
                }
            }
            // If it is still unconscious from either hunger or thirst, do nothing
            if (doNothing) {
                return new DoNothingAction();
            }
        } else {
            // Keep resetting it to 0
            unConsciousHungerElapsed = 0;
            unConsciousThirstElapsed = 0;
        }



        // Pregnancy egg laying code
        Action layEggAction = pregnancyLayEggCheck(map);
        if (layEggAction != null) {
            return layEggAction;
        }

        // Handle multi-turn actions from behaviours
        if ((lastAction != null) && (lastAction.getNextAction() != null)) {
            return lastAction.getNextAction();
        }

        // actor is waiting for its partner to arrive so they can breed
        if (hasCapability(DinosaurStatus.WANTS_TO_BREED)) {
            // actor continues to wait
            if (turnsWaited <= getWaitingPatience()) {
                turnsWaited++;
                return new DoNothingAction();
            }
            // actor ran out of patience and left
            else {
                turnsWaited = 0;
                removeCapability(DinosaurStatus.WANTS_TO_BREED);
            }
        }

        // If passed everything, find something to do
        for (Behaviour behaviour : behaviourList) {
            Action action = behaviour.getAction(this, map);
            if (action != null) {
                return action;
            }
        }

        // Stuck? Too bad! Just do nothing. Probably starve to death though.
        return new DoNothingAction();
    }

}
