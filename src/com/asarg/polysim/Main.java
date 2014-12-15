package com.asarg.polysim;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Random;

public class Main {

    public static void main(String args[]) throws JAXBException {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               /* try {
                   for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    // If Nimbus is not available, you can set the GUI to another look and feel.
                }*/
                Assembly asm = new Assembly();
                Workspace w = new Workspace(asm);
            }
        });

    }
}