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
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author med
 */
public class ChangePassController implements Initializable {

    @FXML public Label close,min;
    @FXML public TextField current,newPass,repeat;
    @FXML public Button save;
    
    private Employer employer = new Employer();
    
    private SpecialAlert alert = new SpecialAlert();
    
    public void getEmployer(Employer emp){
        this.employer = emp;
    }
    
    private boolean checkPassword(){
    
        Connection con = getConnection();
        String query = "SELECT password FROM employers WHERE emp_id = ?";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setInt(1, employer.getEmpID());
            rs = st.executeQuery();
            
            if (rs.next()) {
                
                return rs.getString("password").equals(current.getText());
                
            }
            con.close();
            
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
        
        return false;
    
    }
    
    
    private boolean checkInputs()
    {
        if (current.getText().trim().equals("") || newPass.getText().trim().equals("") || repeat.getText().trim().equals("")) {
            alert.show("Missing required Fields", "Please fill all the required fields !", Alert.AlertType.WARNING);
            return false;
        }
        else if(!newPass.getText().matches("^[a-zA-Z0-9._-]{7,30}$")){
            alert.show("Password error", "Password length must contain at least 7 to 30 character!", Alert.AlertType.WARNING);
            return false;              
        }
        else if(!newPass.getText().equals(repeat.getText())){
            alert.show("Passwords do not match", "New password do not match! ", Alert.AlertType.ERROR);
            return false;           
        }
        
        return true;
    }
    
    public void changePassword(){
        
        if (checkInputs()) {
            
            if(checkPassword()){
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;

            
                    ps = con.prepareStatement("UPDATE employers SET password = ? WHERE emp_id = ?");
                
                
      

                ps.setString(1, newPass.getText());
                ps.setInt(2, employer.getEmpID());

                ps.executeUpdate();
                con.close();
                
                alert.show("Password updated", employer.getFullname() + "'s password was successfully updated !", Alert.AlertType.INFORMATION);
                save.getScene().getWindow().hide();


            }
            catch (NumberFormatException | SQLException e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
            else{
                alert.show("Wrong Password", "Wrong password ! Make sure the the current password is right", Alert.AlertType.ERROR);
            }
        }        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        min.setOnMouseClicked(Action -> {
            
            minimize(Action);
            
        });
        
        close.setOnMouseClicked(Action -> {
            
            ((Node)Action.getSource()).getScene().getWindow().hide();
            
            
        });
        // TODO
    }    
    
}
