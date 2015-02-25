package engine;

import data.InsulatedWall;
import data.Material;
import static data.Constantes.*;

/**
 * @author mmultari
 * @version 25/02/2015
 */
public class SimulationEngineV1 {

    private InsulatedWall insulatedWall;

    public SimulationEngineV1(InsulatedWall wall){
        this.insulatedWall=wall;

    }

    public void launchEngine(){

    }

    private double calculateC(Material material){

        double grandC=(material.getLambda()*DT)/(material.getMu()*material.getC()*DX*DX);
        return grandC;

    }

}
