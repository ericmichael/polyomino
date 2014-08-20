package test.com.asarg.polysim; 

import com.asarg.polysim.FrontierElement;
import com.asarg.polysim.PolyTile;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import static org.junit.Assert.*;

import java.awt.*;

/** 
* FrontierElement Tester. 
* 
* @author Mario M
* @since <pre>Aug 20, 2014</pre> 
* @version 1.0 
*/ 
public class FrontierElementTest {
    FrontierElement fe1,fe2,fe3,fe4;
    PolyTile p1,p2,p3,p4;
@Before
public void before() throws Exception {
    p1 = new PolyTile();
    p1.addTile(0,0, new String[]{null, null, "a",null});
    fe1 = new FrontierElement(new Point(0,1), new Point(1,1),p1,0);

    // same polytile as the first
    p2 = new PolyTile();
    p2.addTile(0,0, new String[]{null, null, "a",null});
    fe2 = new FrontierElement(new Point(0,1), new Point(1,1),p2,0);

    // same polytile at different glue
    p3 = new PolyTile();
    p2.addTile(0,0, new String[]{null, null, "a",null});
    fe3 = new FrontierElement(new Point(8,1), new Point(1,1),p3,2);

    // same polytile at different point and same glue direction
    p4 = new PolyTile();
    p2.addTile(0,0, new String[]{null, null, "a",null});
    fe4 = new FrontierElement(new Point(20,1), new Point(1,1),p4,0);
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
}

}
