package engine;

import data.InsulatedWall;
import data.Material;

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

    private void calculateC(){

    }

    private double toKelvin(double tempCelsius){
        return (tempCelsius+274.15);
    }
}
