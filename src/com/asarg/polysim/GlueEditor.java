package com.asarg.polysim;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GlueEditor extends JPanel implements ActionListener {

    private HashMap<Pair<String, String>, Integer> glueFunction;

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints layoutConstraints = new GridBagConstraints();

    GlueEditor(HashMap<Pair<String, String>, Integer> newGlueFunction) {
        glueFunction = newGlueFunction;

        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridx = GridBagConstraints.RELATIVE;
        layoutConstraints.gridy = -1;
        drawLabelBoxes();

        // draw scroll pane
        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);
        setVisible(true);
    }

    void setGlueFunction(HashMap<Pair<String, String>, Integer> newGlueFunction){
        glueFunction = newGlueFunction;
    }

    void drawLabelBoxes(){
        setLayout(layout);

        // label lines for existing glue pairs in function
        for (Map.Entry<Pair<String,String>,Integer> entry : glueFunction.entrySet()){
            JLabel leftParenthesis = new JLabel(" ( ");
            JTextField glue1 = new JTextField();
            JLabel comma = new JLabel(" , ");
            JTextField glue2 = new JTextField();
            JLabel rightParenthesis = new JLabel(" ) ");
            JLabel equals = new JLabel(" = ");
            JTextField glueStrength = new JTextField();

            String g1 = entry.getKey().getKey();
            String g2 = entry.getKey().getValue();
            String g1g2Strength = entry.getValue().toString();
            glue1.setText(g1);
            glue2.setText(g2);
            glueStrength.setText(g1g2Strength);
            glue1.setHorizontalAlignment(JTextField.CENTER);
            glue2.setHorizontalAlignment(JTextField.CENTER);
            glueStrength.setHorizontalAlignment(JTextField.CENTER);

            glue1.setColumns(10);
            glue2.setColumns(10);
            glueStrength.setColumns(5);

            layoutConstraints.gridy++;

            layout.setConstraints(leftParenthesis, layoutConstraints);
            add(leftParenthesis);

            layout.setConstraints(glue1, layoutConstraints);
            add(glue1);

            layout.setConstraints(comma, layoutConstraints);
            add(comma);

            layout.setConstraints(glue2, layoutConstraints);
            add(glue2);

            layout.setConstraints(rightParenthesis, layoutConstraints);
            add(rightParenthesis);

            layout.setConstraints(equals, layoutConstraints);
            add(equals);

            layout.setConstraints(glueStrength, layoutConstraints);
            add(glueStrength);
        }

        // new glue button
        layoutConstraints.gridy++;
        layoutConstraints.gridx = 1;
        JButton btnNewGluePair = new JButton();
        btnNewGluePair.setText("New");
        layout.setConstraints(btnNewGluePair, layoutConstraints);
        add(btnNewGluePair);
        btnNewGluePair.addActionListener(this);
        btnNewGluePair.setActionCommand("newGluePair");

        // update glue function button
        layoutConstraints.gridx = 4;
        layoutConstraints.gridwidth = 3;
        JButton btnUpdateGlueFunction = new JButton();
        btnUpdateGlueFunction.setText("Update");
        layout.setConstraints(btnUpdateGlueFunction, layoutConstraints);
        add(btnUpdateGlueFunction);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("newGluePair")){
            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridx = 1;
            layoutConstraints.gridx = GridBagConstraints.RELATIVE;

            //add(new JLabel("thing"));
            System.out.println("Adding new box");
            // add a new field when the new button is pressed
            JLabel leftParenthesis = new JLabel(" ( ");
            JTextField glue1 = new JTextField();
            JLabel comma = new JLabel(" , ");
            JTextField glue2 = new JTextField();
            JLabel rightParenthesis = new JLabel(" ) ");
            JLabel equals = new JLabel(" = ");
            JTextField glueStrength = new JTextField();

            glue1.setHorizontalAlignment(JTextField.CENTER);
            glue2.setHorizontalAlignment(JTextField.CENTER);
            glueStrength.setHorizontalAlignment(JTextField.CENTER);

            glue1.setColumns(10);
            glue2.setColumns(10);
            glueStrength.setColumns(5);

            layoutConstraints.gridy++;

            layout.setConstraints(leftParenthesis, layoutConstraints);
            add(leftParenthesis);

            layout.setConstraints(glue1, layoutConstraints);
            add(glue1);

            layout.setConstraints(comma, layoutConstraints);
            add(comma);

            layout.setConstraints(glue2, layoutConstraints);
            add(glue2);

            layout.setConstraints(rightParenthesis, layoutConstraints);
            add(rightParenthesis);

            layout.setConstraints(equals, layoutConstraints);
            add(equals);

            layout.setConstraints(glueStrength, layoutConstraints);
            add(glueStrength);

            revalidate();
            repaint();
        }
    }
}
