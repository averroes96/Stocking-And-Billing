/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author med
 */
public class NewPaymentController implements Initializable {
    
    @FXML private TextField paidField,restField,referenceField,colorField;
    @FXML Slider sizeSlider;
    @FXML Label currentSize;
    @FXML Button addPayment,cancel;
    
        SpecialAlert alert = new SpecialAlert();
        
        Employer employer = new Employer();
        
        
   public void getEmployer(Employer employer){
       
       this.employer = employer;
       
   }     
        
   public int productExist(){
       
        Connection con = getConnection();
        String query = "SELECT * FROM product WHERE reference = ? AND color = ? AND size = ?";

        PreparedStatement st;
        ResultSet rs;

        try {
            int prodID = 0 ;
            st = con.prepareStatement(query);
            st.setString(1, referenceField.getText());
            st.setString(2, colorField.getText());
            st.setInt(3, (int)sizeSlider.getValue());
            rs = st.executeQuery();
            int count = 0;
            while (rs.next()) {
                prodID = rs.getInt("prod_id");
                ++count;
                
            }
            

            con.close();
            if(count == 0)
                return 0;
            else
                return prodID;


        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            return 0;
        }       
       
   } 
   
       private boolean checkInputs()
    {
        if (paidField.getText().equals("") || restField.getText().equals("") || referenceField.getText().equals("") || colorField.equals("") || sizeSlider.equals("") )  {
            alert.show("Missing required Fields", "Reference and Price and Color and Size fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if(productExist() == 0){
            alert.show("Missing product", "No such product was found ! ", Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            Integer.parseInt(paidField.getText());
            Integer.parseInt(restField.getText());
            
            if(Integer.parseInt(paidField.getText()) > 0  && Integer.parseInt(restField.getText()) > 0 )
            return true;
            else{
            alert.show("Error", "Paid and Rest should not have a negative value !", Alert.AlertType.ERROR);
            return false;
            }
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 2000, 1000)", Alert.AlertType.ERROR);
            return false;
        }
    }
       
    private void resetWindow()
    {
        referenceField.setText("");
        paidField.setText("");
        restField.setText("");        
        colorField.setText("");
        sizeSlider.setValue(36);
        
    }       

    private void insertPayment()
    {
        if (checkInputs()) {
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;
            
                    ps = con.prepareStatement("INSERT INTO sell(sellPrice, sell_date, emp_id, prod_id) values(?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);

                ps.setInt(3, this.employer.getEmpID());
                ps.setInt(1, Integer.parseInt(paidField.getText()) + Integer.parseInt(restField.getText()));
                ps.setInt(4, productExist());

                LocalDate todayLocalDate = LocalDate.now();
                Date sqlDate = Date.valueOf(todayLocalDate);

                ps.setDate(2, sqlDate);

                ps.executeUpdate();
                
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ps = con.prepareStatement("INSERT INTO payment(paid, rest, pDate, sell_id) values(?,?,?,?)");                        
                        
                        ps.setInt(2, Integer.parseInt(restField.getText()));
                        ps.setInt(1, Integer.parseInt(paidField.getText()));
                        ps.setInt(4, generatedKeys.getInt(1));

                        ps.setDate(3, sqlDate);

                        ps.executeUpdate();
                    }
                    else {
                        throw new SQLException("Creating key failed, no ID obtained.");
                    }
                }
                
                ps = con.prepareStatement("UPDATE product SET sold = 1 WHERE prod_id = ?");
                ps.setInt(1, productExist());
                ps.executeUpdate();
                
                con.close();

                resetWindow();
                
                alert.show("Pre-Payment Added", "Your Payment was successfully added !", Alert.AlertType.INFORMATION);

            }
            catch (Exception e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }
    
    public void returnToMain(ActionEvent event) throws IOException {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(employer);
                        mControl.returnMenu("payments");
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();             
            
    }     
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        sizeSlider.valueProperty().addListener((obs, oldval, newVal) -> 
        sizeSlider.setValue(newVal.intValue()));      
        sizeSlider.setMax(45);
        sizeSlider.setMin(15);
        sizeSlider.setValue(36);
        currentSize.setText(String.valueOf(sizeSlider.getValue()));
        currentSize.textProperty().bindBidirectional(sizeSlider.valueProperty(), NumberFormat.getIntegerInstance());
        
        addPayment.setOnAction(Action -> {
            insertPayment();
        });        
        
    }    
    
}
