import data.InsulatedWall;
import engine.SimulationEngineV1;

/**
 * @author mmultari
 * @version 01/03/2015
 */
public class Main {

    public static void main(String[] args) {

        InsulatedWall myWall = new InsulatedWall();
        System.out.println(myWall);
        SimulationEngineV1 mySimu = new SimulationEngineV1(myWall);
        mySimu.runLongSimulation();
        System.out.println("Après 100 000 cycles :");
        System.out.println(myWall);
    }
}
