package game.items;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import game.PortableItem;

import game.dinosaur.Dinosaur;
import game.dinosaur.Stegosaur;

/**
 * Represents en egg
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @version 1.0
 * @see Purchasable
 * @see EdibleItem
 * @since 05/05/2021
 */
public class Egg extends EdibleItem implements Purchasable {

    private Dinosaur baby;
    private int incubationTime;

    /**
     * Constructor
     *
     * @param dinosaur The baby dinosaur in the egg.
     */
    public Egg(Dinosaur dinosaur) {
        super((dinosaur.toString()) + " egg", 'O');
        baby = dinosaur;
        addCapability(ItemStats.CARNIVORE_CAN_EAT);
    }

    /**
     * Inform the egg that time has passed. Time to grow up.
     *
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        incubationTime++;
        if (incubationTime >= baby.getIncubationPeriod()) {
            if (!(currentLocation.containsAnActor())) {
                currentLocation.map().addActor(baby, currentLocation);
                currentLocation.removeItem(this);
            }

        }

    }

    /**
     * @return A new copy with a new dinosaur
     */
    public Egg getNewCopy() {
        return new Egg(baby.getNewDinosaur());
    }


    /**
     * @return Price of the item
     */
    @Override
    public int getPrice() {
        return baby.getEggPurchasePrice();
    }

    /**
     * Returns how much this item can heal the target depending on its stats.
     * Enter null if checking player feeding dinosaur.
     * Some item will return the same amount regardless of actor since it is a fixed int.
     *
     * @param actor Target
     * @return heal amount
     */
    @Override
    public int getHealAmount(Actor actor) {
        return 10;
    }
}