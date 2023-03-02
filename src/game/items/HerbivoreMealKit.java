package game.items;

/**
 * Represents a mealkit item a herbivore can eat.
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @version 1.0
 * @see MealKit
 * @since 05/05/2021
 */
public class HerbivoreMealKit extends MealKit {

    public HerbivoreMealKit() {
        super("Herbivore Meal Kit", 'h');
        addCapability(ItemStats.HERBIVORE_CAN_EAT);
    }

    /**
     * @return Price of the item
     */
    @Override
    public int getPrice() {
        return 100;
    }
}
