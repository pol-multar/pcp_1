package data;

import java.util.Objects;
import static data.Constantes.*;

/**
 * @author mmultari
 * @version 24/02/2015
 */
public enum Material {
    GLASSWOOL("Laine de verre",GLASSWOOLLAMBDA,GLASSWOOLMU,GLASSWOOLC),
    BRICK("Brique",BRICKLAMBDA,BRICKMU,BRICKC),
    GRANITE("Granite",GRANITELAMBDA,GRANITEMU,GRANITEC);

    private String name;
    private int length;
    private double lambda;
    private double mu;
    private double c;

    Material(String name,double lambda, double mu, double c) {
        this.name = name;
        this.length = 4;
        this.lambda=lambda;
        this.mu=mu;
        this.c=c;
    }

    public int getLength(){
        return this.length;
    }

    public String getName(){
        return this.name;
    }

    public double getLambda(){
        return lambda;
    }

    public double getMu(){
        return mu;
    }

    public double getC(){
        return c;
    }

    public String toString(){
        return "materiau de "+ name + " de largeur " + length;
    }

}
