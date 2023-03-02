package game.action;

import edu.monash.fit2099.engine.*;
import game.PortableItem;
import game.dinosaur.Dinosaur;
import game.items.Corpse;

/**
 * Special Action that shows a Dinosaur starving to death, or dying from dehydration
 *
 * @author Lin Chen Xiang
 * @see Actor
 * @see edu.monash.fit2099.engine.Location
 * @see game.dinosaur.DinosaurStatus
 * @see GameMap
 * @since 03/05/2021
 */

public class DieFromNaturalCausesAction extends Action {

    private String cause;

    /**
     * Constructor
     * @param cause String that describes how actor died
     */
    public DieFromNaturalCausesAction(String cause) {
        this.cause = cause;
    }

    /**
     * Removes Actor and creates a Corpse
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return description that actor died from hunger or thirst
     */

    @Override
    public String execute(Actor actor, GameMap map) {
        PortableItem corpse;
        if (actor instanceof Dinosaur){
            Dinosaur dinosaur = ((Dinosaur)actor);
            corpse = new Corpse(dinosaur.toString(), dinosaur.getCorpseRotTime(), dinosaur.getCorpseHealAmount());
        } else {
            // Backup code just in case it isn't a dinosaur
            corpse = new Corpse(actor.toString(), 20, 20);
        }
        map.locationOf(actor).addItem(corpse);

        Actions dropActions = new Actions();
        for (Item item : actor.getInventory())
            dropActions.add(item.getDropAction());
        for (Action drop : dropActions)
            drop.execute(actor, map);
        map.removeActor(actor);

        return actor + " " + cause + ".";
    }

    /**
     *
     * @param actor The actor performing the action.
     * @return description that dinosaur died from hunger or thirst
     */

    @Override
    public String menuDescription(Actor actor) {
        return actor + " " + cause + ".";
    }
}
