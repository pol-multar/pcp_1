package data;

import java.util.ArrayList;

import static data.Constantes.*;


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
    public static final int insulatedWallLength = wallLength + insulationLength;

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
     * @param insulation le type de materiaux composant l'isolant
     */
    private void initWall(Material wallCompos, Material insulation) {

        wallParts= new ArrayList<WallPart>();

        int cpt;

        /* Je construit le mur */
        for (cpt = 0; cpt < wallLength/wallCompos.getLength(); cpt++) {
            wallParts.add(cpt, new WallPart(T0,wallCompos));
        }

        /* Je construis l'isolant */
        for (cpt = wallLength/wallCompos.getLength(); cpt < insulatedWallLength/insulation.getLength(); cpt++) {
            wallParts.add(cpt, new WallPart(T0,insulation));
        }

        this.wall=wallCompos;
        this.insulation=insulation;


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

    @Override
    public String toString() {
        return "C est un mur de "+ insulatedWallLength + " centimetres.\n"
                + "Il est compose sur sa face exterieure de "+wall+" et sur sa face intérieure de "+insulation+".";
    }

}
