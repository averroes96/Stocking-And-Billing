/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import Data.Record;
import static Include.Common.dateFormatter;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class UpdateRecordController implements Initializable {

    @FXML public DatePicker date;
    @FXML public TextField paid;
    @FXML public ChoiceBox absPres;
    @FXML public Button returnButton,saveButton;
    
    private Employer employer = new Employer();
    private Employer selectedEmp = new Employer();
    private Record selectedRecord = new Record(); 
    
    public  ObservableList<String> absOrPres = FXCollections.observableArrayList();
    
    private SpecialAlert alert = new SpecialAlert();
    
    public void fillTheFields(Record record){
        
        date.getEditor().setText(record.getDate());
        paid.setText(String.valueOf(record.getPayment()));
        absPres.setValue(record.getAbsPres());
        
    }
    
    public void getInfo(Employer employer, Record selected, Employer selectedOne){
        
        this.employer = employer;
        this.selectedRecord = selected;
        this.selectedEmp = selectedOne;
        
    }

    private boolean checkInputs()
    {
        if (Integer.parseInt(paid.getText()) < 0) {
            alert.show("Paid Value", "Paid Value must be a positive number !", Alert.AlertType.WARNING);
            return false;
        }     
       
        
        try {
            Integer.parseInt(paid.getText());
            return true;
        }
        catch (NumberFormatException e) {
            alert.show("Salary Error", "Paid value should be a decimal number (eg: 400, 1000)", Alert.AlertType.ERROR);
            return false;
        }
    }
    
   public int recordExist(){
       
        Connection con = getConnection();
        String query = "SELECT * FROM employer_payment WHERE emp_id = ? AND pay_date = ?";

        PreparedStatement st;
        ResultSet rs;

        try {
            
            st = con.prepareStatement(query);
            st.setInt(1, selectedRecord.getEmployerID());
            st.setString(2, date.getEditor().getText());
            rs = st.executeQuery();
            
            int count = 0;
            
            while (rs.next()) {
                ++count;
            }
            

            con.close();
            if(count == 0)
                return 0;
            else
                return 1;


        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            return 1;
        }       
       
   }
   
    public void updateEmployer(ActionEvent event) {

        if (checkInputs()) {
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;
                
            
                ps = con.prepareStatement("UPDATE employer_payment SET amount = ?, pay_date = ?, absent = ? WHERE empay_id = ?");

                ps.setInt(1, Integer.parseInt(paid.getText()));
                if(absPres.getValue().equals("Absent")){                
                ps.setInt(3, 1);
                }
                else{
                ps.setInt(3, 0);
                }
                ps.setDate(2, Date.valueOf(date.getEditor().getText()));
                ps.setInt(4, selectedRecord.getRecordID());
                ps.executeUpdate();
                
                con.close();

                alert.show("Record Updated", "This record was successfully updated !", Alert.AlertType.INFORMATION);
                

                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployerRecords.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        EmployerRecordsController mControl = (EmployerRecordsController)loader.getController();
                        Scene scene = new Scene(root);
                        mControl.getInfo(selectedEmp, employer);
                        mControl.fillTheTable(selectedEmp,LocalDate.now().getMonth().getValue());
                        mControl.fillFields(selectedEmp);
                        mControl.stats(selectedEmp, LocalDate.now().getMonth().getValue());
                        mControl.header.setText(selectedEmp.getFullname() + "'s Records");
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();                
                        ((Node)event.getSource()).getScene().getWindow().hide();                
                


            }
            catch (Exception e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }        
        
    }   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        date.setConverter(dateFormatter());
        
        absOrPres.addAll("Present","Absent");
        
        absPres.setItems(absOrPres);
        
        returnButton.setOnAction(Action -> {
            try {
                ((Node)Action.getSource()).getScene().getWindow().hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployerRecords.fxml"));
                AnchorPane root = (AnchorPane)loader.load();
                EmployerRecordsController mControl = (EmployerRecordsController)loader.getController();
                mControl.getInfo(selectedEmp, employer);
                mControl.fillTheTable(selectedEmp,LocalDate.now().getMonth().getValue());
                mControl.fillFields(selectedEmp);                
                Scene scene = new Scene(root);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);
                scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene);  
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(UpdateEmployerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });        

        
        
    }    
    
}
