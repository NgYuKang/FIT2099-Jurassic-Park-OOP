package game.dinosaur;


import java.util.Random;

/**
 * A herbivorous dinosaur.
 * Represents a Stegosaur.
 *
 * @author NgYuKang
 * @version 1.0
 * @see DinosaurStatus
 * @see Gender
 * @see HerbivoreDinosaur
 * @see Dinosaur
 * @since 25/04/2021
 */
public class Stegosaur extends HerbivoreDinosaur {
    // Will need to change this to a collection if Stegosaur gets additional Behaviours.

    /**
     * Constructor to initialise an adult Stegosaur with a specific gender.
     * All Stegosaurs are represented by a 'd' and have 100 max hit points.
     * They should start with 50 hit points.
     *
     * @param gender the gender this dinosaur should have.
     */
    public Stegosaur(Enum<Gender> gender) {
        super("Stegosaur", 's', 100, gender, 100);
        addCapability(DinosaurStatus.ALLOSAUR_CAN_ATTACK);
        addCapability(DinosaurStatus.SHORT_NECK);
    }

    /**
     * Constructor to initialise a baby stegosaur with randomised gender.
     * Starts with 10 hp.
     */
    public Stegosaur() {
        super("Stegosaur", 's', 100, 100);
        addCapability(DinosaurStatus.ALLOSAUR_CAN_ATTACK);
        addCapability(DinosaurStatus.SHORT_NECK);
    }

    /**
     * @return How long a Stegosaur corpse should stay before disappearing.
     */
    @Override
    public int getCorpseRotTime() {
        return 20;
    }

    /**
     * @return The HP(Hunger) of the Stegosaur that is considered it being hungry
     */
    @Override
    public int getHungerThreshold() {
        return 90;
    }

    /**
     * @return The age that a Stegosaur starts being an adult.
     */
    @Override
    public int getAdultAge() {
        return 30;
    }

    @Override
    public int getIncubationPeriod() {
        return 15;
    }

    /**
     * @return The HP an adult Stegosaur should start with.
     */
    @Override
    public int getStartingHP() {
        return 50;
    }

    /**
     * @return HP that a baby Stegosaur should start with.
     */
    @Override
    public int getBabyStartingHP() {
        return 15;
    }

    /**
     * @return The HP(Hunger) where a Stegosaur is considered well fed.
     */
    @Override
    public int getWellFeedHunger() {
        return 50;
    }

    /**
     * @return How long can a Stegosaur be unconscious for.
     */
    @Override
    public int getHungerUnConsciousThreshold() {
        return 20;
    }

    /**
     * @return Length of a Stegosaur pregnancy.
     */
    @Override
    public int getPregnancyLength() {
        return 10;
    }

    /**
     * @return A new instance of this dinosaur
     */
    @Override
    public Dinosaur getNewDinosaur() {
        return new Stegosaur();
    }

    /**
     * @return How much HP does the corpse of this dinosaur heals
     */
    @Override
    public int getCorpseHealAmount() {
        return 50;
    }

    /**
     *
     * @return How much this dinosaur's egg should cost.
     */
    @Override
    public int getEggPurchasePrice() {
        return 200;
    }

    /**
     * @return How much eco points are gained when a dinosaur of this kind hatches.
     */
    @Override
    public int getEggHatchEcoPoint() {
        return 100;
    }

    /**
     *
     * @return How much the dinosaur can drink in one go
     */
    @Override
    public int getMaxDrinkAmount() {
        return 30;
    }

}
