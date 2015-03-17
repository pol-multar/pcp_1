/**
 * @author mmultari
 * @version 01/03/2015
 */
public class Main {

    private static boolean debug;

    public static void main(String[] args) {

        //Mur en granite

        debug=true;
        SimulationEngineV1 mySimu = new SimulationEngineV1(Material.BRICK,Material.GLASSWOOL);
        if(debug)System.out.println(mySimu);
        mySimu.runYourSimulation(10);
        //mySimu.runWebSimulation();
        //mySimu.runLongSimulation();
//        if(debug) {
//            System.out.println("Après 100 000 cycles :");
//            System.out.println(mySimu);
//        }
        System.out.println("Changement a partir de l'etape "+mySimu.getStepOfChange());
        System.out.println("Temps d'execution de la simulation : "+mySimu.getExecTime()+" ms");

    }
}
