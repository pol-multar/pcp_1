/**
 * @author mmultari
 * @version 09/04/2015
 */
public class RendezVous {

    //Contient le nombre de thread presente au rdv
    protected int nbPresent;
    //Contient les temperatures a retourner
    protected double [] temperatures;

    public RendezVous(){
        nbPresent=0;
        temperatures = new double[2];
    }

    /**
     * Le thread donne sa temperature et recupere celle de son voisin
     * @param tmp la temperature du thread qui arrive au rendez vous
     * @return la temperature du thread voisin
     */
    public synchronized double meetic(Double tmp){
        if(nbPresent==0){
            nbPresent=1;
            temperatures[0] = tmp;
            try{
                wait();
            } catch (InterruptedException e) {
            }
            return temperatures[1];
        }
        else{
            nbPresent=0;
            temperatures[1] = tmp;
            notify();
            return temperatures[0];
        }

    }

}
