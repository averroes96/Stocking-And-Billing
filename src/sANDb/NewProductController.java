/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import static Include.Common.getConnection;
import static Include.Common.minimize;
import static Include.Common.saveSelectedImage;
import Include.SpecialAlert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
public class NewProductController implements Initializable {
    
    
        @FXML public TextField refField,brandField,priceField,colorField;
        @FXML public ChoiceBox catField;
        @FXML public Label imgField,selectedSize,minimize;
        @FXML public Button addProduct,addPhoto,cancel;
        @FXML public Slider sizeField;
        @FXML public Spinner<Integer> minSize,maxSize;

        SpecialAlert alert = new SpecialAlert();

        File selectedFile = null;
        
        SpinnerValueFactory<Integer> minVals = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 45);
        SpinnerValueFactory<Integer> maxVals = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 45); 
        
    public ObservableList<Integer> sizes = FXCollections.observableArrayList();
    
    public ObservableList<String> categories = FXCollections.observableArrayList();
    
    public Employer employer = new Employer();

    
    public void getEmployer(Employer employer){
        
        this.employer = employer; 
    }
    
    @FXML    
    private void chooseImage()
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .JPG .PNG .GIF image", "*.jpg", "*.png", "*.gif")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                imgField.setText("");
                imgField.setGraphic(new ImageView(new Image(
                        selectedFile.toURI().toString(), 224, 224, true, true)));
            }
            catch (Exception e) {
                alert.show("Error", "Failed to add Image", Alert.AlertType.ERROR);
            }
        }

    }        
    
    @FXML
    private boolean checkInputs()
    {
        if (refField.getText().equals("") && priceField.getText().equals("") && colorField.equals("") ) {
            alert.show("Missing required Fields", "Reference and Price and Color and Size fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if (refField.getText().trim().equals("")) {
            alert.show("Missing required Fields", "Please enter product's reference", Alert.AlertType.WARNING);
            return false;
        }
        else if (priceField.getText().trim().equals("")) {
            alert.show("Missing required Fields", "Please enter product's price", Alert.AlertType.WARNING);
            return false;
        }
        else if (colorField.getText().trim().equals("")) {
            alert.show("Missing required Fields", "Please enter product's color", Alert.AlertType.WARNING);
            return false;
        }
        else if(minSize.getValue() > maxSize.getValue()){
            
            alert.show("Size range", "Minimal size cannot be bigger than maximal size", Alert.AlertType.WARNING);
            return false;            
            
        }
        
        try {
            Integer.parseInt(priceField.getText());
            return true;
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 40, 10)", Alert.AlertType.ERROR);
            return false;
        }
    }
    
    
    private void resetWindow()
    {
        refField.setText("");
        priceField.setText("");
        colorField.setText("");
        brandField.setText("");         
        imgField.setText("No image selected");
        imgField.setGraphic(null);
        selectedFile = null;
        refField.requestFocus();
    }    
  
    public void logOut(ActionEvent event) throws IOException {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(employer);
                        mControl.returnMenu("products");
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();            
            
    } 

    @FXML
    private void insertProduct()
    {
        if (checkInputs()) {
            
            for(int cpt = minSize.getValue();cpt <= maxSize.getValue();cpt++){
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;

                if (selectedFile == null) {
                    ps = con.prepareStatement("INSERT INTO product(reference, size, brand, price, add_date, category, color) values(?,?,?,?,?,?,?)");
                }
                else {
                    String createImagePath = saveSelectedImage(selectedFile);
            
                    ps = con.prepareStatement("INSERT INTO product(reference, size, brand, price, add_date, category, color, imageURL) values(?,?,?,?,?,?,?,?)");
                    ps.setString(8, createImagePath);
                }

                ps.setString(1, refField.getText());
                ps.setInt(2, cpt);
                ps.setString(3, brandField.getText());
                ps.setInt(4, Integer.parseInt(priceField.getText()));
                ps.setString(6, catField.getValue().toString());
                ps.setString(7, colorField.getText());                

                LocalDate todayLocalDate = LocalDate.now();
                Date sqlDate = Date.valueOf(todayLocalDate);

                ps.setDate(5, sqlDate);

                ps.executeUpdate();
                con.close();

                

            }
            catch (Exception e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
            }
            
            alert.show("Product Added", "Your product was successfully added !", Alert.AlertType.INFORMATION);
            resetWindow();
        }

    }        
        // TODO
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        categories.addAll("Other","Sandal","Fillete","Soiree","Sabot","Chaussure","Moccasin");
        catField.setItems(categories);
        catField.setValue("Other");
        minSize.setValueFactory(minVals);
        maxSize.setValueFactory(maxVals);
        minSize.getValueFactory().setValue(15);
        maxSize.getValueFactory().setValue(45);
        
        refField.requestFocus();
        
        addPhoto.setOnAction(Action -> {
            chooseImage();
        });

        addProduct.setOnAction(Action -> {
            insertProduct();
        });
        
        minimize.setOnMouseClicked(Action -> {
        
            minimize(Action);
            
        });
    }    
    
}
