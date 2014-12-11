package com.asarg.polysim.actions;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by ericmartinez on 12/11/14.
 */
public class PlayAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        System.out.println("Action [" + e.getActionCommand() + "] performed!");
    }
}
