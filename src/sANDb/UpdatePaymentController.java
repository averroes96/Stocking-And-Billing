/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import Data.Payment;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author med
 */
public class UpdatePaymentController implements Initializable {
    
    @FXML private Button saveButton,cancelButton;
    @FXML private TextField paidField,restField;
    @FXML private DatePicker dateField;  

    Employer employer = new Employer();
    
    Payment payment = new Payment();
    
    SpecialAlert alert = new SpecialAlert();
        
    final String dateFormat = "yyyy-MM-dd";   
        
        
    public StringConverter dateFormatter()
    {
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                }
                return null;
            }
        };
        return converter;
    }     
    
    
    public void getInfo(Employer employer, Payment payment){
        
        this.employer = employer;
        this.payment = payment;
    }
    
    public void fillFields(Payment payment){       
        
        paidField.setText(String.valueOf(payment.getPaid()));
        restField.setText(String.valueOf(payment.getRest()));
        dateField.getEditor().setText(payment.getPayDate());

    }
    
    @FXML
    public void cancel(ActionEvent event) throws IOException {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(employer);
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();            
            
    }
       
    private boolean checkInputs()
    {
        if (restField.getText().equals("") || paidField.getText().equals(""))  {
            alert.show("Missing required Fields", "Please precise both paid price and what rest!", Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            Integer.parseInt(restField.getText());
            Integer.parseInt(paidField.getText());
            return true;
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 2000, 1000)", Alert.AlertType.ERROR);
            return false;
        }
    }
    

    public void updatePay(ActionEvent event) {

        if (checkInputs()) {
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;
            
                ps = con.prepareStatement("UPDATE payment SET paid = ?, rest = ?, pDate = ? WHERE pay_id = ?");

                ps.setInt(1, Integer.parseInt(paidField.getText()));
                ps.setInt(2, Integer.parseInt(restField.getText()));
                ps.setDate(3, Date.valueOf(dateField.getEditor().getText()));
                ps.setInt(4, this.payment.getPayID());
                ps.executeUpdate();
                
                ps = con.prepareStatement("UPDATE sell SET sellPrice = ? WHERE sell_id = ?");
                ps.setInt(1, Integer.parseInt(restField.getText()) + Integer.parseInt(paidField.getText()));
                ps.setInt(2,payment.getSell().getSellID());
                ps.executeUpdate();
                
                con.close();

                alert.show("Payment and sell Updated", "Your pre-payment was successfully updated !", Alert.AlertType.INFORMATION);
                

                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(this.employer);
                        mControl.returnMenu("payments");
                        Scene scene = new Scene(root);
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
        
        fillFields(payment);
        
        dateField.setConverter(dateFormatter()); 
    
        
        
    }    
    
}
