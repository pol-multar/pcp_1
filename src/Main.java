import java.util.concurrent.BrokenBarrierException;

/**
 * @author mmultari
 * @version 01/03/2015
 */
public class Main {

    private static boolean debug;

    public static void main(String[] args) {

        //Mur en granite

        debug = true;
        SimulationEngine mySimu = new SimulationEngine(Material.BRICK, Material.GLASSWOOL);
        if (debug) System.out.println(mySimu);
        int nbStep=100000;
        mySimu.runMultiThreadSimulation(nbStep);
//mySimu.runLongSimulation();

    }
}
