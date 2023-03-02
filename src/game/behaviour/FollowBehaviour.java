package game.behaviour;

import edu.monash.fit2099.engine.*;

/**
 * A class that figures out a MoveAction that will move the actor one step 
 * closer to a target Actor, or does something when it reaches the target
 *
 * @author Lin Chen Xiang
 * @see Behaviour
 * @see Actor
 * @see Action
 * @see GameMap
 * @see Location
 * @since 03/05/2021
 */
public class FollowBehaviour extends MovingBehaviour {

	/**
	 * Target we are following
	 */
	private Actor target;
	/**
	 * Action we are going to perform after we next to the target.
	 */
	private Action action;
	/**
	 * Used to refer to this object, used in overwriting getNextAction
	 */
	private Behaviour self = this;

	/**
	 * Constructor.
	 *
	 * @param subject the Actor to follow
	 * @param action  the Action to be done when reached target
	 */
	public FollowBehaviour(Actor subject, Action action) {
		this.target = subject;
		this.action = action;
	}

	/**
	 * Decides the current Action for this Actor
	 *
	 * @param actor the Actor acting
	 * @param map   the GameMap containing the Actor
	 * @return MoveActorAction if Actor has not reached target, or action if Actor reached target
	 */

	@Override
	public Action getAction(Actor actor, GameMap map) {
		if (!map.contains(target) || !map.contains(actor))
			return null;

		Location here = map.locationOf(actor);
		Location there = map.locationOf(target);

		int currentDistance = distance(here, there);
		for (Exit exit : here.getExits()) {
			Location destination = exit.getDestination();
			if (destination.canActorEnter(actor)) {
				int newDistance = distance(destination, there);
				if (newDistance < currentDistance) {
					// actor not beside target, continue to follow
					return (new MoveActorAction(destination, exit.getName()) {
						@Override
						public Action getNextAction() { // override next action as current action to chain follow
							return self.getAction(actor, map);
						}
					});
				}
			}
		}

		if (currentDistance > 1) {
			// actor not beside target, continue to follow
			return (new DoNothingAction() {
				@Override
				public Action getNextAction() { // override next action as current action to chain follow
					return self.getAction(actor, map);
				}
			});
		} else
			return action; // actor is already beside target, do action
	}
}

