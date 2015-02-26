package engine;

import data.InsulatedWall;
import data.Material;
import data.WallPart;

import static data.Constantes.*;

/**
 * @author mmultari
 * @version 25/02/2015
 */
public class SimulationEngineV1 {

    private InsulatedWall insulatedWall;
    private double outsideTemp;
    private int _t;

    public SimulationEngineV1(InsulatedWall wall){
        this.insulatedWall=wall;
        this.outsideTemp=T0;
        this._t =0;
    }

    public void launchEngine(){
        stepOne();

    }

    /**
     * Passage de T0 à T1
     */
    private void stepOne() {

        double wallC=calculateC(insulatedWall.getWall());
        double insulation=calculateC(insulatedWall.getInsulation());

        //Etape 1: calculer le premier morceau du mur



        //Etape 2 : calculer par rapport à la temperature des autres

        for(int i=1;i<insulatedWall.getWallParts().size();i++){


        }
        _t++;
        updateOutsideTemp();
    }

    private void updateOutsideTemp() {
        outsideTemp=T1+B*Math.sin(OMEGA* _t);
    }

    /**
     *
     * @param previousPart
     * @param currentPart
     * @param nextPart
     * @param bigC
     */
    //TODO verifier
    private void updatePartWallTemp(WallPart previousPart, WallPart currentPart, WallPart nextPart, double bigC){
        double newTemp=currentPart.getTemp()+bigC*(nextPart.getTemp()+previousPart.getTemp()-2*(currentPart.getTemp()));
    }

    private double calculateC(Material material){

        double grandC=(material.getLambda()*DT)/(material.getMu()*material.getC()*DX*DX);
        return grandC;

    }

}
