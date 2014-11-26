package com.asarg.polysim;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GlueEditor extends JPanel implements ActionListener {

    private JPanel boxesPanel = new JPanel();
    public HashMap<Pair<String, String>, Integer> glueFunction;

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints layoutConstraints = new GridBagConstraints();
    List<JTextField[]> textFields = new ArrayList<JTextField[]>();

    GlueEditor(HashMap<Pair<String, String>, Integer> newGlueFunction) {
        // boxes panel with scrollbar inside a panel.
        glueFunction = newGlueFunction;

        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridx = GridBagConstraints.RELATIVE;
        layoutConstraints.gridy = -1;
        drawLabelBoxes();

        boxesPanel.setVisible(true);
        setLayout(new BorderLayout());

        // draw scroll pane
        JScrollPane scrollPane = new JScrollPane(boxesPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        this.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    void setGlueFunction(HashMap<Pair<String, String>, Integer> newGlueFunction){
        glueFunction = newGlueFunction;
    }

    void drawLabelBoxes(){
        boxesPanel.setLayout(layout);

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
            boxesPanel.add(leftParenthesis);

            layout.setConstraints(glue1, layoutConstraints);

            boxesPanel.add(glue1);

            layout.setConstraints(comma, layoutConstraints);
            boxesPanel.add(comma);

            layout.setConstraints(glue2, layoutConstraints);
            boxesPanel.add(glue2);

            layout.setConstraints(rightParenthesis, layoutConstraints);
            boxesPanel.add(rightParenthesis);

            layout.setConstraints(equals, layoutConstraints);
            boxesPanel.add(equals);

            layout.setConstraints(glueStrength, layoutConstraints);
            boxesPanel.add(glueStrength);

            // add the text fields to be read to the list (for future access)
            JTextField[] row = {glue1, glue2, glueStrength};
            textFields.add(row);
        }

        // new glue button
        layoutConstraints.gridy++;
        layoutConstraints.gridx = 1;
        layoutConstraints.gridwidth = 3;
        layoutConstraints.fill = GridBagConstraints.BOTH;
        JButton btnNewGluePair = new JButton();
        btnNewGluePair.setText("New");
        layout.setConstraints(btnNewGluePair, layoutConstraints);
        boxesPanel.add(btnNewGluePair);
        btnNewGluePair.addActionListener(this);
        btnNewGluePair.setActionCommand("newGluePair");

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
            boxesPanel.add(leftParenthesis);

            layout.setConstraints(glue1, layoutConstraints);
            boxesPanel.add(glue1);

            layout.setConstraints(comma, layoutConstraints);
            boxesPanel.add(comma);

            layout.setConstraints(glue2, layoutConstraints);
            boxesPanel.add(glue2);

            layout.setConstraints(rightParenthesis, layoutConstraints);
            boxesPanel.add(rightParenthesis);

            layout.setConstraints(equals, layoutConstraints);
            boxesPanel.add(equals);

            layout.setConstraints(glueStrength, layoutConstraints);
            boxesPanel.add(glueStrength);

            JTextField[] row = {glue1, glue2, glueStrength};
            textFields.add(row);
            boxesPanel.revalidate();
            boxesPanel.repaint();
        }
    }
    public HashMap<Pair<String, String>, Integer> getNewGlueFunction(){
        HashMap<Pair<String, String>, Integer> newGlueFunction = new HashMap<Pair<String,String>, Integer>();

        for (JTextField[] row : textFields){
//            System.out.println(row[0].getText()+' '+row[1].getText()+' '+row[2].getText());
            newGlueFunction.put( new Pair<String,String>(row[0].getText(),row[1].getText()), Integer.parseInt(row[2].getText()) );
        }
        glueFunction = newGlueFunction;
        return glueFunction;
    }
}
