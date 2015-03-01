package data;

/**
 * La classe WallPart définit les éléments qui vont composer le mur
 * Un element est compose d un materiau et d une temperature
 * @author mmultari
 * @version 25/02/2015
 */
public class WallPart {

    private double temp;
    private Material compos;

    /**
     * Constructeur de la classe WallPart
     * @param temp la temperature de ce bout de mur
     * @param compos le materiau composant ce bout de mur
     */
    public WallPart(double temp, Material compos){
        this.temp=temp;
        this.compos=compos;
    }

    public double getTemp(){
        return temp;
    }

    public int getAskedTemp(){
        Integer integer =  Integer.valueOf((int) Math.round(temp));
        return integer;
    }

    public Material getCompos(){
        return compos;
    }

    public void setTemp(double newTemp){
        this.temp=newTemp;
    }

    @Override
    public String toString() {
        Integer integer =  Integer.valueOf((int) Math.round(temp));
        return "element du mur en "+compos+". Sa temperature est de "+integer+"\n";
    }
}
