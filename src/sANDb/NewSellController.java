/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import static Include.Common.dateFormatter;
import static Include.Common.getConnection;
import static Include.Common.getPrice;
import static Include.Common.minimize;
import static Include.Common.refExist;
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
import javafx.scene.control.DatePicker;
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
public class NewSellController implements Initializable {

    @FXML private TextField price,reference,color;
    @FXML private Slider size;
    @FXML private Label selectedSize,minimize,refStatus,priceStatus;
    @FXML private Button addSell,cancel;
    @FXML public DatePicker date;
    
        SpecialAlert alert = new SpecialAlert();
        
        Employer employer = new Employer();       
      
        

    public void getEmployer(Employer employer){
        
        this.employer = employer; 
    }        
        
   public int productExist(){
       
        Connection con = getConnection();
        String query = "SELECT * FROM product WHERE reference = ? AND color = ? AND size = ? AND sold = 0";

        PreparedStatement st;
        ResultSet rs;

        try {
            int prodID = 0 ;
            st = con.prepareStatement(query);
            st.setString(1, reference.getText());
            st.setString(2, color.getText());
            st.setInt(3, (int)size.getValue());
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
            alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
            return 0;
        }       
       
   }     
        
    
    private boolean checkInputs()
    {
        if (price.getText().equals("") || reference.getText().equals("") || color.equals("") || size.equals("") )  {
            alert.show("Missing required Fields", "Reference and Price and Color and Size fields cannot be empty!", Alert.AlertType.WARNING,false);
            return false;
        }
        else if(productExist() == 0){
            alert.show("Missing product", "No such product was found ! ", Alert.AlertType.WARNING,false);
            return false;
        }
        
        try {
            Integer.parseInt(price.getText());
            if(Integer.parseInt(price.getText()) > 0)
            return true;
            else{
            alert.show("Error", "Price should not have a negative value !", Alert.AlertType.ERROR,false);
            return false;
            }
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 2000, 10000)", Alert.AlertType.ERROR,false);
            return false;
        }
    }
    
    private void resetWindow()
    {
        reference.setText("");
        price.setText("");
        color.setText("");
        size.setValue(36);
        
    }
    
    private void insertSell()
    {
        if (checkInputs()) {
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR,true);
                }

                PreparedStatement ps;
            
                    ps = con.prepareStatement("INSERT INTO sell(sellPrice, sell_date, emp_id, prod_id) values(?,?,?,?)");

                ps.setInt(3, employer.getEmpID());
                ps.setInt(1, Integer.parseInt(price.getText()));
                ps.setInt(4, productExist());

                ps.setDate(2, Date.valueOf(date.getEditor().getText()));

                ps.executeUpdate();
                
                ps = con.prepareStatement("UPDATE product SET sold = 1 WHERE prod_id = ?");
                ps.setInt(1, productExist());
                ps.executeUpdate();
                
                con.close();

                resetWindow();
                
                alert.show("Sell Added", "Your sell was successfully added !", Alert.AlertType.INFORMATION,false);               


            }
            catch (NumberFormatException | SQLException e) {
                alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
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
                        mControl.returnMenu("sells");
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
       
        size.valueProperty().addListener((obs, oldval, newVal) -> 
        size.setValue(newVal.intValue()));      
        size.setMax(45);
        size.setMin(15);
        size.setValue(36);
        selectedSize.setText(String.valueOf(size.getValue()));
        selectedSize.textProperty().bindBidirectional(size.valueProperty(), NumberFormat.getIntegerInstance());
        date.setConverter(dateFormatter());
        date.getEditor().setText(LocalDate.now().toString());
        
        addSell.setOnAction(Action -> {
            insertSell();
        });
        
        minimize.setOnMouseClicked(Action -> {
        
            minimize(Action);
            
        });

        reference.setOnKeyTyped(event -> {
            
            price.setText(String.valueOf(getPrice(reference.getText())));
            if(refExist(reference.getText())){
                
                reference.setStyle("-fx-border-width: 2; -fx-border-color:green;-fx-padding:0 0 0 40");
                refStatus.setVisible(false);
                
            }
            else{
                
                reference.setStyle("-fx-border-width: 2; -fx-border-color:red;-fx-padding:0 0 0 40");
                refStatus.setVisible(true);
                
            }             
            
        });
        reference.setOnKeyReleased(event -> {
            
            price.setText(String.valueOf(getPrice(reference.getText())));
            if(refExist(reference.getText())){
                
                reference.setStyle("-fx-border-width: 2; -fx-border-color:green;-fx-padding:0 0 0 40");
                refStatus.setVisible(false);
                
            }
            else{
                
                reference.setStyle("-fx-border-width: 2; -fx-border-color:red;-fx-padding:0 0 0 40");
                refStatus.setVisible(true);
                
            }            
            
        });
        reference.setOnKeyPressed(event -> {
            
            price.setText(String.valueOf(getPrice(reference.getText())));
            if(refExist(reference.getText())){
                
                reference.setStyle("-fx-border-width: 2; -fx-border-color:green;-fx-padding:0 0 0 40");
                refStatus.setVisible(false);
                
            }
            else{
                
                reference.setStyle("-fx-border-width: 2; -fx-border-color:red;-fx-padding:0 0 0 40");
                refStatus.setVisible(true);
                
            }            
            
        });
        
        price.setOnKeyReleased(event -> {
            
        if (!price.getText().matches("^[1-9]?[0-9]*$")) {
            priceStatus.setVisible(true);
            price.setStyle("-fx-border-width: 2; -fx-border-color:red;-fx-padding:0 0 0 40");
        }
        else{
            priceStatus.setVisible(false);
            price.setStyle("-fx-border-width: 2; -fx-border-color:green;-fx-padding:0 0 0 40");
        }             
            
        });
        price.setOnKeyPressed(event -> {
            
        if (!price.getText().matches("^[1-9]?[0-9]*$")) {
            priceStatus.setVisible(true);
            price.setStyle("-fx-border-width: 2; -fx-border-color:red;-fx-padding:0 0 0 40");
        }
        else{
            priceStatus.setVisible(false);
            price.setStyle("-fx-border-width: 2; -fx-border-color:green;-fx-padding:0 0 0 40");
        }            
            
        });
        price.setOnKeyTyped(event -> {
            
        if (!price.getText().matches("^[1-9]?[0-9]*$")) {
            priceStatus.setVisible(true);
            price.setStyle("-fx-border-width: 2; -fx-border-color:red;-fx-padding:0 0 0 40");
        }
        else{
            priceStatus.setVisible(false);
            price.setStyle("-fx-border-width: 2; -fx-border-color:green;-fx-padding:0 0 0 40");
        }            
            
        });         
        
        
        
    }    
    
}
