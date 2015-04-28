import java.util.concurrent.BrokenBarrierException;

/**
 * @author mmultari
 * @version 10/04/2015
 */
public class RunLayer extends Thread {

    private int partNb;

    private Simulation simulation;

    private double currentTemp;

    private int stepNumber;

    private RendezVous lrv, rrv;


    public RunLayer(int partNb, Simulation simulation, RendezVous leftRv, RendezVous rightRv) {
        super();
        this.partNb = partNb;
        this.simulation = simulation;
        this.lrv = leftRv;
        this.rrv = rightRv;
        currentTemp = 20;//Temperature de la couche au debut de la simulation
    }

    public void run() {


        for (int i = 0; i < simulation.getNbStep(); i++) {
            long timeBegin = System.currentTimeMillis();
            double leftPartTemp;
            double rightPartTemp;

            if (partNb != 1) {
                leftPartTemp = lrv.meetic(currentTemp);
            } else {
                leftPartTemp = 110;
            }

            if (partNb != 7) {
                rightPartTemp = rrv.meetic(currentTemp);
            } else {
                rightPartTemp = 20;
            }
            currentTemp = simulation.updateWallPartTemp(partNb, i, leftPartTemp, currentTemp, rightPartTemp);

            if (partNb == 1) {
                int tmp = simulation.getExecTime() + (int) (System.currentTimeMillis() - timeBegin);
                timeBegin=(long)0;
                simulation.setExecTime(tmp);
            }

        }
        //On attend que toutes les threads aient finies pour l'affichage
        try {
            simulation.getBarrier().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


}
