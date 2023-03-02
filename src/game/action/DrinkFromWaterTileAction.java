package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.dinosaur.Dinosaur;
import game.watertile.WaterTile;

public class DrinkFromWaterTileAction extends Action {

    /**
     * The tile we are thinking from.
     */
    private WaterTile waterTile;

    /**
     * Constructor.
     *
     * @param waterTile Tile to drink from
     */
    public DrinkFromWaterTileAction(WaterTile waterTile) {
        this.waterTile = waterTile;
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action.
     * @param map   The map the actor is on.
     * @return a description of what happened that can be displayed to the user.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (waterTile.getSipCapacity() > 0) {
            waterTile.decreaseSipCount();
            Dinosaur dinosaur = ((Dinosaur) actor);
            dinosaur.drink(dinosaur.getMaxDrinkAmount());
            return actor.toString() + " drinks from " + waterTile.toString() + " for " +
                    dinosaur.getMaxDrinkAmount() + "" + " water level";
        } else {
            return actor.toString() + "couldn't drink from " + waterTile.toString() +
                    " as it is already empty by the " + "time it got there";
        }
    }

    /**
     * Returns a descriptive string
     *
     * @param actor The actor performing the action.
     * @return the text we put on the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor.toString() + "drinks from " + waterTile.toString();
    }
}
