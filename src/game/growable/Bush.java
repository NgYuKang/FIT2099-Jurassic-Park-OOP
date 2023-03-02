package game.growable;

/**
 * Represents a bush.
 *
 * @author NgYuKang
 * @version 1.0
 * @see Growable
 * @since 25/04/2021
 */
public class Bush extends Growable {

    /**
     * Constructor.
     * Bush will have a display character of w.
     * Has SHORT enum, to be used by behaviours or actions
     */
    public Bush() {
        super('w');
        addCapability(GrowableStatus.SHORT);
        addCapability(GrowableStatus.ENCOURAGE_GROWTH);
    }

    /**
     * @return chance that a fruit will grow every turn in a bush.
     */
    @Override
    public double growFruitChance() {
        return 0.1;
    }

    /**
     * @return Name of this growable
     */
    @Override
    public String toString() {
        return "Bush";
    }

}
