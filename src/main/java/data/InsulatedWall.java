package data;

import java.util.ArrayList;

/**
 * @author Maxime
 * @version 03/02/2015.
 */
public class InsulatedWall {

    //Epaisseur du mur en centimetres (soit en brique soit en granite)
    private static final int wallLength=20;
    //Epaisseur de l'isolant en centimetres
    private static final int insulationLength=12;
    //Epaisseur totale mur+isolant
    public static final int

    private ArrayList<Material> wallParts;

    public InsulatedWall(){
        int cpt;

        for(cpt=0;cpt<wallLength;cpt++){
            wallParts.add(cpt,Material.BRICK);
        }

        for(cpt=(wallLength-1);)
    }
}
