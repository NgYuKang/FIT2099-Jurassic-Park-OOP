package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.PortableItem;
import game.dinosaur.Dinosaur;
import game.dinosaur.DinosaurStatus;
import game.items.EdibleItem;
import game.items.Egg;

/**
 * Special Action that allows a Dinosaur to lay an Egg.
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see Location
 * @see Egg
 * @see Dinosaur
 * @see DinosaurStatus
 * @see GameMap
 * @since 03/05/2021
 */

public class LayEggAction extends Action {

    /**
     * Creates an Egg of that Dinosaur type and removes PREGNANT enum from Dinosaur
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description stating that the dinosaur laid an egg
     */

    @Override
    public String execute(Actor actor, GameMap map) {

        Location here = map.locationOf(actor);
        if (!(actor.hasCapability(DinosaurStatus.PREGNANT))) {
            return actor + " cannot lay eggs.";
        }
        Dinosaur dinosaur = ((Dinosaur)actor);
        PortableItem egg = new Egg(dinosaur.getNewDinosaur());
        here.addItem(egg);

        actor.removeCapability(DinosaurStatus.PREGNANT); // no longer pregnant
        return actor + " lays an egg.";
    }

    /**
     *
     * @param actor The actor performing the action.
     * @return description about actor laying an egg
     */

    @Override
    public String menuDescription(Actor actor) {
        return actor + " lays an egg.";
    }
}
