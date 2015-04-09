import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * @author mmultari
 * @version 25/02/2015
 */
public class SimulationEngine {

    /* Constantes necessaires a la simulation */


    //Temperatures en degré celsius
    private static final double T0 = 20;
    private static final double OUTSIDETEMP = 110;
    private static final double INSIDETEMP = 20;

    //Pas de temps (secondes)
    //public static final int DT=1;
    private static final int DT = 600;

    //Pas d'espace (m)
    //public static final double DX=0.02;
    private static final double DX = 0.04;

    /* Attributs de la classe */

    //Contient le numero de l'etape courante
    private int simulationStepActualNumber;
    //Le C du materiau composant le mur
    private double wallC;
    //Le C du materiau composant l'isolant du mur
    private double insulationC;
    //L'etape ou la temperature a change
    private int stepOfChange;
    //Savoir si l'on a deja la premiere etape de changement de temperature
    private boolean isChanged;
    //Contient le temps d'execution de l'algorithme
    private int execTime;

    //Le tableau qui contiendra les temperatures prochaines du mur
    private double[] nextTemp;

    //Le tableau qui contiendra les temperatures actuelles du mur
    private double[] currentTemp;

    //La barrière utilisée en multithread
    CyclicBarrier barrier;

    //Le nombre d'itération de la simulation
    int nbStep;

    //mode avec affichage
    boolean debug;

    //Le tableau contenant les valeurs des temperatures a afficher
    private double[][] savedTemp;


    /**
     * Constructeur de la classe SimulationEngine avec 100 000 iterations
     *
     * @param wallCompos       le materiau utilise pour composer le mur
     * @param insolationCompos le materiau utilise pour isoler le mur
     * @param debug            pour savoir si l'on doit afficher les etapes ou seulement le resultat final
     */
    public SimulationEngine(Material wallCompos, Material insolationCompos, boolean debug) {
        this(wallCompos, insolationCompos, 100000, debug);
    }

    /**
     * Constructeur de la classe SimulationEngine
     *
     * @param wallCompos       le materiau utilise pour composer le mur
     * @param insolationCompos le materiau utilise pour isoler le mur
     * @param nbStep           le nombre d'etape de la simulation
     * @param debug            pour savoir si l'on doit afficher les etapes ou seulement le resultat final
     */
    public SimulationEngine(Material wallCompos, Material insolationCompos, int nbStep, boolean debug) {
        this.currentTemp = new double[9];
        this.nextTemp = new double[9];
        this.wallC = calculateC(wallCompos);
        this.insulationC = calculateC(insolationCompos);
        this.simulationStepActualNumber = 0;
        this.stepOfChange = 0;
        isChanged = false;
        this.execTime = 0;
        this.debug = debug;
        this.nbStep = nbStep;
        this.savedTemp = new double[9][nbStep];
        initWall();
    }


    /**
     * Methode chargee d'initialiser le tableau de temperatures du mur
     */
    private void initWall() {
        for (int i = 0; i < currentTemp.length - 1; i++) {
            currentTemp[i] = nextTemp[i] = T0;
        }
        currentTemp[0] = nextTemp[0] = OUTSIDETEMP;
        currentTemp[8] = nextTemp[8] = INSIDETEMP;
    }


    /**
     * Methode permettant d'executer la simulation avec 100000 cycles
     */
    public void runMonoThreadSimulation() {
        for (int i = 0; i < this.nbStep; i++) {
            oneStepMonoThread();
            displayResults();
        }
    }

    /**
     * Methode de creation des threads pour la deuxieme simulation.
     * Les threads sont crees pour les parties 1 a 7.
     * Les parties 0 et 8 ne changent, elles, pas de temperature
     * Elle initialise la barriere cyclique avec le nombre de thread
     * et le thread charge de l affichage
     */
    public void runMultiThreadSimulation2() {
        barrier = new CyclicBarrier(7, new Thread(createRunnableDisplay()));//7 threads car 7 parties

        for (int i = 1; i < currentTemp.length - 1; i++) {
            new Thread(createRunnableSimu2(i)).start();
        }

    }

