package com.asarg.polysim;

import com.asarg.polysim.adapters.graphics.raster.Drawer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ericmartinez on 1/12/15.
 */
public class TileEditorController implements Initializable {

    TileConfiguration tc;
    JPanel polyTilePanel;

    @FXML
    AnchorPane ptAnchorPane;
    @FXML
    SwingNode swingNodeCanvas;
    @FXML
    ListView listview_polytiles;

    Dimension res = new Dimension();

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        listview_polytiles.setCellFactory(new Callback<ListView<PolyTile>, ListCell<PolyTile>>() {
            @Override
            public ListCell<PolyTile> call(ListView<PolyTile> list) {
                final ListCell<PolyTile> ptCell = new PolyTileCell();
                return ptCell;
            }
        });
        if(this.tc!=null){ listview_polytiles.setItems(tc.getTiletypes()); }

        swingNodeCanvas.setContent(polyTilePanel);

        listview_polytiles.getSelectionModel().selectedIndexProperty().addListener( new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(!oldValue.equals(newValue)){

                }
            }
        });
    }

    public void setTileConfiguration(TileConfiguration tc){
        this.tc = tc;
        if(this.tc!=null){ listview_polytiles.setItems(tc.getTiletypes()); }
    }
}
