package game.growable;

import edu.monash.fit2099.engine.Location;
import game.PortableItem;
import game.VendingMachine;
import game.items.EdibleItem;
import game.items.Fruit;

/**
 * Represents a Tree.
 *
 * @author NgYuKang
 * @version 1.0
 * @see DroppableFruitGrowable
 * @since 25/04/2021
 */
public class Tree extends DroppableFruitGrowable {
    /**
     * Age of a tree.
     */
    private int age = 0;

    /**
     * Constructor.
     * New tree starts with character +.
     * As they grow, it will turn into t, then T.
     * Has TALL enum, to be used by behaviours or actions.
     */
    public Tree() {
        super('+');
        addCapability(GrowableStatus.TALL);
        addCapability(GrowableStatus.OBSTRUCT_GROWTH);
    }

    /**
     * @return Chance that a fruit will grow every turn in a tree.
     */
    @Override
    public double growFruitChance() {
        return 0.2;
    }

    /**
     * @return Chance that a fruit will drop from a tree every turn.
     */
    @Override
    public double dropFruitChance() {
        return 0.05;
    }


    /**
     * Used to check if a Fruit would grow.
     */
    @Override
    protected boolean checkGrowFruit() {
        boolean res = super.checkGrowFruit();
        if (res) {
            VendingMachine.increaseEcoPoint(1);
        }
        return res;
    }

    /**
     * And now the tree will grow older.
     * Informs the tree the passage of time so that it will run its method every turn.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        super.tick(location);
        age++;
        if (age == 10)
            displayChar = 't';
        if (age == 20)
            displayChar = 'T';
    }

    /**
     *
     * @return Name of the tree. Tree.
     */
    @Override
    public String toString() {
        return "Tree";
    }
}
