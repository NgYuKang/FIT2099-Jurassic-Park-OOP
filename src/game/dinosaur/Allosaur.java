package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.behaviour.PredatorBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Carnivore Allosaur.
 * Allosaurs will continue on its previous actions even if there are
 * nearby corpses or Stegosaurs. They are focused being.
 *
 * @author NgYuKang
 * @version 1.0
 * @see DinosaurStatus
 * @see Gender
 * @see CarnivoreDinosaur
 * @see Dinosaur
 * @since 25/04/2021
 */
public class Allosaur extends CarnivoreDinosaur {

    /**
     * Constructor to initialise an adult Allosaur with a specific gender.
     * All Allosaurs are represented by a 'A' and have max 100 hit points.
     * They should start with 50 hit points.
     *
     * @param gender the gender this Allosaur should have.
     */
    public Allosaur(Enum<Gender> gender) {
        super("Allosaur", 'A', 100, gender, 100);
        behaviourList.add(new PredatorBehaviour());
    }

    /**
     * Constructor to initialise a baby Allosaur with a specific gender and age
     * All Allosaurs are represented by a 'A' and have max 100 hit points.
     * They should start with 10 hit points.
     */
    public Allosaur() {
        super("Allosaur", 'A', 100, 100);
        behaviourList.add(new PredatorBehaviour());
    }

    /**
     * @return Corpse rot time of the Allosaur.
     */
    @Override
    public int getCorpseRotTime() {
        return 20;
    }

    /**
     * @return The HP (hunger) to which the Allosaur is considered hungry.
     */
    @Override
    public int getHungerThreshold() {
        return 50;
    }

    /**
     * @return The age that the Allosaur is considered an adult.
     */
    @Override
    public int getAdultAge() {
        return 50;
    }

    /**
     * @return How long it takes for an Allosaur egg to hatch.
     */
    @Override
    public int getIncubationPeriod() {
        return 50;
    }

    /**
     * @return the HP the ALlosaur will start with.
     */
    @Override
    public int getStartingHP() {
        return 50;
    }

    /**
     * @return Starting HP of a baby Allosaur.
     */
    @Override
    public int getBabyStartingHP() {
        return 20;
    }

    /**
     * @return HP (hunger) that the ALlosaur is considered well fed.
     */
    @Override
    public int getWellFeedHunger() {
        return 50;
    }

    /**
     * @return How long can the Allosaur be unconscious for.
     */
    @Override
    public int getHungerUnConsciousThreshold() {
        return 20;
    }

    /**
     * @return Length of an Allosaur pregnancy
     */
    @Override
    public int getPregnancyLength() {
        return 20;
    }

    /**
     * @return The damage an Allosaur would do.
     */
    @Override
    protected IntrinsicWeapon getIntrinsicWeapon() {
        if (hasCapability(DinosaurStatus.BABY)) {
            return new IntrinsicWeapon(10, "bites");
        } else {
            return new IntrinsicWeapon(20, "bites");
        }
    }

    /**
     * @return A new instance of this dinosaur
     */
    @Override
    public Dinosaur getNewDinosaur() {
        return new Allosaur();
    }

    /**
     * @return How much HP does the corpse of this dinosaur heals
     */
    @Override
    public int getCorpseHealAmount() {
        return 50;
    }

    /**
     * @return How much this dinosaur's egg should cost.
     */
    @Override
    public int getEggPurchasePrice() {
        return 1000;
    }

    /**
     * @return How much eco points are gained when a dinosaur of this kind hatches.
     */
    @Override
    public int getEggHatchEcoPoint() {
        return 1000;
    }

    /**
     *
     * @return How much the dinosaur can drink in one go
     */
    @Override
    public int getMaxDrinkAmount() {
        return 30;
    }

    /**
     * @return the Enum that allows Allosaur to attack another Dinosaur
     */
    @Override
    public Enum<DinosaurStatus> getAttackableEnum() {
        return DinosaurStatus.ALLOSAUR_CAN_ATTACK;
    }

}
