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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author med
 */
public class LoginController implements Initializable {
    
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Button loginButton;
    @FXML Label closeButton,reduceButton;
    
    SpecialAlert alert = new SpecialAlert();
    
    private double xOffset = 0;
    private double yOffset = 0;     



    public Employer getEmployer(String username, String password)
    {
        Connection con = getConnection();
        String query = "SELECT * FROM employers WHERE username = ? AND password = ?";

        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement(query);
            ps.setString(1, username);        
            ps.setString(2, password);
            
            rs = ps.executeQuery();
            
                Employer employer = new Employer();            

            while (rs.next()) {

                employer.setEmpID(rs.getInt("emp_id"));
                employer.setFullname(rs.getString("fullname"));
                employer.setSalary(rs.getInt("salary"));
                employer.setPhone(rs.getInt("telephone"));
                employer.setAdmin(rs.getInt("admin"));
                employer.setJoinDate(String.valueOf(rs.getDate("joined_date")));
                employer.setPassword(password);
                employer.setUsername(username);
                
                return employer;
            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
        return null;
    }    
    
    
    public boolean exists(String username, String password) throws SQLException{
        
        Connection con = getConnection();
        String query = "SELECT * FROM employers WHERE username = ? AND password = ? AND active = 1";

        PreparedStatement stmt;
        ResultSet rs;
     
        try {

            stmt = con.prepareStatement(query);
            stmt.setString(1, username);        
            stmt.setString(2, password);
            
            rs = stmt.executeQuery(); 
            
            return rs.next();
                
  
        } catch (SQLException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    @FXML
    public void login(ActionEvent event){
        
            try {
                if(exists(username.getText(),password.getText())){
                    try {
                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(getEmployer(username.getText(), password.getText()));
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();
                        root.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                xOffset = event.getSceneX();
                                yOffset = event.getSceneY();
                            }
                        });
                        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                stage.setX(event.getScreenX() - xOffset);
                                stage.setY(event.getScreenY() - yOffset);
                            }
                        });                          
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                else{
                    
                 alert.show("Error", "Wrong username or password !", Alert.AlertType.ERROR);
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }        
        
        
    }
    
    @FXML
    public void minimize(MouseEvent event){
        
        ((Stage)((Label)event.getSource()).getScene().getWindow()).setIconified(true);
        
    }
    
    @FXML
    public void close(MouseEvent event){
        
        System.exit(0);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        
    }    
    
}
