package data;

import java.util.Objects;

/**
 * @author mmultari
 * @version 24/02/2015
 */
public enum Material {
    GLASSWOOL("Laine de verre"),
    BRICK("Brique"),
    GRANITE("Granite");

    private String name;
    private int length;

    Material(String name) {
        this.name = name;
        this.length = 4;
    }

    public int getLength(){
        return this.length;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "C'est un materiau de"+ name + "de largeur" + length;
    }

}
