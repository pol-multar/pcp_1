import java.util.concurrent.BrokenBarrierException;

/**
 * Classe chargée d'implémenter les calculs en multithread avec une barrière
 * @author mmultari
 * @version 17/03/2015
 */
public class RunSimulation implements Runnable {

    private int partNb;
    private SimulationEngine simulation;
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
        //System.out.println("A l'interieur du thread responsable de la partie "+partNb+" avant le for");

        for(cpt=0;cpt < stepsNumber;cpt++) {

            timeBegin = System.currentTimeMillis();

            //System.out.println(partNb+" debut de l etape "+cpt);
            if (partNb < 5) {
                newTemp = simulation.updateWallPartTemp(simulation.getCurrentTemp(partNb - 1), simulation.getCurrentTemp(partNb), simulation.getCurrentTemp(partNb + 1), simulation.getWallC());
            } else if (partNb > 5) {
                newTemp = simulation.updateWallPartTemp(simulation.getCurrentTemp(partNb - 1), simulation.getCurrentTemp(partNb), simulation.getCurrentTemp(partNb + 1), simulation.getInsulationC());
            } else {
                newTemp = simulation.getCurrentTemp(partNb) + simulation.getWallC() * (simulation.getCurrentTemp(partNb - 1) - simulation.getCurrentTemp(partNb)) + simulation.getInsulationC() * (simulation.getCurrentTemp(partNb + 1) - simulation.getCurrentTemp(partNb));
            }

            if(partNb==7){
                if(Constantes.toInt(newTemp)>20 && simulation.isChanged()==false){
                    simulation.setChanged(true);
                    simulation.setStepOfChange(cpt);
                }
            }

            //System.out.println(partNb+" : J ai fini mon calcul, attente des autres threads");

            try {
                simulation.getBarrier().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            //System.out.println(partNb+" : Les autres ont fini, je vais modifier les temperatures");

            simulation.updateCurrentTemp(newTemp, partNb);

            if(simulation.get_t()<cpt)simulation.set_t(cpt);

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
