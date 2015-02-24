package data;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * @author mmultari
 * @version 24/02/2015
 */
public class InsulatedWallTest {
    private InsulatedWall wall1, wall2, wall3;


    public InsulatedWallTest() {
    }

    /**
     * Initialisation des test
     */
    @Before
    public void setUp() {
        wall1 = new InsulatedWall();
        wall2 = new InsulatedWall(Material.GRANITE, Material.GLASSWOOL);
        wall3 = new InsulatedWall(Material.GLASSWOOL, Material.GLASSWOOL);

    }

    @After
    public void tearDown() {
        wall1 = null;
        wall2 = null;
        wall3 = null;
    }


    @Test
    public void testWallLength() {
        assertEquals(8, wall1.getWallParts().size());
        assertEquals(8, wall2.getWallParts().size());
        assertEquals(8, wall3.getWallParts().size());
    }

    @Test
    public void testWallContent(){
        ArrayList<Material> al=wall1.getWallParts();
        testAlContent(Material.BRICK,Material.GLASSWOOL,al);
        al=wall2.getWallParts();
        testAlContent(Material.GRANITE,Material.GLASSWOOL,al);
        al=wall3.getWallParts();
        testAlContent(Material.GLASSWOOL,Material.GLASSWOOL,al);

    }

    private void testAlContent(Material wall, Material insulation,ArrayList<Material> al){
        assertEquals(wall,al.get(0));
        assertEquals(wall,al.get(4));
        assertEquals(insulation,al.get(5));
        assertEquals(insulation,al.get(7));
    }

}

