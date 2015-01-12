package com.asarg.polysim;

import com.asarg.polysim.adapters.graphics.raster.Drawer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ericmartinez on 1/12/15.
 */
public class PolyTileCell extends ListCell<PolyTile> {
    @Override
    public void updateItem(PolyTile pt, boolean empty) {
        super.updateItem(pt, empty);
        if (pt != null) {

            BufferedImage iconDrawSpace = new BufferedImage(140, 140, BufferedImage.TYPE_INT_ARGB);
            Graphics2D iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
            iconDrawSpaceGraphics.setClip(0, 0, 140, 140);
            Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, pt);
            WritableImage ptImage = SwingFXUtils.toFXImage(iconDrawSpace, null);
            ImageView view = new ImageView();
            view.setImage(ptImage);
            setAlignment(Pos.CENTER);
            setGraphic(view);
        }
    }
}