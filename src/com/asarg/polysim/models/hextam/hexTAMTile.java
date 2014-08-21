package com.asarg.polysim.models.hextam;

import com.asarg.polysim.models.datam.daTAMTile;
import com.asarg.polysim.xml.PairXmlAdapter;

/**
 * Created by ericmartinez on 8/20/14.
 */
public class hexTAMTile extends daTAMTile {
    public hexTAMTile() {
        System.out.println("hextam tile with no name and infinite counts");
    }
    public hexTAMTile(String n) {
        super(n, false);
    }
    public hexTAMTile(String n, double conc){
        super(n, false, conc);
    }
    public hexTAMTile(String n, int c){
        super(n, false, c);
    }

    public void setGlues(String[] gl){
        prime(gl, 1);
        prime(gl, 4);
        super.setGlues(gl);
    }

    private void prime(String [] gl, int x){
        if(x<gl.length){
            gl[x] = gl[x]+'\u0000';
        }
    }
}
