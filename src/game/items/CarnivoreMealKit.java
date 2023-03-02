package game.items;

/**
 * Represents a meal that Carnivore can eat
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @see MealKit
 * @since 05/05/2021
 * @version 1.0
 */
public class CarnivoreMealKit extends MealKit {


    /**
     * Constructor.
     *
     */
    public CarnivoreMealKit() {
        super("Carnivore Meal Kit", 'c');
        addCapability(ItemStats.CARNIVORE_CAN_EAT);
    }

    /**
     * @return Price of the item
     */
    @Override
    public int getPrice() {
        return 500;
    }
}
