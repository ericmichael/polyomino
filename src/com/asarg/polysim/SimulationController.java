package com.asarg.polysim;

import com.asarg.polysim.adapters.graphics.raster.TestCanvas;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ericmartinez on 1/5/15.
 */
public class SimulationController implements Initializable {
    Stage stage;

    //FXML Components
    @FXML
    TabPane tabPane;
    @FXML
    StackPane inspector;
    @FXML
    BorderPane borderPane;
    @FXML
    Button btn_fb;
    @FXML
    Button btn_b;
    @FXML
    Button btn_play;
    @FXML
    Button btn_f;
    @FXML
    Button btn_ff;
    @FXML
    ToggleButton btn_settings;
    @FXML
    Button btn_help;
    @FXML
    Label lbl_left_status;
    @FXML
    Label lbl_right_status;

    private boolean inspecting = false;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        btn_fb.setText(String.valueOf('\uf049'));
        btn_b.setText(String.valueOf('\uf048'));
        btn_play.setText(String.valueOf('\uf04b'));
        btn_f.setText(String.valueOf('\uf051'));
        btn_ff.setText(String.valueOf('\uf050'));
        btn_settings.setText(String.valueOf('\uf013'));
        btn_help.setText(String.valueOf('\uf059'));
        inspector.managedProperty().bind(inspector.visibleProperty());
        inspector.setVisible(false);
        btn_settings.setSelected(false);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        if (t1 != null) {
                            SimulationNode current = (SimulationNode) t1.getContent();
                            if (current != null) {
                                lbl_left_status.textProperty().bind(current.left_status);
                                lbl_right_status.textProperty().bind(current.right_status);
                            }
                        }
                    }
                }
        );

        //TODO: Fix this grossness
        borderPane.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            final KeyCombination enter = new KeyCodeCombination(KeyCode.ENTER);
            final KeyCombination pgup = new KeyCodeCombination(KeyCode.PAGE_UP);
            final KeyCombination pgdown = new KeyCodeCombination(KeyCode.PAGE_DOWN);
            final KeyCombination left = new KeyCodeCombination(KeyCode.LEFT);
            final KeyCombination right = new KeyCodeCombination(KeyCode.RIGHT);
            final KeyCombination escape = new KeyCodeCombination(KeyCode.ESCAPE);

            public void handle(javafx.scene.input.KeyEvent t) {
                SimulationNode cn = currentSimulationNode(); //current simulationnode in tab

                if (cn != null) {
                    if (enter.match(t)) {
                        if (cn.currentFrontierAttachment != null) {
                            FrontierElement fe = new FrontierElement(cn.currentFrontierAttachment.getLocation(),
                                    cn.currentFrontierAttachment.getOffset(), cn.currentFrontierAttachment.getPolyTile(), 4);
                            cn.resetFrontier();
                            cn.assembly.attach(fe);
                            cn.frontier = cn.assembly.calculateFrontier();
                            cn.placeFrontierOnGrid();
                            cn.exitFrontierMode();
                            cn.drawGrid();
                        }
                    } else if (pgup.match(t)) {
                        cn.zoomInDraw();
                    } else if (pgdown.match(t)) {
                        cn.zoomOutDraw();
                    } else if (left.match(t)) {
                        cn.step(-1, true);
                    } else if (right.match(t)) {
                        cn.step(1, true);
                    } else if (escape.match(t)) {
                        cn.exitFrontierMode();
                        cn.getCanvas().reset();
                        cn.frontier = cn.assembly.calculateFrontier();
                        cn.placeFrontierOnGrid();
                        cn.drawGrid();
                    }
                }
            }
        });

    }

    private void updateAttachTime(double time) {
        String time_str = String.format("%.4f", time);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private SimulationNode currentSimulationNode() {
        try {
            return (SimulationNode) tabPane.getSelectionModel().getSelectedItem().getContent();
        } catch (NullPointerException npe) {
            return (SimulationNode) null;
        }
    }

    @FXML
    public void forward() {
        SimulationNode current = currentSimulationNode();
        if (current != null) current.forward();
    }

    @FXML
    public void fast_forward() {
        SimulationNode current = currentSimulationNode();
        if (current != null) current.fast_forward();
    }

    @FXML
    public void backward() {
        SimulationNode current = currentSimulationNode();
        if (current != null) current.backward();
    }

    @FXML
    public void fast_backward() {
        SimulationNode current = currentSimulationNode();
        if (current != null) current.fast_backward();
    }

    @FXML
    public void play() {
        SimulationNode current = currentSimulationNode();
        if (current != null) current.play();
    }

    @FXML
    public void toggle_settings() {
        inspecting = !inspecting;
        inspector.setVisible(inspecting);
        btn_settings.setSelected(inspecting);
    }


    @FXML
    public void newMenuItem() {
        Tab tab = new Tab();
        tab.setText("Untitled");
        tabPane.getTabs().add(tab);
    }

    @FXML
    public void loadMenuItem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Assembly...");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                Unmarshaller unmarshaller;
                unmarshaller = jaxbContext.createUnmarshaller();
                Assembly assembly = (Assembly) unmarshaller.unmarshal(selectedFile);

                Tab tab = new Tab();
                tab.setText(selectedFile.getName());
                final TestCanvas blankCanvas = new TestCanvas(800, 600);
                final SimulationNode simulationNode = new SimulationNode(assembly, blankCanvas);
                simulationNode.drawGrid();
                tab.setContent(simulationNode);
                tabPane.getTabs().add(tab);

                //SimulationWindow tcf = new SimulationWindow(800, 600, assembly);
            } catch (javax.xml.bind.JAXBException jaxbe) {
                javax.swing.JOptionPane.showMessageDialog(null, "Failed to load assembly");
            }
        }
    }
}
