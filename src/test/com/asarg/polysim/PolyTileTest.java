package test.com.asarg.polysim; 

import com.asarg.polysim.*;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import static org.junit.Assert.*;

public class PolyTileTest {
    private PolyTile pt;
@Before
public void before() throws Exception {
    this.pt = new PolyTile();
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: addTile(int x, int y, String[] gl) 
* 
*/ 
@Test
public void testAddTile() throws Exception { 
/*Create and add a tile to the test polytile.
    check if it's in list by getting its open glues.
 */
    String[] blankGlues = {null,"I'm a glue",null,null};

    this.pt.addTile(0,0,blankGlues);
} 

/** 
* 
* Method: removeTile(int x, int y) 
* 
*/ 
@Test
public void testRemoveTile() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getTile(int x, int y) 
* 
*/ 
@Test
public void testGetTile() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: changeConcentration(double c) 
* 
*/ 
@Test
public void testChangeConcentration() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: changeCount(int c) 
* 
*/ 
@Test
public void testChangeCount() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: changeName(String n) 
* 
*/ 
@Test
public void testChangeName() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getGlues() 
* 
*/ 
@Test
public void testGetGlues() throws Exception { 
//TODO: Test goes here... 
} 


} 
