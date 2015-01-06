package com.asarg.polysim;

import com.asarg.polysim.adapters.graphics.raster.TestCanvas;
import java.util.ArrayList;
import java.io.File;

/**
 * Created by ericmartinez on 12/11/14.
 */

public class Workspace2{
    public ArrayList<Assembly> assemblies = new ArrayList<Assembly>();
    public ArrayList<File> files = new ArrayList<File>();

    public Workspace2(){

    }

    public Workspace2(Assembly asm){
        add(asm);
    }

    public void add(Assembly asm){
        assemblies.add(asm);
        files.add(new File("Untitled"));
    }

    public void add(Assembly asm, File f){
        assemblies.add(asm);
        files.add(f);
    }
}
