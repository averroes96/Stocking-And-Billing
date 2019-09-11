/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import static Include.Common.getConnection;
import static Include.Common.saveSelectedImage;
import Include.SpecialAlert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author med
 */
public class NewEmployerController implements Initializable {

    @FXML Button upload,sava,cancel;
    @FXML TextField fullname,salary,phone,username;
    @FXML PasswordField password;
    @FXML CheckBox admin;
    @FXML Label image;
    
    Employer employer = new Employer();
    
    SpecialAlert alert = new SpecialAlert();

    File selectedFile = null;    
    
    public void getEmployer(Employer employer){
        
        this.employer = employer;
        
    }
    
    @FXML    
    public void chooseImage()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .JPG .PNG .GIF image", "*.jpg", "*.png", "*.gif")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                image.setText("");
                image.setGraphic(new ImageView(new Image(
                        selectedFile.toURI().toString(), 150, 150, true, true)));
            }
            catch (Exception e) {
                alert.show("Error", "Failed to add Image", Alert.AlertType.ERROR);
            }
        }

    }     
    
    @FXML
    private boolean checkInputs()
    {
        if (fullname.getText().equals("") || salary.getText().equals("") || phone.getText().equals("")) {
            alert.show("Missing required Fields", "Fullname and Salary and Phone fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if(!fullname.getText().matches("^[\\p{L} .'-]+$")){
            alert.show("Fullname not valid", "Please specify a valid fullname !", Alert.AlertType.WARNING);
            return false;              
        }        
        else if(username.equals("") || password.equals("")){
            alert.show("Missing required Fields", "Username and Password fields cannot be empty!", Alert.AlertType.WARNING);
            return false;            
            
        }
        else if(!username.getText().matches("^[a-zA-Z0-9._-]{5,30}$")){
            alert.show("Username error", "Username length must contain at least 5 to 30!", Alert.AlertType.WARNING);
            return false;              
        }
        else if(!password.getText().matches("^[a-zA-Z0-9._-]{7,30}$")){
            alert.show("Password error", "Password length must contain at least 7 to 30!", Alert.AlertType.WARNING);
            return false;              
        }
        else if(!phone.getText().matches("^[5-7]?[0-9]{9}$")){
            alert.show("Phone invalid", "Please enter a valid phone number !", Alert.AlertType.WARNING);
            return false;              
        }        

        try {
            Integer.parseInt(salary.getText());
            if(Integer.parseInt(salary.getText()) > 0 )
            return true;
            else
            {
            alert.show("Salary not valid", "Salary should not have a negative value !", Alert.AlertType.ERROR);
            return false;
            }            
        }
        catch (NumberFormatException e) {
            alert.show("Salary Error", "Salary should be a numeric number (eg: 400, 1000)", Alert.AlertType.ERROR);
            return false;
        }
    }
    
    
    public int usernameExist(){
       
        Connection con = getConnection();
        String query = "SELECT * FROM employers WHERE username = ?";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1, username.getText());
            rs = st.executeQuery();
            int count = 0;
            
            while (rs.next()) {
                ++count;
                
            }
            con.close();
            
            return count;


        }
        catch (SQLException e) {
            alert.show("Error", "IS that you ?", Alert.AlertType.ERROR);
            return 0;
        }       
       
   } 
    
    @FXML
    public void cancel(ActionEvent event) throws IOException {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(employer);
                        mControl.returnMenu("employers");
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();            
            
    }

    @FXML
    private void insertEmployer()
    {
        if (checkInputs()) {
            
            if(usernameExist() == 0){
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;

                if (selectedFile == null) {
                    ps = con.prepareStatement("INSERT INTO employers(fullname, telephone, salary, joined_date, admin, username, password) values(?,?,?,?,?,?,?)");
                }
                else {
                    String createImagePath = saveSelectedImage(selectedFile);
            
                    ps = con.prepareStatement("INSERT INTO employers(fullname, telephone, salary, joined_date, admin, username, password,emp_image) values(?,?,?,?,?,?,?,?)");
                    ps.setString(8, createImagePath);
                }
                
      

                ps.setString(1, fullname.getText());
                ps.setInt(2, Integer.parseInt(phone.getText()));
                ps.setInt(3, Integer.parseInt(salary.getText()));
                if(admin.isSelected())
                ps.setInt(5, 1);
                else{
                ps.setInt(5, 0);
                }
                ps.setString(6, username.getText());
                ps.setString(7, password.getText());                

                LocalDate todayLocalDate = LocalDate.now();
                Date sqlDate = Date.valueOf(todayLocalDate);

                ps.setDate(4, sqlDate);

                ps.executeUpdate();
                con.close();
                
                alert.show("Employer Added", "Employer was successfully added !", Alert.AlertType.INFORMATION);


            }
            catch (NumberFormatException | SQLException e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
            else{
                alert.show("+Error", "This username is already taken by another employer ! Please specify another one", Alert.AlertType.ERROR);
            }
        }

    }        
           
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
