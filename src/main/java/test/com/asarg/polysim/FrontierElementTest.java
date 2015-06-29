package test.com.asarg.polysim; 

import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.FrontierElement;
import com.asarg.polysim.models.base.PolyTile;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import static org.junit.Assert.*;

/** 
* FrontierElement Tester. 
* 
* @author Mario M
* @since <pre>Aug 20, 2014</pre> 
* @version 1.0 
*/ 
public class FrontierElementTest {
    private FrontierElement fe1;
    private FrontierElement fe2;
    private FrontierElement fe3;
    private FrontierElement fe5;
    private FrontierElement fe6;

    @Before
public void before() throws Exception {
    PolyTile p1 = new PolyTile();
    p1.addTile(0, 0, new String[]{null, null, "a", null});
    fe1 = new FrontierElement(new Coordinate(0,1), new Coordinate(1,1), p1,0);

    // same polytile as the first
    PolyTile p2 = new PolyTile();
    p2.addTile(0, 0, new String[]{null, null, "a", null});
    fe2 = new FrontierElement(new Coordinate(0,1), new Coordinate(1,1), p2,0);

    // same polytile at different glue
    PolyTile p3 = new PolyTile();
    p3.addTile(0, 0, new String[]{null, null, "a", null});
    fe3 = new FrontierElement(new Coordinate(8,1), new Coordinate(1,1), p3,2);

    // same polytile at different point and same glue direction
    PolyTile p4 = new PolyTile();
    p4.addTile(0, 0, new String[]{null, null, "a", null});
    FrontierElement fe4 = new FrontierElement(new Coordinate(20, 1), new Coordinate(1, 1), p4, 0);

    // frontier element with different offset
    PolyTile p5 = new PolyTile();
    p5.addTile(0, 0, new String[]{null, null, "a", null});
    fe5 = new FrontierElement(new Coordinate(0,1), new Coordinate(1,20), p5,0);

    // different polytile.
        PolyTile p6 = new PolyTile();
    p6.addTile(0, 0, new String[]{null, "b", "a", null});
    fe6 = new FrontierElement(new Coordinate(0,1), new Coordinate(1,1), p6,0);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getLocation() 
* 
*/ 
@Test
public void testGetLocation() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getOffset() 
* 
*/ 
@Test
public void testGetOffset() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getPolyTile() 
* 
*/ 
@Test
public void testGetPolyTile() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getDirection() 
* 
*/ 
@Test
public void testGetDirection() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: equals(FrontierElement ele2) 
* 
*/ 
@Test
public void testEquals() throws Exception { 
//TODO: Test goes here...
    assertTrue("fe1 and fe2 should be the same", fe1.equals(fe2));
    assertTrue("fe2 and fe1 should be the same", fe2.equals(fe1));
    assertTrue("frontier elements fe1 and fe3 should be the same", fe1.equals(fe3));
    assertTrue("frontier elements fe1 and fe3 should be the same", fe3.equals(fe1));

    assertFalse("fe1 and fe5 should be different", fe1.equals(fe5));
    assertFalse("fe1 and fe5 should be different", fe5.equals(fe1));
    assertFalse("fe1 and fe6 should be different", fe1.equals(fe6));
    assertFalse("fe1 and fe6 should be different", fe6.equals(fe1));
}

}
