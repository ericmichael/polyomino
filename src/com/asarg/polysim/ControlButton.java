package com.asarg.polysim;

import java.awt.*;

/**
 * Created by ericmartinez on 8/22/14.
 */
public class ControlButton extends IconButton {
    public ControlButton(String type) {
        if(type.equals("play")){
            setText(String.valueOf('\uf04b'));
        }else if(type.equals("forward")){
            setText(String.valueOf('\uf051'));
        }else if(type.equals("backward")){
            setText(String.valueOf('\uf04a'));
        }else if(type.equals("fast-forward")){
            setText(String.valueOf('\uf050'));
        }else if(type.equals("fast-backward")){
            setText(String.valueOf('\uf049'));
        }

        setForeground(Color.BLACK);
    }
}