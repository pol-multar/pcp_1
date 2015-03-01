import com.sun.org.apache.xpath.internal.SourceTree;
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

        debug=true;

        InsulatedWall myWall = new InsulatedWall();
        if(debug)System.out.println(myWall);
        WallView myWin0 = new WallView(myWall.getWallParts(),0);
        SimulationEngineV1 mySimu = new SimulationEngineV1(myWall);
        mySimu.runLongSimulation();
        //mySimu.runWebSimulation();
        WallView myWin100000 = new WallView(myWall.getWallParts(),100000);
        if(debug) {
            System.out.println("Après 100 000 cycles :");
            System.out.println(myWall);
            System.out.println("Changement a partir de l'etape "+mySimu.getStepOfChange());
            System.out.println("Temps d'execution de la simulation : "+mySimu.getExecTime()+" ms");
        }

    }
}
