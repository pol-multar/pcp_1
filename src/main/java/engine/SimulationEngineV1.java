package engine;

import data.InsulatedWall;
import data.Material;
import data.WallPart;

import java.util.ArrayList;

import static data.Constantes.*;

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

    public SimulationEngineV1(InsulatedWall wall) {
        this.insulatedWall = wall;
        this.outsideTemp = OUTSIDETEMP;
        this.insideTemp = INSIDETEMP;
        this._t = 0;
        this.wallC = calculateC(insulatedWall.getWall());
        this.insulationC = calculateC(insulatedWall.getInsulation());
        this.wallComponents = new ArrayList<>();
        this.insulationComponents = new ArrayList<>();
        wallComponents.add(Material.BRICK);
        wallComponents.add(Material.GRANITE);
        insulationComponents.add(Material.GLASSWOOL);
    }

    public void launchEngine() {
        oneStep();

    }

    /**
     * Passage de T0 à T1
     */
    private void oneStep() {

        //Etape 1: calculer la temperature du premier morceau du mur, directement soumis à l'action du soleil

        updateFirstWallPartTemp(insulatedWall.getWallParts().get(0));

        //Etape 2 : calculer la temperature pour les autres parties sauf la derniere

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

        _t++;

    }

    private void updateFirstWallPartTemp(WallPart firstPart) {
        firstPart.setTemp(T1 + B * Math.sin(OMEGA * _t));
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
        double newTemp = currentPart.getTemp() + bigC * (nextPart.getTemp() + previousPart.getTemp() - 2 * (currentPart.getTemp()));
        currentPart.setTemp(newTemp);
    }


    private double calculateC(Material material) {

        double grandC = (material.getLambda() * DT) / (material.getMu() * material.getC() * DX * DX);
        return grandC;

    }

}
