/**
 * Enum des differents types de materiaux possible pour le mur
 * @author mmultari
 * @version 24/02/2015
 */
public enum Material {

    GLASSWOOL("laine de verre", 0.04, 30, 900),
    BRICK("brique", 0.84, 1400, 840),
    GRANITE("granite", 2.2, 2700, 790);

    private String name;
    private double lambda;
    private double mu;
    private double c;

    /**
     * Constructeur de materiau utilisé pour former le mur
     *
     * @param name   le nom du matériau
     * @param lambda le lambda du matériau
     * @param mu     le mu du materiau
     * @param c      le c du materiau
     */
    Material(String name, double lambda, double mu, double c) {
        this.name = name;
        this.lambda = lambda;
        this.mu = mu;
        this.c = c;
    }

    public String getName() {
        return this.name;
    }

    public double getLambda() {
        return lambda;
    }

    public double getMu() {
        return mu;
    }

    public double getC() {
        return c;
    }

    public String toString() {
        return "materiau de " + name;
    }

}
