import java.util.ArrayList;


/**
 * @author mmultari
 * @version 25/02/2015
 */
public class SimulationEngineV1 {

    private InsulatedWall insulatedWall;
    private double outsideTemp;
    private double insideTemp;
    private int _t;
    private double wallC;
    private double insulationC;
    private ArrayList<Material> wallComponents;
    private ArrayList<Material> insulationComponents;
    private Boolean debug;
    private int stepOfChange;
    private boolean isChanged;
    private int execTime;

    /**
     * Constructeur de la classe simulation
     *
     * @param wall le mur dont on souhaite etudier l'evolution de temperature
     */
    public SimulationEngineV1(InsulatedWall wall) {
        this.insulatedWall = wall;
        this.outsideTemp = Constantes.OUTSIDETEMP;
        this.insideTemp = Constantes.INSIDETEMP;
        this._t = 0;
        this.stepOfChange = 0;
        this.wallC = calculateC(insulatedWall.getWall());
        this.insulationC = calculateC(insulatedWall.getInsulation());
        this.wallComponents = new ArrayList<>();
        this.insulationComponents = new ArrayList<>();
        wallComponents.add(Material.BRICK);
        wallComponents.add(Material.GRANITE);
        insulationComponents.add(Material.GLASSWOOL);
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
        for (int i = 0; i < insulatedWall.getWallParts().size(); i++) {
            String message = "<elt><time>" + 0 + "</time><X>" + i + "</X><value>" + insulatedWall.getWallParts().get(i).getAskedTemp() + "</value></elt>";
            JavaWebSocketServer.getInstance().broadcastMessage(message);
        }

        //Envoi de l'evolution du mur
        for (int i = 1; i < 200; i++) {
            oneStep();
            for (int j = 0; j < insulatedWall.getWallParts().size(); j++) {
                String message = "<elt><time>" + i + "</time><X>" + j + "</X><value>" + insulatedWall.getWallParts().get(j).getAskedTemp() + "</value></elt>";
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
         * La première et la dernière partie du mur sont des constantes
         */
        for (int i = 1; i < insulatedWall.getWallParts().size() - 1; i++) {

            double theBigC;

            if (this.wallComponents.contains(insulatedWall.getWallParts().get(i).getCompos())) {
                theBigC = wallC;
            } else {
                theBigC = insulationC;
            }

            updateWallPartTemp(insulatedWall.getWallParts().get(i - 1), insulatedWall.getWallParts().get(i), insulatedWall.getWallParts().get(i + 1), theBigC);

        }

        //Etape 3 : Calculer pour la dernière partie du mur

        int lastPart = insulatedWall.getWallParts().size() - 1;

        updateWallPartTemp(insulatedWall.getWallParts().get(lastPart - 1), insulatedWall.getWallParts().get(lastPart), new WallPart(insideTemp, Material.GLASSWOOL), insulationC);

        insideTemp = insulatedWall.getWallParts().get(lastPart).getTemp();


        if (insulatedWall.getWallParts().get(lastPart).getAskedTemp() > 20 && isChanged == false) {
            stepOfChange = _t;
            isChanged = true;
        }

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
    private void updateWallPartTemp(WallPart previousPart, WallPart currentPart, WallPart nextPart, double bigC) {

        double currentPartTemp = Constantes.toKelvin(currentPart.getTemp());
        double previousPartTemp = Constantes.toKelvin(previousPart.getTemp());
        double nextPartTemp = Constantes.toKelvin(nextPart.getTemp());

        double newTemp = currentPartTemp + bigC * (nextPartTemp + previousPartTemp - 2 * (currentPartTemp));

        currentPart.setTemp(Constantes.toCelsius(newTemp));
    }


    /**
     * Methode permettant de calculer C, representant la fraction de degres perdus par rayonnement
     *
     * @param material le materiau dont on souhaite calculer le C
     * @return la valeur de C calculee
     */
    private double calculateC(Material material) {

        double grandC = (material.getLambda() * Constantes.DT) / (material.getMu() * material.getC() * Constantes.DX * Constantes.DX);
        return grandC;

    }

    public int getStepOfChange() {
        return stepOfChange;
    }

    public int getExecTime() {
        return execTime;
    }
}
