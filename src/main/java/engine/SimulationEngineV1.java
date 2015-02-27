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
    private int _t;
    private double wallC;
    private double insulationC;
    private ArrayList<Material> wallComponents;
    private ArrayList<Material> insulationComponents;

    public SimulationEngineV1(InsulatedWall wall){
        this.insulatedWall=wall;
        this.outsideTemp=T0;
        this._t =0;
        this.wallC=calculateC(insulatedWall.getWall());
        this.insulationC =calculateC(insulatedWall.getInsulation());
        this.wallComponents= new ArrayList<>();
        this.insulationComponents = new ArrayList<>();
        wallComponents.add(Material.BRICK);
        wallComponents.add(Material.GRANITE);
        insulationComponents.add(Material.GLASSWOOL);
    }

    public void launchEngine(){
        stepOne();

    }

    /**
     * Passage de T0 à T1
     */
    private void stepOne() {

        ArrayList<WallPart> wallparts=insulatedWall.getWallParts();

        //Etape 1: calculer le premier morceau du mur



        //Etape 2 : calculer par rapport à la temperature des autres

        for(int i=1;i<wallparts.size()-1;i++){


        }

        //Etape 3 : Calculer pour la dernière partie du mur



        _t++;
        updateOutsideTemp();
    }

    private void updateOutsideTemp() {
        outsideTemp=T1+B*Math.sin(OMEGA* _t);
    }

    /**
     * Méthode chargée de calculer la nouvelle valeur d'une partie de mur
     * @param previousPart la partie de mur précédente
     * @param currentPart la partie du mur dont la température va évoluer
     * @param nextPart la partie de mur suivante
     * @param bigC la constante C relative au materiau composant la partie de mur courrante
     */
    private void updatePartWallTemp(WallPart previousPart, WallPart currentPart, WallPart nextPart, double bigC){
        double newTemp=currentPart.getTemp()+bigC*(nextPart.getTemp()+previousPart.getTemp()-2*(currentPart.getTemp()));
        currentPart.setTemp(newTemp);
    }

    private double calculateC(Material material){

        double grandC=(material.getLambda()*DT)/(material.getMu()*material.getC()*DX*DX);
        return grandC;

    }

}