    /**
     * Methode de creation des threads pour la deuxieme simulation.
     * Les threads sont crees pour les parties 1 a 7.
     * Les parties 0 et 8 ne changent, elles, pas de temperature
     */
    public void runMultiThreadSimulation3() {
        RendezVous rdvG = new RendezVous();
        RendezVous rdvD = new RendezVous();
        Thread[]threads=new Thread[7];

        //On lance les thread pour chaque partie du mur modifiee
        for (int i = 1; i < 8; i++) {
            threads[i-1]=new Thread(createRunnableSimu3(i, rdvG, rdvD));
            threads[i-1].start();
            /*Le rendez vous droit du thread actuel devient
              le rendez vous gauche du prochain thread */
            rdvG = rdvD;
            rdvD = new RendezVous();
        }


        /*
        On attend que l'execution soit terminee pour en afficher les resultat
         */
        for(Thread t: threads){
            try{
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*
        On affiche le resultat
         */

        System.out.println("Temps d execution : "+this.execTime);
    }


    /**
     * Méthode représentant l'évolution de la temperature au cours d'un cycle
     * en un seul thread
     */
    private void oneStepMonoThread() {

        long timeBegin = System.currentTimeMillis();

        /**
         * La premiere et la dernière partie du mur (respectivement partie 0 et partie 8) sont des constantes,
         * on ne va donc pas modifier leur valeur de temperature
         */

        /* Etape 1 : modification des parties du mur composees du premier materiau, soit les parties 1 a 4 */
        for (int i = 1; i < 5; i++) {
            nextTemp[i] = updateWallPartTemp(currentTemp[i - 1], currentTemp[i], currentTemp[i + 1], this.wallC);
        }

        /* Etape 2 : modification de la temperature de la partie du milieu, soit la partie 5 */

        nextTemp[5] = currentTemp[5] + this.wallC * (currentTemp[4] - currentTemp[5]) + this.insulationC * (currentTemp[6] - currentTemp[5]);

        /* Etape 3 : modification de la temperature des dernieres parties du mur, soit les parties 6 et 7 */

        for (int i = 6; i < currentTemp.length - 1; i++) {
            nextTemp[i] = updateWallPartTemp(currentTemp[i - 1], currentTemp[i], currentTemp[i + 1], this.insulationC);
        }

        if (((int) nextTemp[7]) > 20 && !isChanged) {
            stepOfChange = simulationStepActualNumber;
            isChanged = true;
        }

        /*  Je met a jour les temperatures */
        for (int i = 0; i < currentTemp.length; i++) {
            currentTemp[i] = nextTemp[i];
            //System.out.println("Nouvelle temperature de "+i+" = "+currentTemp[i]);
        }

        //Le cycle est termine
        simulationStepActualNumber++;
        long timeEnd = System.currentTimeMillis();
        this.execTime += (int) (timeEnd - timeBegin);

    }

    /**
     * Methode chargee de cree un thread pour une partie du mur
     * Cette methode est utilisee dans la partie multithreadee du projet avec barriere
     *
     * @param partNb le numero de la partie du mur
     * @return la thread de la partie du mur concerne
     */
    private Runnable createRunnableSimu2(final int partNb) {

        return new Runnable() {
            int cpt;
            int execTime = 0;

            public void run() {

                double newTemp;
                long timeBegin;

                for (cpt = 0; cpt < getNbStep(); cpt++) {

                    //Je recupere l'heure de debut de ma methode
                    timeBegin = System.currentTimeMillis();

                    //Je calcule la nouvelle temperature de la partie du mur en fonction de sa position dans le tableau
                    if (partNb < 5) {
                        newTemp = updateWallPartTemp(getCurrentTemp(partNb - 1), getCurrentTemp(partNb), getCurrentTemp(partNb + 1), getWallC());
                    } else if (partNb > 5) {
                        newTemp = updateWallPartTemp(getCurrentTemp(partNb - 1), getCurrentTemp(partNb), getCurrentTemp(partNb + 1), getInsulationC());
                    } else {
                        newTemp = getCurrentTemp(partNb) + getWallC() * (getCurrentTemp(partNb - 1) - getCurrentTemp(partNb)) + getInsulationC() * (getCurrentTemp(partNb + 1) - getCurrentTemp(partNb));
                    }

            /* Si je suis la derniere couche et que ma nouvelle temperature vaut 20
             * Alors je le notifie a la simulation et je sauvegarde le numero de l'etape dans laquelle je me trouve
             */
                    if (partNb == 7) {
                        if (((int) newTemp) > 20 && !isChanged()) {
                            setChanged(true);
                            setStepOfChange(cpt);
                        }
                    }

            /* J ai fini mon calcul, j'attends que les autres threads aient finies elles aussi
             * a l'aide de la cyclicBarrier de la simulation
             */

                    try {
                        getBarrier().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    //Je met a jour la temperature dans le tableau de la simulation

                    updateCurrentTemp(newTemp, partNb);

                    //Je met a jour le numero d etape de la simulation si ce n est pas deja fait

                    if (getSimulationStepActualNumber() < cpt) setSimulationStepActualNumber(cpt);


                    if (partNb == 1) {
                        execTime += (int) (System.currentTimeMillis() - timeBegin);
                        setExecTime(execTime);
                    }

                    try {
                        getBarrier().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }


            }
        };

    }

    /**
     * Methode chargee de l'affichage de la simulation multithreadee avec barriere
     *
     * @return un runnable qui affiche une fois sur deux les resultats
     */
    private Runnable createRunnableDisplay() {

        return new Runnable() {
            boolean needToDisplay = false;

            @Override
            public void run() {
                if (needToDisplay) {
                    displayResults();
                    needToDisplay = false;
                } else {
                    needToDisplay = true;
                }
            }
        };

    }

    private Runnable createRunnableSimu3(final int partNb, final RendezVous grv, final RendezVous drv) {
        return new Runnable() {

            int cpt; //Un compteur
            double newTemp; //La nouvelle temperature de la couche actuelle
            double currentPTemp; // La temperature actuelle de la couche actuelle
            double prevPTemp; // La temperature actuelle de la couche precedente
            double nextPTemp; // La temperature actuelle de la couche suivante

            @Override
            public void run() {


                long timeBegin;

                for (cpt = 0; cpt < getNbStep(); cpt++) {

                    //Je recupere l'heure de debut de ma methode
                    timeBegin = System.currentTimeMillis();

                    //Je recupere les temperatures
                    currentPTemp = getCurrentTemp(partNb);
                    if(partNb!=1) {
                        prevPTemp = grv.meetic(currentPTemp);
                    }else{
                        prevPTemp=OUTSIDETEMP;
                    }
                    if(partNb!=7) {
                        nextPTemp = drv.meetic(currentPTemp);
                    }else{
                        nextPTemp=INSIDETEMP;
                    }

                    if (partNb < 5) {
                        newTemp = updateWallPartTemp(prevPTemp, currentPTemp, nextPTemp, getWallC());
                    } else if (partNb > 5) {
                        newTemp = updateWallPartTemp(prevPTemp, currentPTemp, nextPTemp, getInsulationC());
                    } else {
                        newTemp = currentPTemp + getWallC() * (prevPTemp - currentPTemp) + getInsulationC() * (nextPTemp - currentPTemp);
                    }
                    /* Si je suis la derniere couche et que ma nouvelle temperature vaut 20
                     * Alors je le notifie a la simulation et je sauvegarde le numero de l'etape dans laquelle je me trouve
                     */

                    if (partNb == 7) {
                        if (((int) newTemp) > 20 && !isChanged()) {
                            setChanged(true);
                            setStepOfChange(cpt);
                        }
                    }

                    //Je met a jour la temperature dans les tableau de la simulation
                    updateCurrentTemp(newTemp, partNb);
                    savedTemp[partNb][cpt] = newTemp;

                    //Je met a jour le numero d etape de la simulation si ce n est pas deja fait

                    if (getSimulationStepActualNumber() < cpt) setSimulationStepActualNumber(cpt);


                    if (partNb == 1) {
                        execTime += (int) (System.currentTimeMillis() - timeBegin);
                        setExecTime(execTime);
                    }

                }

            }
        };
    }


    /**
     * Méthode chargée de calculer la nouvelle valeur d'une partie de mur
     *
     * @param previousPart la partie de mur précédente
     * @param currentPart  la partie du mur dont la température va évoluer
     * @param nextPart     la partie de mur suivante
     * @param bigC         la constante C relative au materiau composant la partie de mur courrante
     */
    public double updateWallPartTemp(double previousPart, double currentPart, double nextPart, double bigC) {

        return (currentPart + bigC * (nextPart + previousPart - 2 * (currentPart)));

    }


    /**
     * Methode permettant de calculer C, representant la fraction de degres perdus par rayonnement
     *
     * @param material le materiau dont on souhaite calculer le C
     * @return la valeur de C calculee
     */
    private double calculateC(Material material) {

        return (material.getLambda() * (double) DT) / (material.getMu() * material.getC() * DX * DX);

    }

    /**
     * Methode chargee d'afficher les dix premieres heures de simulation
     */
    private void displayTenFirstHours() {
        if ((this.getSimulationStepActualNumber() % 6) == 0 && (this.getSimulationStepActualNumber() < 61) && (this.getSimulationStepActualNumber() > 0)) {
            System.out.println(this);
        }
    }

    /**
     * Methode chargee d' afficher le resultat de la simulation
     */
    public void displayResults() {

        if (debug)
            displayTenFirstHours();

        if (this.getSimulationStepActualNumber() == this.getNbStep() - 1) {
            System.out.println("Changement a partir de l'etape " + this.getStepOfChange()
                    + " soit après " + (this.getStepOfChange() * 600) / 3600 + " heure(s)");
            System.out.println("Temps d'execution de la simulation : " + this.getExecTime() + " ms");
        }
    }

    /**
     * Convertisseur de double vers int
     *
     * @param aDouble le double a convertir
     * @return le resultat en int
     */
    public static int toInt(double aDouble) {
        return Integer.valueOf((int) Math.round(aDouble));
    }

    /* Accesseurs en lecture et en ecriture */

    public int getNbStep() {
        return nbStep;
    }

    public int getStepOfChange() {
        return stepOfChange;
    }

    public int getExecTime() {
        return execTime;
    }

    public void setStepOfChange(int stepOfChange) {
        this.stepOfChange = stepOfChange;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public double getCurrentTemp(int index) {
        return currentTemp[index];
    }

    public void updateCurrentTemp(double newTemp, int index) {
        this.currentTemp[index] = newTemp;
    }

    public double getWallC() {
        return wallC;
    }

    public double getInsulationC() {
        return insulationC;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public int getSimulationStepActualNumber() {
        return simulationStepActualNumber;
    }

    public void setSimulationStepActualNumber(int simulationStepActualNumber) {
        this.simulationStepActualNumber = simulationStepActualNumber;
    }

    public void setExecTime(int execTime) {
        this.execTime = execTime;
    }

    @Override
    public String toString() {
        int hour = ((simulationStepActualNumber) * DT) / 3600;

        String toReturn = "t=" + hour + " heure(s) ";

        for (int i = 0; i < 5; i++) {
            toReturn += toInt(currentTemp[i]) + ",";
        }
        toReturn += toInt(currentTemp[5]) + "-" + toInt(currentTemp[5]) + ",";
        for (int i = 6; i < 8; i++) {
            toReturn += toInt(currentTemp[i]) + ",";
        }
        toReturn += toInt(currentTemp[8]);
        return toReturn;
    }
}

