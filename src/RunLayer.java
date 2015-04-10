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
        long timeBegin = System.currentTimeMillis();

        for (int i = 0; i < simulation.getNbStep(); i++) {
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
                simulation.setExecTime(tmp);
            }

        }
    }


}
