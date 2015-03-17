import java.util.ArrayList;


/**
 * @author mmultari
 * @version 25/02/2015
 */
public class SimulationEngineV1 {

    //Le mur que l'on va etudier
    //private InsulatedWall insulatedWall;
    private int _t;
    //Le C du materiau composant le mur
    private double wallC;
    //Le C du materiau composant l'isolant du mur
    private double insulationC;
    //L'etape ou la temperature a change
    private int stepOfChange;
    //Savoir
    private boolean isChanged;
    private int execTime;

    //Le tableau qui contiendra les temperatures prochaines du mur
    private double [] nextTemp;

    //Le tableau qui contiendra les temperatures actuelles du mur
    private double [] currentTemp;

    /**
     * Constructeur de la classe simulation
     *
     * @param wall le mur dont on souhaite etudier l'evolution de temperature
     */

    /**
     * Constructeur de la classe simulation
     * @param wallCompos le materiau utilise pour composer le mur
     * @param insolationCompos le materiau utilise pour isoler le mur
     */
    public SimulationEngineV1(Material wallCompos, Material insolationCompos) {
        this.currentTemp= new double [9];
        this.nextTemp= new double[9];
        this.wallC = calculateC(wallCompos);
        this.insulationC = calculateC(insolationCompos);

        this._t = 0;
        this.stepOfChange = 0;
        isChanged = false;
        this.execTime = 0;

    }



    /**
     * Methode permettant d'executer la simulation avec un nombre de cycle choisi
     *
     * @param step le nombre de cycles choisit
     */
    public void runYourSimulation(int step) {
        for (int i = 0; i < step; i++) {
            oneStep();
        }
    }

    /**
     * Methode permettant d'executer la simulation avec 100000 cycles
     */
    public void runLongSimulation() {
        for (int i = 0; i < 100000; i++) {
            oneStep();
        }
    }

    /**
     * Methode permettant d'envoyer les resultats de la simulation avec un websocket
     */

    public void runWebSimulation() {

        JavaWebSocketServer.getInstance();// Init the server

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Envoi de l'etat initial du mur
        for (int i = 0; i < currentTemp.length; i++) {
            String message = "<elt><time>" + 0 + "</time><X>" + i + "</X><value>" + currentTemp[i] + "</value></elt>";
            JavaWebSocketServer.getInstance().broadcastMessage(message);
        }

        //Envoi de l'evolution du mur
        for (int i = 1; i < 199; i++) {
            oneStep();
            for (int j = 0; j < currentTemp.length; j++) {
                String message = "<elt><time>" + i + "</time><X>" + j + "</X><value>" + currentTemp[j] + "</value></elt>";
                JavaWebSocketServer.getInstance().broadcastMessage(message);
            }
        }
    }

    /**
     * Méthode représentant l'évolution de la temperature au cours d'un cycle
     */
    //TODO modifier les temperatures a la fin !
    private void oneStep() {

        long timeBegin = System.currentTimeMillis();

        /**
         * La premiere et la dernière partie du mur sont des constantes,
         * on ne va donc pas modifier leur valeur de temperature
         */

        /* Etape 1 : modification des parties du mur composees du premier materiau, soit les parties 1 a 4 */
        for (int i = 1; i < 5; i++) {
            nextTemp[i]=updateWallPartTemp(currentTemp[i-1], currentTemp[i], currentTemp[i+1], this.wallC);
        }
//TODO corriger la suite

        /* Etape 2 : modification de la temperature de la partie du milieu, soit la partie 5 */

        /* Etape 3 : modification de la temperature des dernieres parties du mur, soit les parties 6 et 7 */

        /*if (insulatedWall.getWallParts().get(lastPart).getAskedTemp() > 20 && isChanged == false) {
            stepOfChange = _t;
            isChanged = true;
        }*/

        //Le cycle est termine
        _t++;
        long timeEnd = System.currentTimeMillis();
        this.execTime += (int) (timeEnd - timeBegin);

    }


    /**
     * Méthode chargée de calculer la nouvelle valeur d'une partie de mur
     *
     * @param previousPart la partie de mur précédente
     * @param currentPart  la partie du mur dont la température va évoluer
     * @param nextPart     la partie de mur suivante
     * @param bigC         la constante C relative au materiau composant la partie de mur courrante
     */
    private double updateWallPartTemp(double previousPart, double currentPart, double nextPart, double bigC) {

        return currentPart + bigC * (nextPart + previousPart - 2 * (currentPart));

    }


    /**
     * Methode permettant de calculer C, representant la fraction de degres perdus par rayonnement
     *
     * @param material le materiau dont on souhaite calculer le C
     * @return la valeur de C calculee
     */
    private double calculateC(Material material) {

        double bigC = (material.getLambda() * Constantes.DT) / (material.getMu() * material.getC() * Constantes.DX * Constantes.DX);
        return bigC;

    }

    public int getStepOfChange() {
        return stepOfChange;
    }

    public int getExecTime() {
        return execTime;
    }
}
