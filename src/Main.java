import java.util.concurrent.BrokenBarrierException;

/**
 * @author mmultari
 * @version 01/03/2015
 */
public class Main {

    private static boolean debug;


    public static void main(String[] args) {

        SimulationEngine mySimu;
        //Mur en brique

        if(args.length==1){
            debug = true;
        }else{
            debug = false;
        }
        mySimu = new SimulationEngine(Material.BRICK, Material.GLASSWOOL,debug);
        if (debug) System.out.println(mySimu);
        mySimu.runMultiThreadSimulation();
        //mySimu.runMonoThreadSimulation();

    }
}
