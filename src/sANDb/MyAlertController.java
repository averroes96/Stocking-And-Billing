/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author med
 */
public class MyAlertController implements Initializable {
    
    @FXML public ImageView icon;
    @FXML public Label title,message,close;
    @FXML public Button cancel;
    
    
    public void setAlert(String icon, String title, String message){
        
        this.icon.setImage(new Image(new File("@images/" + icon +".png").toURI().toString(),35,35,true,true));
        this.title.setText(title);
        this.message.setText(message);
        
        System.out.println(new File("@images/" + icon +".png").toURI().toString());
        
    }
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
