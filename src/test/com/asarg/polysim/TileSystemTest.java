package test.com.asarg.polysim; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import static org.junit.Assert.*;
import com.asarg.polysim.TileSystem;

/** 
* TileSystem Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug 18, 2014</pre> 
* @version 1.0 
*/ 
public class TileSystemTest {
    private TileSystem ts;
    private int temperature;

@Before
public void before() throws Exception {
    this.ts = new TileSystem(this.temperature);
    this.ts.addGlueFunction("a","a", 2);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: addGlueFunction(String label1, String label2, int temp) 
* 
*/ 
@Test
public void testAddGlueFunction() throws Exception {
    this.ts.addGlueFunction("b","b", 2);
    assertEquals("Strength of b-b glue should be " + 2, 2, this.ts.getStrength("b", "b"));

    this.ts.addGlueFunction("a","a", 5);
    assertEquals("Strength of a-a glue should be " + 5, 5, this.ts.getStrength("a", "a"));
} 

/** 
* 
* Method: removeGlueFunction(String label1, String label2) 
* 
*/ 
@Test
public void testRemoveGlueFunction() throws Exception { 
    this.ts.removeGlueFunction("a", "a");

    assertEquals("Strength should be 0 after removal", 0, this.ts.getStrength("a", "a"));
}

/** 
* 
* Method: getStrength(String label1, String label2) 
* 
*/ 
@Test
public void testGetStrength() throws Exception { 
    assertEquals("Strength should be 2", 2, this.ts.getStrength("a","a"));
} 

/** 
* 
* Method: addPolyTile(PolyTile p) 
* 
*/ 
@Test
public void testAddPolyTile() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getTemperature() 
* 
*/ 
@Test
public void testGetTemperature() throws Exception { 
    assertEquals("Temperature should equal " + this.temperature, this.temperature, this.ts.getTemperature());
} 

/** 
* 
* Method: setTemperature(int s) 
* 
*/ 
@Test
public void testSetTemperature() throws Exception {
    this.ts.setTemperature(5);
    assertEquals("Temperature after setting should equal " + 5, 5, this.ts.getTemperature());
}

/** 
* 
* Method: getTileTypes() 
* 
*/ 
@Test
public void testGetTileTypes() throws Exception { 
//TODO: Test goes here... 
} 


} 
