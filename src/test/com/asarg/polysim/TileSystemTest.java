package test.com.asarg.polysim; 

import com.asarg.polysim.PolyTile;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import com.asarg.polysim.TileSystem;
import java.util.Set;
import java.util.HashSet;

public class TileSystemTest {
    private TileSystem ts;
    private int temperature;

@Before
public void before() throws Exception {
    ts = new TileSystem(temperature);
    ts.addGlueFunction("a","a", 2);
    ts.addGlueFunction("t","e", 10);
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
    ts.addGlueFunction("b", "b", 2);
    assertEquals("Strength of b-b glue should be " + 2, 2, ts.getStrength("b", "b"));

    ts.addGlueFunction("a","a", 5);
    assertEquals("Strength of a-a glue should be " + 5, 5, ts.getStrength("a", "a"));

    // a-a is now 5.
    assertThat("Adding the same labels with different strength should overwrite it", ts.getStrength("a", "a"), not(equalTo(2)));

    ts.addGlueFunction("a","b",2);
    assertEquals("Labels need not be equal to have >0 strength. ", 2, ts.getStrength("a", "b"));
    assertEquals("Label order should not matter", 2, ts.getStrength("b","a"));

    ts.addGlueFunction("b","a", 5);
    assertEquals("Label order does not matter, same labels should be overwritten", 5, ts.getStrength("b","a"));
    assertEquals("Label order does not matter, same labels should be overwritten", 5, ts.getStrength("a", "b"));
    assertThat("Label order does not matter, same labels should be overwritten", ts.getStrength("a","b"), not(equalTo(2)));

} 

/** 
* 
* Method: removeGlueFunction(String label1, String label2) 
* 
*/ 
@Test
public void testRemoveGlueFunction() throws Exception { 
    ts.removeGlueFunction("a", "a");

    assertEquals("Strength should be 0 after removal", 0, ts.getStrength("a", "a"));
    assertNotNull("Strength is 0, never null", ts.getStrength("a","a"));
}

/** 
* 
* Method: getStrength(String label1, String label2) 
* 
*/ 
@Test
public void testGetStrength() throws Exception { 
    assertEquals("Strength should be 2", 2, ts.getStrength("a","a"));
    assertEquals("Strength returned should match what's given", 10, ts.getStrength("t","e"));

    assertNotNull("Strength of nonexistent glues should not be null", ts.getStrength("fake","fake"));

    assertEquals("Strength of nonexistent glues is 0",0,ts.getStrength("fake","fake"));
} 

/** 
* 
* Method: addPolyTile(PolyTile p) 
* 
*/ 
@Test
public void testAddPolyTile() throws Exception { 
//TODO: Test goes here...
    PolyTile p = new PolyTile();
    String[] emptyGlues = {null, null, null, null};
    p.addTile(0,0, emptyGlues);
    ts.addPolyTile(p);

    Set<PolyTile> testPolyTileList = ts.getTileTypes();
    assertTrue("Polytile list should contain the added polytile",testPolyTileList.contains(p));
    //TODO: figure out if we want to return a reference to the object or a deep copy of the object (tile type list)
    assertSame("Polytile list is the same object as the one returned.", testPolyTileList, ts.getTileTypes());

} 

/** 
* 
* Method: getTemperature() 
* 
*/ 
@Test
public void testGetTemperature() throws Exception { 
    assertEquals("Temperature should equal " + temperature, temperature, ts.getTemperature());
} 

/** 
* 
* Method: setTemperature(int s) 
* 
*/ 
@Test
public void testSetTemperature() throws Exception {
    ts.setTemperature(5);
    assertEquals("Temperature after setting should equal " + 5, 5, ts.getTemperature());
}

/** 
* 
* Method: getTileTypes() 
* 
*/ 
@Test
public void testGetTileTypes() throws Exception { 
//TODO: Test goes here...
    assertNotNull("function should not return null", ts.getTileTypes());
}

} 
