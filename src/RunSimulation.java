import java.util.concurrent.BrokenBarrierException;

/**
 * Classe chargée d'implémenter les calculs en multithread avec une barrière
 * @author mmultari
 * @version 17/03/2015
 */
public class RunSimulation implements Runnable {

    //Le numero du mur dont est charge le thread
    private int partNb;
    //Les donnes de la simulation
    private SimulationEngine simulation;
    //Le nombre d etape de la simulation
    private int stepsNumber;

    /**
     * Le constructeur de la classe
     * @param simulation les données de la simulation
     * @param number le numéro associé au thread
     * @param steps le nombre de cycles de la simulation
     */
    public RunSimulation(SimulationEngine simulation, int number, int steps) {
        this.partNb = number;
        this.simulation = simulation;
        this.stepsNumber = steps;
    }

    /**
     * La méthode principale du thread
     */
    @Override
    public void run() {
        int cpt=0;
        int execTime=0;
        double newTemp;
        long timeBegin;

        for(cpt=0; cpt < stepsNumber; cpt++) {

            //Je recupere l'heure de debut de ma methode
            timeBegin = System.currentTimeMillis();

            //Je calcule la nouvelle temperature de la partie du mur en fonction de sa position dans le tableau
            if (partNb < 5) {
                newTemp = simulation.updateWallPartTemp(simulation.getCurrentTemp(partNb - 1), simulation.getCurrentTemp(partNb), simulation.getCurrentTemp(partNb + 1), simulation.getWallC());
            } else if (partNb > 5) {
                newTemp = simulation.updateWallPartTemp(simulation.getCurrentTemp(partNb - 1), simulation.getCurrentTemp(partNb), simulation.getCurrentTemp(partNb + 1), simulation.getInsulationC());
            } else {
                newTemp = simulation.getCurrentTemp(partNb) + simulation.getWallC() * (simulation.getCurrentTemp(partNb - 1) - simulation.getCurrentTemp(partNb)) + simulation.getInsulationC() * (simulation.getCurrentTemp(partNb + 1) - simulation.getCurrentTemp(partNb));
            }

            /* Si je suis la derniere couche et que ma nouvelle temperature vaut 20
             * Alors je le notifie a la simulation et je sauvegarde le numero de l'etape dans laquelle je me trouve
             */
            if(partNb==7){
                if(SimulationEngine.toInt(newTemp)>20 && simulation.isChanged()==false){
                    simulation.setChanged(true);
                    simulation.setStepOfChange(cpt);
                }
            }

            /* J ai fini mon calcul, j'attends que les autres threads aient finies elles aussi
             * a l'aide de la cyclicBarrier de la simulation
             */

            try {
                simulation.getBarrier().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            //Je met a jour la temperature dans le tableau de la simulation

            simulation.updateCurrentTemp(newTemp, partNb);

            //Je met a jour le numero d etape de la simulation

            if(simulation.getSimulationStepActualNumber()<cpt)simulation.setSimulationStepActualNumber(cpt);


            if(partNb==1) {
                execTime += (int) (System.currentTimeMillis() - timeBegin);
                simulation.setExecTime(execTime);
            }

            //System.out.println(partNb+" : J ai fini la modification, attente des autres threads");
            try {
                simulation.getBarrier().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }


    }
}
