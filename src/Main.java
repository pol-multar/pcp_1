/**
 * @author mmultari
 * @version 01/03/2015
 */
public class Main {

    private static boolean debug;

    public static void main(String[] args) {

        Simulation mySimu;

        if(args.length==1){
            debug = true;
        }else{
            debug = false;
        }
        mySimu = new Simulation(100000,debug);
        //if (debug) System.out.println(mySimu);
        mySimu.multithreadSimulation();

    }
}
