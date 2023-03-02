package game.action;

import edu.monash.fit2099.engine.*;
import game.dinosaur.*;

/**
 * Special Action that attacks an Actor, and heals the attacker Actor
 *
 * @author Lin Chen Xiang
 * @see game.behaviour.PredatorBehaviour
// * @see carnihungerbehaviour
 * @see Actor
 * @see Allosaur
 * @see AttackAction
 * @see GameMap
 * @since 03/05/2021
 */

public class EatPreyAction extends AttackAction {

    /**
     * Constructor.
     *
     * @param target the Actor to attack
     */
    public EatPreyAction(Actor target) {
        super(target);
    }

    /**
     * Attacks an actor and heals by its damage dealt
     * @param actor the Allosaur attacking
     * @param map the current world map
     * @return description of what happened during execute (missed, killed target etc.)
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        String[] output;
        String result;
        Enum<DinosaurStatus> capability = ((CarnivoreDinosaur) actor).getAttackableEnum();

        // check if actor and target are correct types
        if (!(target.hasCapability(capability)) || !(target.hasCapability(DinosaurStatus.ON_LAND))) {
            return actor + " cannot attack " + target;
        }

        // if target has a small body, actor can eat it whole and heal fully without missing
        if (target.hasCapability(DinosaurStatus.SMALL_BODY)) {
            actor.heal(Integer.MAX_VALUE);
            result = actor + " swallowed " + target;
            map.removeActor(target);
            return result;
        }

        // Allosaur tries attacking target
        result = super.execute(actor, map);
        output = result.split(" ");

        // missed, nothing else happens
        if (output[1].equals("misses")) {
            return result;
        }

        Weapon weapon = actor.getWeapon();
        int healPoints = weapon.damage();

        // Allosaur heals by same amount of damage dealt to target
        actor.heal(healPoints);
        result += System.lineSeparator() + actor + " heals for " + healPoints + " HitPoints.";

        // target Dinosaur survives, add to hashMap to ensure Allosaur can't attack it for 20 turns
        if (target.isConscious()) {
            ((CarnivoreDinosaur)actor).insertDinosaurAttacked((Dinosaur) target);
        }

        return result;
    }
}
