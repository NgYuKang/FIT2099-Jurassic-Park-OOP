package game.items;

import edu.monash.fit2099.engine.WeaponItem;

/**
 * Represents a Laser gun
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @version 1.0
 * @see WeaponItem
 * @since 05/05/2021
 */
public class LaserGun extends WeaponItem implements Purchasable {
    /**
     * Constructor.
     */
    public LaserGun() {
        super("Laser Gun", 'F', 160, "zaps");
        addCapability(ItemStats.IS_WEAPON);
    }

    /**
     * @return Price of the item
     */
    @Override
    public int getPrice() {
        return 500;
    }
}
