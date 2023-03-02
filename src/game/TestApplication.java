package game;
import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.FancyGroundFactory;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.World;
import game.*;
import game.dinosaur.*;
import game.growable.Bush;
import game.growable.Tree;
import game.items.Corpse;
import game.watertile.Lake;

/**
 * The main class for the Jurassic World game.
 *
 */
public class TestApplication {

    public static void main(String[] args) {
        World world = new World(new Display());

        FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(),
                new VendingMachine(), new Bush(), new Lake());

        List<String> map = Arrays.asList(
                "............+++......",
                "..~~.........+++++...",
                "~~~V...........++....",
                ".....~~~.....+++.....",
                "............+++.......",
                "......................");
        List<String> map2 = Arrays.asList(
                ".....................",
                ".............+++++...",
                "...........~...++....",
                ".............+++.....",
                "......................",
                "V.....................");
//		GameMap gameMap = new GameMap(groundFactory, map );
        GameMap gameMap = new JurassicParkGameMap(groundFactory, map);
        GameMap gameMap2 = new JurassicParkGameMap(groundFactory, map2);
        world.addGameMap(gameMap);
        world.addGameMap(gameMap2);
        ((JurassicParkGameMap)gameMap).addConnectingGameMap(gameMap2,8);

        Actor player = new Player("Player", '@', 100);
        world.addPlayer(player, gameMap.at(3, 3));

        // Place a pair of stegosaurs in the middle of the map
//        Brachiosaur s1 = new Brachiosaur(Gender.MALE);
//        Brachiosaur s2 = new Brachiosaur(Gender.FEMALE);
//        Stegosaur s1 = new Stegosaur(Gender.MALE);
//        Stegosaur s2 = new Stegosaur(Gender.FEMALE);
//        gameMap.at(5, 5).addActor(s1);
//        gameMap.at(0, 1).addActor(s2);
//        s1.heal(10000);
//        s2.heal(10000);
//        gameMap.at(1,1).addActor(new Allosaur(Gender.MALE));
        // Place a pair of Brachiosaur
//        gameMap.at(20,12).addActor(new Brachiosaur("Brachiosaur", Gender.MALE));
//        gameMap.at(19,12).addActor(new Brachiosaur("Brachiosaur", Gender.FEMALE));
        // test allosaurs
//        gameMap.at(2,2).addActor(new Allosaur(Gender.MALE));
//        s1.heal(200);
//        s2.heal(200);
        Pterodactyl p1 = new Pterodactyl(Gender.MALE);
        Pterodactyl p2 = new Pterodactyl(Gender.FEMALE);
        gameMap.at(0,0).addActor(p1);
        gameMap.at(0,1).addActor(p2);
        gameMap.at(3,4).addItem(new Corpse("A corpse", 40, 40));
        p1.removeCapability(DinosaurStatus.CAN_FLY);
        p1.addCapability(DinosaurStatus.ON_LAND);
        p1.heal(1000);
        p2.heal(1000);
//        Pterodactyl p3 = new Pterodactyl(Gender.MALE);
//        p3.removeCapability(DinosaurStatus.CAN_FLY);
//        p3.addCapability(DinosaurStatus.ON_LAND);
//        gameMap.at(0,0).addActor(p3);
////        Allosaur a1 = new Allosaur((Gender.FEMALE));
////        gameMap.at(0,1).addActor(a1);

        world.run();
    }
}
