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
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author med
 */
public class NewRecordController implements Initializable {
    
    

    @FXML public DatePicker date;
    @FXML public TextField paid;
    @FXML public ChoiceBox absPres;
    @FXML public Button addButton,returnButton;
    
    private Employer employer = new Employer();
    private Employer selectedEmployer = new Employer();
    
    public  ObservableList<String> absOrPres = FXCollections.observableArrayList();
    
    private SpecialAlert alert = new SpecialAlert();
    
      
    
    public void getEmployer(Employer employer, Employer selected){
        
        this.employer = employer;
        this.selectedEmployer = selected;
        
    }
    
    public void cancel(ActionEvent event) throws IOException {

                            LocalDate ldt = LocalDate.now() ;
                            ((Node)event.getSource()).getScene().getWindow().hide();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployerRecords.fxml"));
                            AnchorPane root = (AnchorPane)loader.load();
                            EmployerRecordsController erControl = (EmployerRecordsController)loader.getController();
                            erControl.getInfo(selectedEmployer, employer);
                            erControl.fillTheTable(selectedEmployer,ldt.getMonth().getValue());
                            erControl.fillFields(selectedEmployer);
                            erControl.stats(selectedEmployer, ldt.getMonth().getValue());
                            erControl.header.setText(selectedEmployer.getFullname() + "'s Records");
                            Scene scene = new Scene(root);
                            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                            scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                            stage.setScene(scene);                         
                            stage.show();          
            
    }

   public int recordExist(){
       
        Connection con = getConnection();
        String query = "SELECT * FROM employer_payment WHERE emp_id = ? AND pay_date = ?";

        PreparedStatement st;
        ResultSet rs;

        try {
            
            st = con.prepareStatement(query);
            st.setInt(1, selectedEmployer.getEmpID());
            st.setString(2, date.getEditor().getText());
            rs = st.executeQuery();
            
            int count = 0;
            
            while (rs.next()) {
                count = rs.getInt("empay_id");
            }
            

            con.close();
            
            return count;


        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            return 1;
        }       
       
   }
   
    private boolean checkInputs()
    {
        
        try {
            Integer.parseInt(paid.getText());
            if(Integer.parseInt(paid.getText()) > 0){
            return true;
            }
            else{
                alert.show("Error", "Please enter a valid sum ! (eg: 2000, 10000)", Alert.AlertType.ERROR);
                return false;
            }
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Paid salary should be a decimal number (eg: 2000, 10000)", Alert.AlertType.ERROR);
            return false;
        }
    }
    
    private void resetWindow()
    {
        date.getEditor().setText(String.valueOf(LocalDate.now()));
        paid.setText("");
        absPres.getSelectionModel().select(0);
        
    }
    
    public Record getRecord(){
        
        Connection con = getConnection();
        String query = "SELECT * FROM employer_payment WHERE pay_date = ? AND emp_id = ?";

        PreparedStatement st;
        ResultSet rs;
        Record record = new Record();        

        try {
            st = con.prepareStatement(query);
            st.setString(1, date.getEditor().getText());
            st.setInt(2, selectedEmployer.getEmpID());
            rs = st.executeQuery();

            while (rs.next()) {
                
                record.setDate(rs.getDate("pay_date").toString());
                if(rs.getInt("absent") == 0){
                    record.setAbsPres("Present");
                }
                else if(rs.getInt("absent") == 1){
                    record.setAbsPres("Absent");
                }
                record.setPayment(rs.getInt("amount"));
                record.setEmployerID(rs.getInt("emp_id"));
                record.setRecordID(rs.getInt("empay_id"));                

            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
        
        return record;
        
    }    
    
    
    private void insertRecord()
    {
        if (checkInputs()) {
            try {
                
                if(recordExist() == 0){

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;
            
                    ps = con.prepareStatement("INSERT INTO employer_payment(emp_id, amount, pay_date, absent) values(?,?,?,?)");

                ps.setInt(1, selectedEmployer.getEmpID());
                ps.setInt(2, Integer.parseInt(paid.getText()));
                if(absPres.getValue().equals("Present")){
                    ps.setInt(4, 0);
                }
                else{
                    ps.setInt(4, 1);
                }

                ps.setDate(3, Date.valueOf(date.getEditor().getText()));

                ps.executeUpdate();
                
                con.close();

                resetWindow();
                
                alert.show("Record Added", "Your record was successfully added !", Alert.AlertType.INFORMATION);               
                
                }
                else{
                        this.addButton.getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("recordDialog.fxml"));
                        Pane root = (Pane)loader.load();
                        RecordDialogController mControl = (RecordDialogController)loader.getController();
                        mControl.setInfo(selectedEmployer, employer, getRecord());
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();                     
                    
                }

            }
            catch (Exception e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        date.setConverter(dateFormatter());
        date.getEditor().setText(String.valueOf(LocalDate.now()));

        absOrPres.addAll("Present","Absent");
        
        absPres.setItems(absOrPres);
        
        absPres.getSelectionModel().select(0);
        
        addButton.setOnAction(Action -> {
            
            insertRecord();
            
        });
        
        returnButton.setOnAction(Action -> {
            
            try {
                cancel(Action);
            } catch (IOException ex) {
                Logger.getLogger(NewRecordController.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            
        });
        
        
        
    }    
    
}
