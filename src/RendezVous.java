/**
 * @author mmultari
 * @version 09/04/2015
 */
public class RendezVous {

    boolean isFirst=true;
    Double temperatures[] = new Double[2];

    /**
     * Le thread donne sa temperature et recupere celle de son voisin
     * @param tmp la temperature du thread qui arrive au rendez vous
     * @return la temperature du thread voisin
     */
    public synchronized Double meetic(Double tmp){
        if(isFirst){
            isFirst=false;
            temperatures[0] = tmp;
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return temperatures[1];
        }
        else{
            temperatures[1] = tmp;
            isFirst=true;
            notify();
            return temperatures[0];
        }

    }

}
