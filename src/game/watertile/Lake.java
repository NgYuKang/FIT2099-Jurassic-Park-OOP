package game.watertile;


/**
 * Represents a lake.
 * @author NgYuKang
 * @version 1.0
 * @see WaterTile
 * @since 13/05/2021
 */
public class Lake extends WaterTile {

    /**
     * Constructor.
     */
    public Lake() {
        super('~', 25, 5, 25);
    }

    /**
     * Runs RNG to see how much we sips we can get when it rains
     */
    @Override
    protected void increaseSipCapacity() {
        double modifier = Math.random() * (0.6 - 0.2) + 0.2;
        sipCapacity += (20 * modifier);
    }

    /**
     * Runs rng to see if we can get a new fish
     */
    @Override
    protected void checkFishGrowth() {
        double chance = Math.random();
        if (chance < 0.6){
            incrementFishCount();
        }
    }

    /**
     *
     * @return Name of the ground tile.
     */
    @Override
    public String toString() {
        return "Lake";
    }
}
