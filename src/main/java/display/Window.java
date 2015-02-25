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
public class Window extends JFrame {

    private ArrayList<WallPart> wallParts;

    public Window(ArrayList<WallPart> wallParts){
        this.wallParts=wallParts;
        this.setTitle("Programmation concurente - Simulation de transfert de chaleur");
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
        this.getContentPane().add(new InfosView(), BorderLayout.NORTH);
        this.getContentPane().add(new WallPartView(wallParts), BorderLayout.CENTER);
        this.getContentPane().add(new WallPartTempView(wallParts), BorderLayout.SOUTH);
        this.setVisible(true);

    }



}