/**
 * @author mmultari
 * @version 24/02/2015
 */
public enum Material {
    GLASSWOOL("laine de verre",Constantes.GLASSWOOLLAMBDA,Constantes.GLASSWOOLMU,Constantes.GLASSWOOLC),
    BRICK("brique",Constantes.BRICKLAMBDA,Constantes.BRICKMU,Constantes.BRICKC),
    GRANITE("granite",Constantes.GRANITELAMBDA,Constantes.GRANITEMU,Constantes.GRANITEC);

    private String name;
    private int length;
    private double lambda;
    private double mu;
    private double c;

    /**
     * Constructeur de materiau utilisé pour former le mur
     * @param name le nom du matériau
     * @param lambda le lambda du matériau
     * @param mu le mu du materiau
     * @param c le c du materiau
     */
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
