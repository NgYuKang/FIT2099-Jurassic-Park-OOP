package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Menu;
import game.action.PickFruitAction;
import game.action.QuitGameAction;
import game.growable.Growable;

/**
 * Class representing the Player.
 *
 * @author NgYuKang
 * @author Amos Leong Zheng Khang
 * @see QuitGameAction
 * @see GameMap
 * @see Action
 * @see Display
 * @see Menu
 * @see PickFruitAction
 * @since 22/05/2021
 */
public class Player extends Actor {

    /**
     * Menu for the player to choose from
     */
    private Menu menu = new Menu();
    /**
     *
     */
    private int targetTurn = 0;
    private int targetPoint = 0;
    private boolean challengeMode = false;
    private int turnTimer = 0;

    /**
     * Constructor.
     *
     * @param name        Name to call the player in the UI
     * @param displayChar Character to represent the player in the UI
     * @param hitPoints   Player's starting number of hitpoints
     */
    public Player(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
    }

    /**
     * Specialised constructor for application driver
     *
     * @param mode        mode of the game we going to play. False for sandbox, True for challenge.
     * @param targetTurn  target turn for challenge mode. Ignored if mode is false.
     * @param targetPoint target turn for challenge mode. Ignroed if mode is false.
     */
    public Player(boolean mode, int targetTurn, int targetPoint) {
        super("Player", '@', 100);
        this.targetTurn = targetTurn;
        this.targetPoint = targetPoint;
        this.challengeMode = mode;
    }

    /**
     * Let the player have its turn
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return Action the player is taking
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        // Turn related stuff
        turnTimer++;
        actions.add(new QuitGameAction());
        display.println(String.format("Turn %s", turnTimer));
        display.println(String.format("Current EcoPoints: %s", VendingMachine.getEcoPoint()));
        // Challenge mode check
        if (challengeMode) {
            if (turnTimer >= targetTurn) {
                display.println("Challenge timer up!");
                if (VendingMachine.getEcoPoint() >= targetPoint) {
                    display.println("You have achieved your target. You win the game!");
                } else {
                    display.println("You have not achieved your target. You lose!");
                }
                return new QuitGameAction();
            }
        }

        // Pick fruit
        if (map.locationOf(this).getGround() instanceof Growable) {
            Growable growable = (Growable) map.locationOf(this).getGround();
            if (growable.getNumberOfRipeFruit() > 0) {
                actions.add(new PickFruitAction(growable));
            }
        }

        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        // Show menu
        return menu.showMenu(this, actions, display);
    }
}
