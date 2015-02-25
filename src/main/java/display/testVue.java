package display;

import data.InsulatedWall;

/**
 * @author mmultari
 * @version 25/02/2015
 */
public class testVue {
    public static void main(String[] args) {
        InsulatedWall wall1 = new InsulatedWall();
        Window myWin = new Window(wall1.getWallParts());
    }
}
