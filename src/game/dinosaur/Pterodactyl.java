package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.watertile.WaterTile;
import game.watertile.WaterTileStatus;

import java.util.Random;

/**
 * Represents a Carnivore Pterodactyl.
 * Pterodactyls only eat Corpse and fishes in Lakes.
 *
 * @author Lin Chen Xiang
 * @version 1.0
 * @see DinosaurStatus
 * @see Gender
 * @see CarnivoreDinosaur
 * @see Dinosaur
 * @since 10/05/2021
 */
// TODO: to Chen Xiang: Fix constructor, I added new stuff

// TODO: to Chen xiang: Implement the new methods
public class Pterodactyl extends CarnivoreDinosaur{

    /**
     * Constructor for an adult Pterodactyl with a set gender
     * Pterodactyls is shown as 'P' on map and has max HP and thirst of 100
     * Spawns with 50 HP and 60 Thirst
     * @param gender Gender to initialise the Pterodactyl with
     */
    public Pterodactyl(Enum<Gender> gender) {
        super("Pterodactyl", 'P', 100, gender, 100);
        addCapability(DinosaurStatus.ALLOSAUR_CAN_ATTACK);
        addCapability(DinosaurStatus.SMALL_BODY);
    }

    /**
     * Constructor for a baby Pterodactyl
     * Shown as 'P' on map and has max HP and thirst of 100
     * Spawns with 20 HP and 60 Thirst
     */
    public Pterodactyl() {
        super("Pterodactyl", 'P', 100, 100);
        addCapability(DinosaurStatus.ALLOSAUR_CAN_ATTACK);
        addCapability(DinosaurStatus.SMALL_BODY);
    }

    /**
     * @return Time taken for Pterodactyl Corpse to rot
     */
    @Override
    public int getCorpseRotTime() {
        return 20;
    }

    /**
     * @return HP considered for Pterodactyl to be hungry
     */
    @Override
    public int getHungerThreshold() {
        return 70;
    }

    /**
     * @return Number of turns needed to turn baby Pterodactyl into adult
     */
    @Override
    public int getAdultAge() {
        return 25;
    }

    /**
     * @return Number of turns needed to hatch a Pterodactyl Egg
     */
    @Override
    public int getIncubationPeriod() {
        return 20;
    }

    /**
     * @return Starting HP of adult Pterodactyl
     */
    @Override
    public int getStartingHP() {
        return 50;
    }

    /**
     * @return Starting HP of baby Pterodactyl
     */
    @Override
    public int getBabyStartingHP() {
        return 20;
    }

    /**
     * @return Number of turns allowed for Pterodactyl to be unconscious for due to hunger
     */
    @Override
    public int getHungerUnConsciousThreshold() {
        return 15;
    }

    /**
     * @return Number of turns needed for Pregnant Pterodactyl to carry baby before laying egg
     */
    @Override
    public int getPregnancyLength() {
        return 10;
    }

    /**
     * @return HP considered whether Pterodactyl is well fed or not
     */
    @Override
    public int getWellFeedHunger() {
        return 50;
    }

    /**
     * @return creates a new baby Pterodactyl
     */
    @Override
    public Dinosaur getNewDinosaur() {
        return new Pterodactyl();
    }

    /**
     * @return How much a Pterodactyl Corpse heals when eaten
     */
    @Override
    public int getCorpseHealAmount() {
        return 30;
    }

    /**
     * @return Price of a Pterodactyl Egg in Vending Machine
     */
    @Override
    public int getEggPurchasePrice() {
        return 200;
    }

    /**
     * @return Amount of Eco Points earned when Pterodactyl Egg hatches
     */
    @Override
    public int getEggHatchEcoPoint() {
        return 100;
    }

    /**
     * @return How much a Pterodactyl can drink in one turn
     */
    @Override
    public int getMaxDrinkAmount() {
        return 30;
    }

    /**
     * @return Maximum tiles allowed for Pterodactyl to fly across
     */
    @Override
    public int getMaxFlyingTile() {
        return 30;
    }

    /**
     * @return the Enum that allows Pterodactyl to attack another Dinosaur
     */
    @Override
    public Enum<DinosaurStatus> getAttackableEnum() {
        return DinosaurStatus.PTERODACTYL_CAN_ATTACK;
    }

    /**
     * Describes what happens when Pterodactyl is traversing across this Lake tile.
     * Prints a description if Pterodactyl ate fish, drank water, or both
     * @param waterTile the Lake that the Pterodactyl is on
     */
    public void traverseLake(WaterTile waterTile) {
        String result="";
        Random random = new Random();
        // Pterodactyl can catch 0, 1 or 2 fish
        int fishCount = random.nextInt(3);

        // if Pterodactyl manages to catch fish, heal by certain amount and add description to be printed
        if (fishCount > 0) {
            int healPoints = fishCount*5;
            heal(healPoints);
            result += this + " eats " + fishCount + " fish and heals for " + healPoints + " HitPoints.";
        }

        // if Lake still has water, drink by its max drink amount
        if (waterTile.getSipCapacity() > 0) {
            // this is just to be the whole description look nicer
            if (result.length()>0) {
                result += System.lineSeparator();
            }
            drink(getMaxDrinkAmount());
            waterTile.decreaseSipCount();
            result += this + " drinks from from Lake and increases " + getMaxDrinkAmount() + " water level.";
        }

        // if anything happened, print the description, else don't print
        if (result.length()>0) {
            System.out.println(result);
        }
    }

    /**
     * Pterodactyl's playTurn. First checks if it is on a Lake to eat and drink, then continue with super's playTurn
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return an Action depending on what happens to it.
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);
        if (hasCapability(DinosaurStatus.FLYING) && here.getGround().hasCapability(WaterTileStatus.WATER_TRAVERSE)) {
            traverseLake((WaterTile) here.getGround());
        }
        return super.playTurn(actions, lastAction, map, display);
    }
}
