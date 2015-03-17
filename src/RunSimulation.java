import java.util.concurrent.BrokenBarrierException;

/**
 * @author mmultari
 * @version 17/03/2015
 */
public class RunSimulation implements Runnable {

    private int partNb;
    private SimulationEngine simulation;

    public RunSimulation(SimulationEngine simulation,int number){
        this.partNb=number;
        this.simulation=simulation;
    }

    @Override
    public void run() {

        double currentTemp=simulation.getCurrentTemp(partNb);
        double newTemp;

        if(partNb<5){
            newTemp=simulation.updateWallPartTemp(simulation.getCurrentTemp(partNb-1),simulation.getCurrentTemp(partNb),simulation.getCurrentTemp(partNb+1),simulation.getWallC());
        }else if(partNb>5){
            newTemp=simulation.updateWallPartTemp(simulation.getCurrentTemp(partNb-1),simulation.getCurrentTemp(partNb),simulation.getCurrentTemp(partNb+1),simulation.getInsulationC());
        }else{
            newTemp=simulation.getCurrentTemp(partNb)+simulation.getWallC()*(simulation.getCurrentTemp(partNb-1)-simulation.getCurrentTemp(partNb))+simulation.getInsulationC()*(simulation.getCurrentTemp(partNb+1)-simulation.getCurrentTemp(partNb));
        }

        try {
            simulation.getBarrier().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //Puis on envoie la modifs


    }
}
