package game.growable;

/**
 * Collection of Enums that are used by Growable's extended classes.
 * Will be helpful in behaviours or actions, since all you need to do
 * is to check these enums, instead of checking if it is of a certain class.
 * Many other class may use this.
 *
 * @author NgYuKang
 * @version 1.0
 * @see Growable
 * @see Tree
 * @see Bush
 * @since 25/04/2021
 */
public enum GrowableStatus {
    SHORT,
    TALL,
    OBSTRUCT_GROWTH,
    ENCOURAGE_GROWTH,
    FRAGILE,
    DIRT
}
