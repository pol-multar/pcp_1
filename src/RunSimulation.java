import java.util.concurrent.BrokenBarrierException;

/**
 * @author mmultari
 * @version 17/03/2015
 */
public class RunSimulation implements Runnable {

    private int partNb;
    private SimulationEngine simulation;
    private int stepsNumber;

    public RunSimulation(SimulationEngine simulation, int number, int steps) {
        this.partNb = number;
        this.simulation = simulation;
        this.stepsNumber = steps;
    }

    @Override
    public void run() {
        int cpt=0;
        int execTime=0;
        //System.out.println("A l'interieur du thread responsable de la partie "+partNb+" avant le for");

        for(cpt=0;cpt < stepsNumber;cpt++) {
            double newTemp;
            long timeBegin = System.currentTimeMillis();

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
                long timeEnd = System.currentTimeMillis();
                execTime += (int) (timeEnd - timeBegin);
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
