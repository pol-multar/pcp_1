
import java.util.ArrayList;


/**
 * @author Maxime
 * @version 03/02/2015.
 */
public class InsulatedWall {

    //Epaisseur du mur en centimetres (soit en brique soit en granite)
    private static final int wallLength = 20;
    //Epaisseur de l'isolant en centimetres
    private static final int insulationLength = 12;
    //Epaisseur totale mur+isolant
    private static final int insulatedWallLength = wallLength + insulationLength;

    //Nombre de partie du mur en fonction de l'epaisseur totale
    private int wallPartNumber;
    //Nombre de partie de l'isolant en fonction de l'epaisseur totale
    private int insulationPartNumber;

    private ArrayList<WallPart> wallParts;

    private Material wall;
    private Material insulation;


    /**
     * Constructeur par défaut
     * Il définit un mur en brique isolé avec de la laine de verre
     */
    public InsulatedWall() {
        initWall(Material.BRICK, Material.GLASSWOOL);
    }

    /**
     * Constructeur prenant en entrée le type de mur et le type d'isolant
     * Attention ne vérifie pas leur valeur !
     * @param wall le type de materiaux composant le mur
     * @param insulation le type de materiaux composant l'isolant
     */
    public InsulatedWall(Material wall, Material insulation) {
        initWall(wall, insulation);
    }

    /**
     * Mérhode d'initialisation du mur
     * @param wallCompos le type de materiaux composant le mur
     * @param insulationCompos le type de materiaux composant l'isolant
     */
    private void initWall(Material wallCompos, Material insulationCompos) {

        wallParts= new ArrayList<WallPart>();
        wallPartNumber=(wallLength/wallCompos.getLength());
        insulationPartNumber=(insulationLength/insulationCompos.getLength());


        /* Je construit le mur */
      for (int cpt = 0; cpt < wallPartNumber; cpt++) {
           wallParts.add(new WallPart(Constantes.T0,wallCompos));
       }

        /* Je construis l'isolant */
        for (int cpt = wallPartNumber+1; cpt < wallPartNumber+insulationPartNumber+1; cpt++) {
            wallParts.add(new WallPart(Constantes.T0,insulationCompos));
        }

        this.wall=wallCompos;
        this.insulation=insulationCompos;

        /* La première partie du mur suit la loi de commande, ici Cte à 110 °C */
        wallParts.get(0).setTemp(Constantes.OUTSIDETEMP);

        /* La dernière partie du mur reste à 20°C */
        wallParts.get(wallParts.size()-1).setTemp(Constantes.INSIDETEMP);

    }

    public ArrayList<WallPart> getWallParts(){
        return wallParts;
    }

    public Material getWall() {
        return wall;
    }

    public Material getInsulation() {

        return insulation;
    }

    public int getWallPartNumber() {
        return wallPartNumber;
    }

    public int getInsulationPartNumber() {
        return insulationPartNumber;
    }

    @Override
    public String toString() {

        String walls = new String();

        for(WallPart wallPart : wallParts){
        walls+=wallPart.toString()+"\n";
        }

        return "Mur de "+ insulatedWallLength + " centimetres.\n"
                + "Il est compose sur sa face exterieure de "+wall+"\n"+"et sur sa face intérieure de "+insulation+".\n"
                + walls;
    }

}
