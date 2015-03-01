package display;

import data.Constantes;
import data.WallPart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * @author mmultari
 * @version 25/02/2015
 */
public class WallPartTempView extends JPanel implements Observer {

    private ArrayList<WallPart> wallParts;

    WallPartTempView(ArrayList<WallPart> wallParts) {
        this.wallParts = wallParts;
        GridLayout gl = new GridLayout();
        gl.setColumns(wallParts.size()+1);
        gl.setRows(1);
        this.setLayout(gl);
        initPanel();
    }

    private void initPanel(){
        this.add(new JLabel(Double.toString(Constantes.OUTSIDETEMP),JLabel.CENTER));
        for( WallPart wallPart : wallParts ){
            this.add( new JLabel(Double.toString(wallPart.getTemp()),JLabel.CENTER));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        initPanel();
    }
}
