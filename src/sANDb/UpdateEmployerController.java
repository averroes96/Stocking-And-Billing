
package sANDb;

import Data.Employer;
import Include.Common;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author med
 */
public class UpdateEmployerController implements Initializable {

    @FXML Button update,save,cancel;
    @FXML TextField fullname,phone,salary;
    @FXML Label image;
    @FXML DatePicker joinedDate;
    @FXML CheckBox admin;
    
    SpecialAlert alert = new SpecialAlert();
    
    Employer employer = new Employer();
    
    Employer selectedEmployer = new Employer();
        
    final String dateFormat = "yyyy-MM-dd";
    
    File selectedFile = null;
        
        
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

    public void getInfo(Employer employer, Employer selected){
        
        this.employer = employer;
        this.selectedEmployer = selected;
    }
    
    public void fillFields(Employer selectedEmployer){       
        
        fullname.setText(selectedEmployer.getFullname());
        phone.setText(String.valueOf(selectedEmployer.getPhone()));
        salary.setText(String.valueOf(selectedEmployer.getSalary()));
        
        if (selectedEmployer.getImage().equals("")) {
            image.setGraphic(null);
            image.setText("No image found");
            System.out.print("image");
            System.out.print(selectedEmployer.getImage());
        }
        else {
            image.setText("");
            image.setGraphic(new ImageView(new Image(
                    new File(selectedEmployer.getImage()).toURI().toString(),
                    150, 150, true, true)));
        }

        joinedDate.getEditor().setText(selectedEmployer.getJoinDate());

        if(selectedEmployer.getAdmin() == 1){
            admin.setSelected(true);
        }
        else
            admin.setSelected(false);

    }


    public void updateImage()
    {

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .JPG .PNG .GIF image", "*.jpg", "*.png", "*.gif")
        );

       
        selectedFile = fileChooser.showOpenDialog(null);

       
        if (selectedFile != null) {
            
            try {
               
                String createImagePath = Common.saveSelectedImage(selectedFile);

                Common.deleteImage(selectedEmployer.getImage());

                selectedEmployer.setImage(createImagePath);

                image.setText("");
                image.setGraphic(new ImageView(new Image(
                    selectedFile.toURI().toString(), 224, 224, true, true)));

            }
            catch (Exception ex) {
                alert.show("Error", "Failed to update Image", Alert.AlertType.ERROR);
            }
        }

    }
    
    private boolean checkInputs()
    {
        if (fullname.getText().trim().equals("") || salary.getText().trim().equals("") || phone.getText().trim().equals("")) {
            alert.show("Missing required Fields", "Fullname and Salary and Phone fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if(!fullname.getText().matches("^[\\p{L} .'-]+$")){
            alert.show("Fullname not valid", "Please specify a valid fullname !", Alert.AlertType.WARNING);
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
            alert.show("Salary Error", "Price should be a decimal number (eg: 400, 1000)", Alert.AlertType.ERROR);
            return false;
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
                
            
                ps = con.prepareStatement("UPDATE employers SET salary = ?, joined_date = ?, telephone = ?, admin = ?, fullname = ?, emp_image = ? WHERE emp_id = ?");

                ps.setInt(3, Integer.parseInt(phone.getText()));
                ps.setInt(1, Integer.parseInt(salary.getText()));
                if(admin.isSelected()){                
                ps.setInt(4, 1);
                selectedEmployer.setAdmin(1);
                }
                else{
                ps.setInt(4, 0);
                selectedEmployer.setAdmin(0);
                }
                ps.setDate(2, Date.valueOf(joinedDate.getEditor().getText()));
                ps.setString(6, this.selectedEmployer.getImage());
                ps.setString(5, fullname.getText());
                ps.setInt(7, this.selectedEmployer.getEmpID());                
                ps.executeUpdate();
                
                con.close();

                alert.show("Employer Updated", "This Employer was successfully updated !", Alert.AlertType.INFORMATION);
                

                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        if(employer.getUsername().equals(selectedEmployer.getUsername())){

                        mControl.getEmployer(selectedEmployer);
                        System.out.println(selectedEmployer.getAdmin());
                        }
                        else{

                        mControl.getEmployer(employer);    
                        }
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

        joinedDate.setConverter(dateFormatter());
        
        fillFields(selectedEmployer);
        
        update.setOnAction(Action -> {
            updateImage();
        });
        
        save.setOnAction(Action -> {
            updateEmployer(Action);
        });
        
        cancel.setOnAction(Action -> {
            try {
                ((Node)Action.getSource()).getScene().getWindow().hide();
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
            } catch (IOException ex) {
                Logger.getLogger(UpdateEmployerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });        


    }    


    
}
