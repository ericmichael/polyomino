package com.asarg.polysim;

import com.asarg.polysim.adapters.graphics.raster.TestCanvas;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    Workspace2 workspace = new Workspace2();

    Stage stage;

    //FXML Components
    @FXML TabPane tabPane;
    @FXML Button btn_fb;
    @FXML Button btn_b;
    @FXML Button btn_play;
    @FXML Button btn_f;
    @FXML Button btn_ff;
    @FXML Button btn_settings;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        btn_fb.setText(String.valueOf('\uf049'));
        btn_b.setText(String.valueOf('\uf048'));
        btn_play.setText(String.valueOf('\uf04b'));
        btn_f.setText(String.valueOf('\uf051'));
        btn_ff.setText(String.valueOf('\uf050'));
        btn_settings.setText(String.valueOf('\uf013'));
    }

    private void updateAttachTime(double time){
        String time_str = String.format("%.4f", time);
    }

    public void setWorkspace(Workspace2 workspace){
        this.workspace = workspace;
    }

    public void setStage(Stage stage){ this.stage = stage; }

    private SimulationNode currentSimulationNode(){
        try {
            return (SimulationNode) tabPane.getSelectionModel().getSelectedItem().getContent();
        }catch(NullPointerException npe){
            return (SimulationNode)null;
        }
    }

    @FXML public void forward(){
        SimulationNode current = currentSimulationNode();
        if(current!=null) current.forward();
    }
    @FXML public void fast_forward(){
        SimulationNode current = currentSimulationNode();
        if(current!=null) current.fast_forward();
    }
    @FXML public void backward(){
        SimulationNode current = currentSimulationNode();
        if(current!=null) current.backward();
    }
    @FXML public void fast_backward(){
        SimulationNode current = currentSimulationNode();
        if(current!=null) current.fast_backward();
    }

    @FXML public void toggle_settings(){
    }


    @FXML
    public void newMenuItem(){
        Tab tab = new Tab();
        tab.setText("Untitled");
        tabPane.getTabs().add(tab);
    }

    @FXML
    public void loadMenuItem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Assembly...");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile!=null) {

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                Unmarshaller unmarshaller;
                unmarshaller = jaxbContext.createUnmarshaller();
                Assembly assembly = (Assembly) unmarshaller.unmarshal(selectedFile);

                workspace.add(assembly, selectedFile);

                Tab tab = new Tab();
                tab.setText(selectedFile.getName());
                final TestCanvas blankCanvas = new TestCanvas(800,600);
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
