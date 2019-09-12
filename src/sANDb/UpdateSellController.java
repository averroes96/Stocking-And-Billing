
package sANDb;

import Data.Employer;
import Data.Sell;
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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
public class UpdateSellController implements Initializable {

    
    @FXML private Button saveButton,cancelButton;
    @FXML private TextField reference,price,color;
    @FXML private Slider size;
    @FXML private DatePicker date;
    @FXML private Label selectedSize,minimize;
  
    
    Employer employer = new Employer();
    Sell sell = new Sell();
    
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
    
    
    public void fillFields(Sell selectedSell){
        
        size.valueProperty().addListener((obs, oldval, newVal) -> 
        size.setValue(newVal.intValue()));      
        size.setMax(45);
        size.setMin(15);
        selectedSize.setText(String.valueOf(size.getValue()));
        selectedSize.textProperty().bindBidirectional(size.valueProperty(), NumberFormat.getIntegerInstance());        
        
        reference.setText(selectedSell.getSellRef());
        price.setText(String.valueOf(selectedSell.getSellPrice()));
        size.setValue(selectedSell.getProduct().getSize());
        date.getEditor().setText(selectedSell.getSellDate());
        color.setText(selectedSell.getProduct().getColor());

    }
    
    @FXML
    public void logOut(ActionEvent event) throws IOException {

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
    
    
        public void getData(Employer employer,Sell sell){
        
        this.employer = employer;
        this.sell = sell;
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
        
    public void updateSell(ActionEvent event) {

        if (checkInputs()) {
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;
                
                ps = con.prepareStatement("UPDATE product SET sold = 0 WHERE prod_id = ?");
                ps.setInt(1, this.sell.getProduct().getId());
                ps.executeUpdate();
            
                ps = con.prepareStatement("UPDATE sell SET sellprice = ?, sell_date = ?, emp_id = ?, prod_id = ? WHERE sell_id = ?");

                ps.setInt(3, employer.getEmpID());
                ps.setInt(1, Integer.parseInt(price.getText()));
                ps.setInt(4, productExist());
                ps.setDate(2, Date.valueOf(date.getEditor().getText()));
                ps.setInt(5, this.sell.getSellID());
                ps.executeUpdate();
                
                ps = con.prepareStatement("UPDATE product SET sold = 1 WHERE prod_id = ?");
                ps.setInt(1, productExist());
                ps.executeUpdate();
                
                con.close();

                alert.show("Sell Updated", "Your sell was successfully updated !", Alert.AlertType.INFORMATION);
                

                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(this.employer);
                        mControl.returnMenu("sells");
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
        
        fillFields(sell);
        
        date.setConverter(dateFormatter());
        
        minimize.setOnMouseClicked(Action -> {
        
            minimize(Action);
            
        });        
        
        
        
    }    


    
}
