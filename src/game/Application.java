package game;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.monash.fit2099.engine.*;
import game.dinosaur.Brachiosaur;
import game.dinosaur.Gender;
import game.dinosaur.Stegosaur;
import game.growable.Bush;
import game.growable.Tree;
import game.watertile.Lake;
import game.watertile.WaterTile;

/**
 * The main class for the Jurassic World game.
 */
public class Application {

    /**
     * Prompts the user for an integer input
     *
     * @param type What to prompt the user for
     * @return Valid integer the user has entered
     */
    public static int getIntUserInput(String type) {
        Scanner scanner = new Scanner(System.in);
        int returnVal = -1;
        String prompt = "Enter " + type + ": ";
        boolean flag = false;
        do {
            try {
                System.out.print(prompt);
                returnVal = scanner.nextInt();
                flag = true;
            } catch (Exception e) {
                System.out.println("Invalid input, please try again");
            } finally {
                scanner.nextLine();
            }
        } while (!flag);
        return returnVal;
    }

    public static void main(String[] args) {
        // Loop while the user hasn't quit
        while (true) {
            System.out.println("----------------------------------------");
            System.out.println("|     Welcome to Jurassic Park Game    |");
            System.out.println("----------------------------------------");
            System.out.println("Select Game Mode:");
            System.out.println("(1) Sandbox");
            System.out.println("(2) Challenge mode");
            System.out.println("(3) Quit");
            // Select mode
            int mode;
            do {
                mode = getIntUserInput("Please enter game mode");
            } while ((mode != 1) && (mode != 2) && (mode != 3));
            // Quit if selected 3
            if (mode == 3) {
                break;
            }
            int targetTurn = 0;
            int targetPoint = 0;
            // Get target turn and point
            if (mode == 2) {
                do {
                    targetTurn = getIntUserInput("Please enter target turn");
                } while (targetTurn <= 0);
                do {
                    targetPoint = getIntUserInput("Please enter target point");
                } while (targetPoint <= 0);
            }
            // Prepare boolean for player
            boolean modeBoolean = false;
            if (mode == 1) {
                modeBoolean = false;
            } else if (mode == 2) {
                modeBoolean = true;
            }

            // Init game
            World world = new World(new Display());

            FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(),
                    new VendingMachine(), new Bush(), new Lake());

            // first map
            List<String> map1 = Arrays.asList(
                    "................................................................................",
                    "................................................................................",
                    ".....#######.................................................~~~~~~~~~..........",
                    ".....#_____#.................................................~~~~~~~~~..........",
                    ".....#____V#.................................................~~~~~~~~~..........",
                    ".....###.###.................................................~~~~~~~~~..........",
                    ".............................................................~~~~~~~~~..........",
                    "......................................+++.......................................",
                    ".......................................++++.....................................",
                    "...................................+++++........................................",
                    ".....................................++++++.....................................",
                    "......................................+++.......................................",
                    ".....................................+++........................................",
                    "................................................................................",
                    "............+++.................................................................",
                    ".............+++++.....................ww.......................................",
                    "...............++.....................ww.................+++++..................",
                    ".............+++....................................++++++++....................",
                    "............+++.......................................+++.......................",
                    "................................................................................",
                    "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~.................................++.....",
                    "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~..................................++.++...",
                    "..............+..........................................................++++...",
                    ".......++.+.........................~~~~..................................++....",
                    ".........+..........................~~~~........................................");

            // second map
            List<String> map2 = Arrays.asList(
                    "................................................................................",
                    "..........................~...........................~.........................",
                    "...........................~.........................~.................+..+.....",
                    "...................~++~.....~.......................~.....~++~............++....",
                    "..++~~+...........~~++~~.....~.....................~.....~~++~~............w....",
                    "....~+..........~~......~~.............................~~......~~.........w.....",
                    "................++..ww..++.............................++..ww..++...............",
                    "................~~......~~.............................~~......~~...............",
                    "..................~~++~~.................................~~++~~.................",
                    "...................~++~...................................~++~..................",
                    "...+++........................~~.......~~~.......~~.............................",
                    "~~~~~~w........................~~.....~~.~~.....~~..............................",
                    "~~~~~~+.........................~~...~~...~~...~~........................w......",
                    "~~~~~~++.........................~~.~~.....~~.~~......................w++.......",
                    "~~~~~~++..........................~~~.......~~~.........................+.......",
                    ".++++................+..........................................................",
                    "..................+..+.+........................................................",
                    "...................+..+++.....................................+~~~~~~~~~~.......",
                    "......................++.....................................++~~~~+............",
                    "#######.........................................................~~~++...........",
                    "#_____#..................................ww.......+++++........~~...............",
                    "#V_____.................................~~~~........++++........................",
                    "#_____#...............................~~~~~~~~.......++.........................",
                    "#######.............................~~~~~~~~~~~~....++++........................",
                    "................................................................................");
//		GameMap gameMap = new GameMap(groundFactory, map );
            JurassicParkGameMap gameMap1 = new JurassicParkGameMap(groundFactory, map1);
            world.addGameMap(gameMap1);
            JurassicParkGameMap gameMap2 = new JurassicParkGameMap(groundFactory, map2);
            world.addGameMap(gameMap2);

            gameMap1.addConnectingGameMap(gameMap2, 8);

            Actor player = new Player(modeBoolean, targetTurn, targetPoint);
            world.addPlayer(player, gameMap1.at(9, 0));


            // Place a pair of stegosaurs in the middle of the map
            gameMap1.at(30, 12).addActor(new Stegosaur(Gender.MALE));
            gameMap1.at(32, 12).addActor(new Stegosaur(Gender.FEMALE));
            // Place a pair of Brachiosaur
            gameMap1.at(20, 12).addActor(new Brachiosaur(Gender.MALE));
            gameMap1.at(19, 12).addActor(new Brachiosaur(Gender.FEMALE));

            world.run();
        }

    }
}
