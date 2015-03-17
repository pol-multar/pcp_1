/**
 * @author Maxime
 * @version 17/02/2015.
 */
public class Constantes {

    //Conductivité thermique du matériau en W.m-1.K-1
    public static final double GLASSWOOLLAMBDA=0.04;
    public static final double BRICKLAMBDA=0.84;
    public static final double GRANITELAMBDA=2.2;

    //masse volumique du matériau en kg.m-3 (mu ou rho)
    public static final double GLASSWOOLMU=30;
    public static final double BRICKMU=1400;
    public static final double GRANITEMU=2700;

    //chaleur spécifique massique du matériau en J.kg-1.K-1
    public static final double GLASSWOOLC=900;
    public static final double BRICKC=840;
    public static final double GRANITEC=790;

    //Temperatures en degré celsius
    public static final double T0=20;
    public static final double T1=55;
    public static final double OUTSIDETEMP=110;
    public static final double INSIDETEMP=20;

    public static final int B=40;

    public static final double OMEGA=86400/(2*Math.PI);

    //Pas de temps (secondes)
    public static final int DT=1;

    //Pas d'espace (m)
    public static final double DX=0.02;

    /**
     * Convertisseur de degre Celsius vers degre Kelvin
     * @param tempCelsius la temperature a convertir
     * @return la temperature convertie
     */
    public static double toKelvin(double tempCelsius){
        return (tempCelsius+274.15);
    }

    /**
     * Convertisseur de degre Kelvin vers degre Celsius
     * @param tempKelvin la temperature a convertir
     * @return la temperature convertie
     */
    public static double toCelsius(double tempKelvin){
        return (tempKelvin-274.15);
    }

}
