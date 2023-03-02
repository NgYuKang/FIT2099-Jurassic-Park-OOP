package game.items;

/**
 * Used to represent an item that can be bought
 * Implement this to make an item purchasable
 *
 * @author NgYuKang, Amos Leong Zheng Khang
 * @since 05/05/2021
 * @version 1.0
 */
public interface Purchasable {

    /**
     *
     * @return Price of the item
     */
    int getPrice();

}
