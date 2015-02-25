package data;

/**
 * La classe WallPart définit les éléments qui vont composer le mur
 * Un element est compose d un materiau et d une temperature
 * @author mmultari
 * @version 25/02/2015
 */
public class WallPart {

    private int temp;
    private Material compos;

    /**
     * Constructeur de la classe WallPart
     * @param temp la temperature de ce bout de mur
     * @param compos le materiau composant ce bout de mur
     */
    public WallPart(int temp, Material compos){
        this.temp=temp;
        this.compos=compos;
    }

    public int getTemp(){
        return temp;
    }

    public Material getCompos(){
        return compos;
    }

    public void setTemp(int newTemp){
        this.temp=newTemp;
    }
}
