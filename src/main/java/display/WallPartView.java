package display;

import data.WallPart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author mmultari
 * @version 25/02/2015
 */
public class WallPartView extends JPanel {

    private ArrayList<WallPart> wallParts;

    WallPartView(ArrayList<WallPart> wallParts) {
        this.wallParts = wallParts;
        GridLayout gl = new GridLayout();
        gl.setColumns(wallParts.size()+1);
        gl.setRows(1);
        this.setLayout(gl);
        initPanel();
    }

    private void initPanel() {
        this.add(new JLabel("Exterieur",JLabel.CENTER));
        for( WallPart wallPart : wallParts ){
            this.add( new JLabel(wallPart.getCompos().toString(),JLabel.CENTER));
        }
    }


}
