/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import static Include.Common.getConnection;
import static Include.Common.minimize;
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
public class NewSellController implements Initializable {

    @FXML private TextField price,reference,color;
    @FXML private Slider size;
    @FXML private Label selectedSize,minimize;
    @FXML private Button addSell,cancel;
    
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
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            return 0;
        }       
       
   }     
        
    
    private boolean checkInputs()
    {
        if (price.getText().equals("") || reference.getText().equals("") || color.equals("") || size.equals("") )  {
            alert.show("Missing required Fields", "Reference and Price and Color and Size fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if(productExist() == 0){
            alert.show("Missing product", "No such product was found ! ", Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            Integer.parseInt(price.getText());
            if(Integer.parseInt(price.getText()) > 0)
            return true;
            else{
            alert.show("Error", "Price should not have a negative value !", Alert.AlertType.ERROR);
            return false;
            }
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 2000, 10000)", Alert.AlertType.ERROR);
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
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;
            
                    ps = con.prepareStatement("INSERT INTO sell(sellPrice, sell_date, emp_id, prod_id) values(?,?,?,?)");

                ps.setInt(3, employer.getEmpID());
                ps.setInt(1, Integer.parseInt(price.getText()));
                ps.setInt(4, productExist());

                LocalDate todayLocalDate = LocalDate.now();
                Date sqlDate = Date.valueOf(todayLocalDate);

                ps.setDate(2, sqlDate);

                ps.executeUpdate();
                
                ps = con.prepareStatement("UPDATE product SET sold = 1 WHERE prod_id = ?");
                ps.setInt(1, productExist());
                ps.executeUpdate();
                
                con.close();

                resetWindow();
                
                alert.show("Sell Added", "Your sell was successfully added !", Alert.AlertType.INFORMATION);               


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
        
        addSell.setOnAction(Action -> {
            insertSell();
        });
        
        minimize.setOnMouseClicked(Action -> {
        
            minimize(Action);
            
        });        

        
        
    }    
    
}
