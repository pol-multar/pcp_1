/**
 * @author mmultari
 * @version 10/04/2015
 */
public class Simulation {
    //Pas de temps (secondes)
    //public static final int DT=1;
    private static final double DT = 600;

    //Pas d'espace (m)
    //public static final double DX=0.02;
    private static final double DX = 0.04;

    //Le tableau contenant les valeurs des temperatures a afficher
    private double[][] savedTemp;

    //Le nombre d'itération de la simulation
    int nbStep;

    //Le C du materiau composant le mur
    private static final double wallC=calculateC(0.84, 1400, 840);
    //Le C du materiau composant l'isolant du mur
    private static final double insulationC=calculateC(0.04, 30, 900);

    //Contient le temps d'execution de l'algorithme
    private int execTime;

    /**
     * Methode permettant de calculer C,
     * representant la fraction de degres perdus par rayonnement
     * @param lambda
     * @param mu
     * @param c
     * @return un double contenant la valeur de C calculee
     */
    private static double calculateC(double lambda, double mu, double c) {

        return (lambda * DT) / (mu * c * DX * DX);

    }

    /**
     * Constructeur de la classe SimulationEngine
     *
     * @param nbStep    le nombre d'etape de la simulation
     * @param debug     pour savoir si l'on doit afficher les etapes ou seulement le resultat final
     */
    public Simulation(int nbStep, boolean debug){
        this.savedTemp = new double[9][nbStep];
        this.nbStep=nbStep;
        this.execTime=0;
    }

    public int getNbStep() {
        return nbStep;
    }

    public int getExecTime() {
        return execTime;
    }

    public void setExecTime(int execTime) {
        this.execTime = execTime;
    }

    public void multithreadSimulation(){
        RendezVous lrv = new RendezVous();
        RendezVous rrv = new RendezVous();
        Thread [] threads = new Thread[6];
        for (int i = 1; i <8 ; i++) {
            System.out.println("Creation du thread "+i);
            new RunLayer(i,this,lrv,rrv).start();
            lrv=rrv;
            rrv = new RendezVous();
        }
/*
        for(Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
        //System.out.println(this.getExecTime());


    }

    //public void

    /**
     * Méthode chargée de calculer la nouvelle valeur d'une partie de mur
     * @param partNb       le numero de la partie du mur
     * @param step         le numero d iteration de la simulation
     * @param previousPart la partie de mur précédente
     * @param currentPart  la partie du mur dont la température va évoluer
     * @param nextPart     la partie de mur suivante
     * @return
     */
    public double updateWallPartTemp(int partNb,int step,double previousPart, double currentPart, double nextPart) {

        //return (currentPart + bigC * (nextPart + previousPart - 2 * (currentPart)));

        double newTemp;

        if (partNb < 5) {
            newTemp = currentPart + wallC * (nextPart + previousPart - 2 * (currentPart));
        } else if (partNb > 5) {
            newTemp = currentPart + insulationC * (nextPart + previousPart - 2 * (currentPart));
        } else {//i==5
            newTemp = currentPart+ wallC * (previousPart - currentPart) + insulationC * (nextPart - currentPart);
        }
        this.savedTemp[partNb][step]= newTemp;
        return newTemp;
    }
}
