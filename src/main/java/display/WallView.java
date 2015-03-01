package display;

import data.WallPart;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author mmultari
 * @version 25/02/2015
 */
public class WallView extends JFrame {

    private ArrayList<WallPart> wallParts;
    private WallPartView wallPartView;
    private WallPartTempView wallPartTempView;

    public WallView(ArrayList<WallPart> wallParts,int cycle){
        this.wallParts=wallParts;
        this.setTitle("Programmation concurente - Simulation de transfert de chaleur");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        this.getContentPane().add(new JLabel("Cycle : "+cycle,JLabel.CENTER), BorderLayout.NORTH);
        wallPartView=new WallPartView(wallParts);
        this.getContentPane().add(wallPartView, BorderLayout.CENTER);
        wallPartTempView=new WallPartTempView(wallParts);
        this.getContentPane().add(wallPartTempView, BorderLayout.SOUTH);
        this.setVisible(true);

    }

    public WallPartTempView getWallPartTempView() {
        return wallPartTempView;
    }
}