import data.InsulatedWall;
import display.WallView;
import engine.SimulationEngineV1;

/**
 * @author mmultari
 * @version 01/03/2015
 */
public class Main {

    private static boolean debug;

    public static void main(String[] args) {

        debug=false;

        InsulatedWall myWall = new InsulatedWall();
        if(debug)System.out.println(myWall);
        SimulationEngineV1 mySimu = new SimulationEngineV1(myWall);
        //mySimu.runLongSimulation();
        mySimu.runWebSimulation();
        if(debug) {
            System.out.println("Après 100 000 cycles :");
            System.out.println(myWall);
        }

    }
}
