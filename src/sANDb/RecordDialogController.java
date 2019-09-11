
package sANDb;

import Data.Employer;
import Data.Record;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class RecordDialogController implements Initializable {
    
    @FXML public Button edit,cancel;
    
    private Employer admin = new Employer();
    private Employer employer = new Employer();
    private Record record = new Record();
    
    public void setInfo(Employer selected, Employer admin, Record record){
        
        this.admin = admin;
        this.employer = selected;
        this.record = record;
        
    }
    
    public void toEditPane(Employer selected, Employer admin, Record record){
        
            try {

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateRecord.fxml"));
                Pane root = (Pane)loader.load();
                UpdateRecordController usControl = (UpdateRecordController)loader.getController();
                usControl.getInfo(admin,record,selected);
                usControl.fillTheFields(record);
                Scene scene = new Scene(root);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene);            
                stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(RecordDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }        
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cancel.setOnAction(Action -> {
            
            try {
                ((Node)Action.getSource()).getScene().getWindow().hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("NewRecord.fxml"));
                AnchorPane root = (AnchorPane)loader.load();
                NewRecordController npControl = (NewRecordController)loader.getController();
                npControl.getEmployer(admin,employer);
                Scene scene = new Scene(root);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);
                scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene); 
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(RecordDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        edit.setOnAction(Action -> {
            
            ((Node)Action.getSource()).getScene().getWindow().hide();
            toEditPane(employer,admin,record);

        });
    }    
    
}
