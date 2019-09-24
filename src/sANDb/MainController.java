
package sANDb;

import Data.Employer;
import Data.Payment;
import Data.Product;
import Data.Sell;
import Include.Common;
import static Include.Common.adminsCount;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author med
 */
public class MainController implements Initializable {
    
    
    @FXML private Label btn_close;
    @FXML private AnchorPane products, sells, employers, payments;
    @FXML private TableView<Product> productsTable ;
    @FXML private TableView<Sell> sellsTable ;
    @FXML private TableView<Employer> employersTable ;
    @FXML private TableView<Payment> paymentsTable ;    
    @FXML private TableColumn<Product, Integer> id ;
    @FXML private TableColumn<Product, String> reference ;
    @FXML private TableColumn<Product, Integer> size ;
    @FXML private TableColumn<Product, String> brand ;
    @FXML private TableColumn<Product, Integer> price ;
    @FXML private TableColumn<Sell, Integer> sellPrice ;    
    @FXML private TableColumn<Product, String> addDate ;
    @FXML private TableColumn<Product, String> category ;
    @FXML private TableColumn<Product, String> color ;
    @FXML private TableColumn<Sell, Integer> sellID ;
    @FXML private TableColumn<Sell, String> sellRef ;
    @FXML private TableColumn<Sell, String> seller ;
    @FXML private TableColumn<Sell, String> sellDate ;
    @FXML private TableColumn<Employer,Integer> empID;
    @FXML private TableColumn<Employer,String> fullname;
    @FXML private TableColumn<Employer,Integer> phone;
    @FXML private TableColumn<Employer,Integer> salary;
    @FXML private TableColumn<Employer,String> joinDate;
    @FXML private TableColumn<Payment, Integer> payID ;
    @FXML private TableColumn<Payment, String> prodReference ;
    @FXML private TableColumn<Payment, Integer> paid ;
    @FXML private TableColumn<Payment, Integer> rest ;
    @FXML private TableColumn<Payment, String> payDate ;    
    @FXML private TableColumn sellActions ;
    @FXML private TableColumn sellActions2 ;
    @FXML private TableColumn payActions ;
    @FXML private TableColumn payActions2 ;     
    
    @FXML private TextField searchField;
    @FXML private TextField sellSearch;    
    @FXML private TextField refField;
    @FXML private TextField brandField;
    @FXML private TextField priceField;
    @FXML private TextField colorField;
    @FXML private Slider sizeField;
    @FXML private ChoiceBox catField,catChoice;
    @FXML private DatePicker dateField;
    @FXML private DatePicker sellDateField;
    @FXML private DatePicker payDateField;    
    @FXML private Label productImg,picLabel,fullnameLabel,phoneLabel,salaryLabel,joinedLabel;
    @FXML private Label idField,selectedSize,sum,totalProdSold,weekSum,weekSells,monthSum,monthSells,allSum,allSells,revSum,revTotal;    
    @FXML private Button addProd,productStats;
    @FXML private Button updateImage;
    @FXML private Button updateProduct; 
    @FXML private Button deleteProduct;
    @FXML private Button newSellButton;
    @FXML public Button newPayButton,addEmployerButton,updateEmployer,deleteEmployer,seeRecords,exBtn,newBillBtn,printProducts,day,week,month,total,sellStats,employerStats,btn_products, btn_sells, btn_employers, btn_payment,changePass;
    @FXML public Pane billPane;
    
    ObservableList<Product> data = FXCollections.observableArrayList();
    ObservableList<Sell> sellsList = FXCollections.observableArrayList(); 
    ObservableList<Employer> employersList = FXCollections.observableArrayList();
    ObservableList<Payment> paymentsList = FXCollections.observableArrayList();
    
    public  ObservableList<Integer> sizes = FXCollections.observableArrayList();
    
    public  ObservableList<String> categories = FXCollections.observableArrayList();
    
    
    SpecialAlert alert = new SpecialAlert();

    final String dateFormat = "yyyy-MM-dd";

    File selectedFile = null;
    
    private Employer thisEmployer;
    
    private double xOffset = 0;
    private double yOffset = 0;      
    

    public void getEmployer(Employer employer) {
        
        this.thisEmployer = employer ;
        if(employer.getAdmin() == 0)
        btn_employers.setVisible(false);

    }        
    

    public void fillTheTable(String category)
    {
        Connection con = getConnection();
        
        String query = "SELECT * FROM product WHERE sold = 0" + category + " ORDER BY prod_id DESC";

        Statement st;
        ResultSet rs;
        

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("prod_id"));
                product.setReference(rs.getString("reference"));
                product.setSize(rs.getInt("size"));
                product.setColor(rs.getString("color"));
                product.setBrand(rs.getString("brand"));
                product.setPrice(rs.getInt("price"));
                product.setAddDate(rs.getDate("add_date").toString());
                product.setCategory(rs.getString("category"));
                product.setImage(rs.getString("imageURL"));

                data.add(product);
            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


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
    
    
    private void search()
    {
        String keyword = searchField.getText();
        
        if (keyword.equals("")) {
            productsTable.setItems(data);
        }
        
        else {
            ObservableList<Product> filteredData = FXCollections.observableArrayList();
            for (Product product : data) {
                if(product.getReference().toLowerCase().contains(keyword.toLowerCase()))
                    filteredData.add(product);
            }
            productsTable.setItems(filteredData);
        }
    }
    
    private void searchSell()
    {
        String keyword = sellSearch.getText();
        
        if (keyword.equals("")) {
            sellsTable.setItems(sellsList);
        }
        
        else {
            ObservableList<Sell> filteredSells = FXCollections.observableArrayList();
            for (Sell sell : sellsList) {
                if(sell.getProduct().getReference().toLowerCase().contains(keyword.toLowerCase()))
                    filteredSells.add(sell);
            }
            sellsTable.setItems(filteredSells);
        }
    }    

    private void updateImage()
    {

        if(productsTable.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .JPG .PNG .GIF image", "*.jpg", "*.png", "*.gif")
        );

       
        selectedFile = fileChooser.showOpenDialog(null);

       
        if (selectedFile != null) {
            
            try {
               
                String createImagePath = Common.saveSelectedImage(selectedFile);

                Connection con = Common.getConnection();

                String query = "UPDATE product SET imageURL = ? WHERE prod_id = ?";

                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, createImagePath);
                ps.setInt(2, Integer.parseInt(idField.getText()));

                ps.executeUpdate();

                con.close();

                Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();

                Common.deleteImage(selectedProduct.getImage());

                selectedProduct.setImage(createImagePath);

                productImg.setText("");
                productImg.setGraphic(new ImageView(new Image(
                    selectedFile.toURI().toString(), 150, 150, true, true)));
                
                productsTable.refresh();
            }
            catch (Exception ex) {
                alert.show("Error", "Failed to update Image", Alert.AlertType.ERROR);
            }
        }

    }    
    
    private boolean checkInputs()
    {
        if (refField.getText().equals("") && priceField.getText().equals("")) {
            try {
                //alert.show("Missing required Fields", "Reference and Price fields cannot be empty!", Alert.AlertType.WARNING);                        ((Node)event.getSource()).getScene().getWindow().hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MyAlert.fxml"));
                AnchorPane root = (AnchorPane)loader.load();
                MyAlertController maControl = (MyAlertController)loader.getController();
                maControl.setAlert("warning_alert", "Missing required Fields", "Reference and Price fields cannot be empty!");
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();
                        return false;
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (refField.getText().equals("")) {
            alert.show("Missing required Fields", "Please enter product name", Alert.AlertType.WARNING);
            return false;
        }
        else if (priceField.getText().equals("")) {
            alert.show("Missing required Fields", "Please enter product price", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(priceField.getText());
            if(Integer.parseInt(priceField.getText()) > 0)
            return true;
            else{
            alert.show("Error", "Price should not have a negative value (eg: 4000, 1000)", Alert.AlertType.ERROR);
            return false;
            }
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 4000, 1000)", Alert.AlertType.ERROR);
            return false;
        }
    }    
    
    private void updateProduct()
    {

        if(productsTable.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update first", Alert.AlertType.INFORMATION);
            return;
        }
        
        if (checkInputs() == false) {
            return;
        }

        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();

        try {

            Connection con = getConnection();
            String query;

            query = "UPDATE product SET reference = ?, size = ?, brand = ?, price = ?, add_date = ?, category = ?, color = ? WHERE prod_id = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, refField.getText());
            ps.setInt(2, (int)sizeField.getValue());
            ps.setString(3, brandField.getText());            
            ps.setInt(4, Integer.valueOf(priceField.getText()));
            ps.setDate(5, Date.valueOf(dateField.getEditor().getText()));
            ps.setString(6,catField.getSelectionModel().getSelectedItem().toString());
            ps.setString(7, colorField.getText());            
            ps.setInt(8, Integer.parseInt(idField.getText()));
            ps.executeUpdate();

            con.close();

            selectedProduct.setReference(refField.getText());
            selectedProduct.setSize((int)sizeField.getValue()); 
            selectedProduct.setBrand(brandField.getText());
            selectedProduct.setPrice(Integer.valueOf(priceField.getText()));
            selectedProduct.setCategory(catField.getSelectionModel().getSelectedItem().toString());
            selectedProduct.setAddDate(dateField.getEditor().getText());
            selectedProduct.setColor(colorField.getText());
            
            productsTable.refresh();

            
            alert.show("Product Successfully updated",
                    "Product information has been successfully updated",
                    Alert.AlertType.INFORMATION);
        
        }
        catch (Exception e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }   
    
    private void showProduct(int index)
    {
        refField.setText(data.get(index).getReference());
        priceField.setText(String.valueOf(data.get(index).getPrice()));
        dateField.getEditor().setText(data.get(index).getAddDate());
        colorField.setText(data.get(index).getColor());
        brandField.setText(data.get(index).getBrand());
        sizeField.setValue(data.get(index).getSize());
        catField.setValue(data.get(index).getCategory());
        catField.setItems(categories);
        idField.setText(String.valueOf(data.get(index).getId()));        

        if (data.get(index).getImage() == null) {
            productImg.setGraphic(null);
            productImg.setText("No image found");
        }
        else {
            productImg.setText("");
            productImg.setGraphic(new ImageView(new Image(
                    new File(data.get(index).getImage()).toURI().toString(),
                    150, 150, true, true)));
        }        
        
        
    }
    
    private void showEmployer(int index)
    {
        fullnameLabel.setText(employersList.get(index).getFullname());
        phoneLabel.setText("0" + String.valueOf(employersList.get(index).getPhone()));
        salaryLabel.setText(String.valueOf(employersList.get(index).getSalary()) + " DA");        
        joinedLabel.setText(employersList.get(index).getJoinDate());       

        if (employersList.get(index).getImage().equals("")) {
            picLabel.setGraphic(null);
            picLabel.setText("No image found");
        }
        else {
            picLabel.setText("");
            picLabel.setGraphic(new ImageView(new Image(
                    new File(employersList.get(index).getImage()).toURI().toString(),
                    200, 200, true, true)));
        }        
        
        
    }    
    
    

    private void showNextProduct()
    {
        if (productsTable.getSelectionModel().getSelectedIndex() < data.size() - 1) {
            int currentSelectedRow = productsTable.getSelectionModel().getSelectedIndex() + 1;
            productsTable.getSelectionModel().select(currentSelectedRow);
            showProduct(currentSelectedRow);
        }
    }    

    private void deleteProduct()
    {

        if(productsTable.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to delete first", Alert.AlertType.INFORMATION);
            return;
        }

        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        
        try {

            Connection con = getConnection();

            String query = "DELETE FROM product WHERE prod_id = ?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, selectedProduct.getId());

            ps.executeUpdate();

            con.close();

            Common.deleteImage(selectedProduct.getImage());
            
            data.remove(selectedProduct);
            
            productsTable.refresh();
            
            if(data.size() > 0) {
                showNextProduct();
            }
            else
            {
                idField.setText("");
                refField.setText("");
                sizeField.setValue(36);
                brandField.setText("");
                priceField.setText("");
                colorField.setText("");
                catField.setValue("");
                dateField.getEditor().setText("");
                productImg.setText("No image found");
                productImg.setGraphic(null);
            }
            
        }
        catch (Exception e) {
            
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }    
    
    private void getAllSells(String selectedDate)
    {
        Connection con = getConnection();
        String query = "SELECT * FROM sell INNER JOIN product ON sell.prod_id = product.prod_id WHERE date(sell_date) = ? ORDER BY sell.sell_id DESC";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1,selectedDate);
            rs = st.executeQuery();

            while (rs.next()) {
                Sell sell = new Sell();
                sell.setSellID(rs.getInt("sell_id"));
                sell.setSellPrice(rs.getInt("sellPrice"));
                sell.setSellDate(rs.getDate("sell_date").toString());
                Product product = new Product();
                product.setId(rs.getInt("prod_id"));
                product.setReference(rs.getString("reference"));               
                product.setSize(rs.getInt("size"));
                product.setColor(rs.getString("color"));
                product.setBrand(rs.getString("brand"));
                product.setPrice(rs.getInt("price"));
                product.setAddDate(rs.getDate("add_date").toString());
                product.setCategory(rs.getString("category"));
                product.setImage(rs.getString("imageURL"));
                
                sell.setProduct(product);
                sell.setSellRef(rs.getString("reference")); 

                sellsList.add(sell);
            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    public void getSellStats(String selectedDate, String type){
        
        Connection con = getConnection();
        String query = "";
        PreparedStatement st;
        ResultSet rs;        
        if(selectedDate.equals("")){
            if(type.equals("ALL")){
                query = "SELECT count(*), SUM(sellPrice) FROM sell";
            }
            else if(type.equals("MONTH")){
                query = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 30 day ) ";
            }
            else if(type.equals("WEEK")){
                query = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ";
            }
        }
        else{
            query = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date = ? ";
        }



        try {
            st = con.prepareStatement(query);
            if(!selectedDate.equals("")){
            st.setString(1,selectedDate);
            }
            rs = st.executeQuery();
            
            int priceSum = 0;
            int totals = 0;

            while (rs.next()) {
                
                priceSum = rs.getInt("SUM(sellPrice)");
                totals = rs.getInt("count(*)");
                
            }
            
            revSum.setText(String.valueOf(priceSum) + " DA");
            revTotal.setText(String.valueOf(totals) + " Sell(s)");

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }        
        
    }   
    
    /*public void getSellsStats(String selectedDate){
        
        Connection con = getConnection();
        String query = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date = ? ";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1,selectedDate);
            rs = st.executeQuery();
            
            int priceSum = 0;
            int total = 0;

            while (rs.next()) {
                
                priceSum = rs.getInt("SUM(sellPrice)");
                total = rs.getInt("count(*)");
                
            }
            
            sum.setText(String.valueOf(priceSum) + " DA");
            totalProdSold.setText(String.valueOf(total) + " Sell(s)");

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }        
        
    }
    
    public void getSellsWeekStats(){
        
        Connection con = getConnection();
        String query = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            
            int priceSum = 0;
            int total = 0;

            while (rs.next()) {
                
                priceSum = rs.getInt("SUM(sellPrice)");
                total = rs.getInt("count(*)");
                
            }
            
            weekSum.setText(String.valueOf(priceSum) + " DA");
            weekSells.setText(String.valueOf(total) + " Sell(s)");

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }         
        
    }
    
    public void getSellsMonthStats(){
        
        Connection con = getConnection();
        String query = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 30 day ) ";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            
            int priceSum = 0;
            int total = 0;

            while (rs.next()) {
                
                priceSum = rs.getInt("SUM(sellPrice)");
                total = rs.getInt("count(*)");
                
            }
            
            monthSum.setText(String.valueOf(priceSum) + " DA");
            monthSells.setText(String.valueOf(total) + " Sell(s)");

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }         
        
    }

    public void getAllSellsStats(){
        
        Connection con = getConnection();
        String query = "SELECT count(*), SUM(sellPrice) FROM sell";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            
            int priceSum = 0;
            int total = 0;

            while (rs.next()) {
                
                priceSum = rs.getInt("SUM(sellPrice)");
                total = rs.getInt("count(*)");
                
            }
            
            allSum.setText(String.valueOf(priceSum) + " DA");
            allSells.setText(String.valueOf(total) + " Sell(s)");

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }         
        
    }     */
  

    private void deleteSell(Sell selectedSell)
    {

        
        try {

            Connection con = getConnection();

            String query = "DELETE FROM sell WHERE sell_id = ?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, selectedSell.getSellID());

            ps.executeUpdate();
            
            query = "UPDATE product SET sold = 0 WHERE prod_id = ?";
            
            ps = con.prepareStatement(query);
            
            ps.setInt(1, selectedSell.getProduct().getId());
            
            ps.executeUpdate();

            con.close();
            
            productsTable.getItems().add(selectedSell.getProduct());
            
            sellsList.remove(selectedSell);
            
            sellsTable.refresh();
            
            getSellStats(sellDateField.getEditor().getText(),"");
            
            alert.show("Sell deleted", "Sell was successfully delete !", Alert.AlertType.INFORMATION);
            
        }
        catch (Exception e) {
            
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void getAllEmployers()
    {
        Connection con = getConnection();
        String query = "SELECT * FROM employers WHERE active = 1";

        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                
                Employer employer = new Employer();
                employer.setEmpID(rs.getInt("emp_id"));
                employer.setFullname(rs.getString("fullname"));
                employer.setSalary(rs.getInt("salary"));
                employer.setPhone(rs.getInt("telephone"));
                employer.setJoinDate(String.valueOf(rs.getDate("joined_date")));
                employer.setUsername(rs.getString("username"));
                employer.setImage(rs.getString("emp_image"));
                employer.setAdmin(rs.getInt("admin"));

                employersList.add(employer);
            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void deletePay(Payment selectedPay)
    {

        
        try {

            Connection con = getConnection();

            String query = "DELETE FROM payment WHERE pay_id = ?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, selectedPay.getPayID());

            ps.executeUpdate();

            con.close();
            
            sellsList.remove(selectedPay);
            
            sellsTable.refresh();
            
        }
        catch (Exception e) {
            
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }    

    private void deleteEmployer(Employer selectedEmployer)
    {

        try {
            
            
            if( adminsCount() > 1 || selectedEmployer.getAdmin() != 1){            

            Connection con = getConnection();

            String query = "UPDATE employers SET active = 0, joined_date = ? WHERE emp_id = ?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(2, selectedEmployer.getEmpID());
            LocalDate todayLocalDate = LocalDate.now();
            Date sqlDate = Date.valueOf(todayLocalDate);

            ps.setDate(1, sqlDate);            

            ps.executeUpdate();

            con.close();
            

            
            if(this.thisEmployer.getUsername().equals(selectedEmployer.getUsername())){
                
                        this.addProd.getScene().getWindow().hide();
                        Stage stage = new Stage();
                        AnchorPane root = FXMLLoader.load(getClass().getResource("Login.fxml"));
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
                
            }
            else{

            employersList.remove(selectedEmployer);
            
            employersTable.refresh();
            }
                alert.show("Employer deleted", "Employer was successfully delete !", Alert.AlertType.INFORMATION);
            }
            else{
                
                alert.show("Last Admin !", "You can't delete this account because it's the last and only admin left ! In order to delete it make another employer an admin or create a new account.", Alert.AlertType.ERROR);
                
            }
            
        }
        catch (Exception e) {
            
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public Sell getSell(int sellID){
        
        Connection con = getConnection();
        String query = "SELECT * FROM sell INNER JOIN product ON sell.prod_id = product.prod_id INNER JOIN employers ON employers.emp_id = sell.emp_id WHERE sell.sell_id = ? ORDER BY sell.sell_date";

        PreparedStatement st;
        ResultSet rs;
        Sell sell = new Sell();        

        try {
            st = con.prepareStatement(query);
            st.setInt(1,sellID);
            rs = st.executeQuery();

            while (rs.next()) {

                sell.setSellID(rs.getInt("sell_id"));
                sell.setSellPrice(rs.getInt("sellPrice"));
                sell.setSellDate(rs.getDate("sell_date").toString());
                Product product = new Product();
                product.setId(rs.getInt("prod_id"));
                product.setReference(rs.getString("reference"));               
                product.setSize(rs.getInt("size"));
                product.setColor(rs.getString("color"));
                product.setBrand(rs.getString("brand"));
                product.setPrice(rs.getInt("price"));
                product.setAddDate(rs.getDate("add_date").toString());
                product.setCategory(rs.getString("category"));
                product.setImage(rs.getString("imageURL"));
                
                sell.setProduct(product);
                sell.setSellRef(rs.getString("reference"));

            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
                System.out.println(sell.getSellRef());        
        return sell;
        
    }
    
    private void getAllPayments(String selectedDate)
    {
        Connection con = getConnection();
        String query = "SELECT * FROM payment INNER JOIN sell ON sell.sell_id = payment.sell_id WHERE date(pDate) = ? ORDER BY payment.pDate DESC";

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1,selectedDate);
            rs = st.executeQuery();

            while (rs.next()) {
                
                Payment payment = new Payment();
                Sell sell = getSell(rs.getInt("sell_id"));
                payment.setSell(sell);                
                payment.setPayID(rs.getInt("pay_id"));
                payment.setPaid(rs.getInt("paid"));
                payment.setProdReference(sell.getSellRef());
                payment.setPayDate(rs.getString("pDate"));
                payment.setRest(rs.getInt("rest"));              
            System.out.println(payment.getProdReference());
                paymentsList.add(payment);
            }
            


            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
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
    
    @FXML
    private int insertBill()
    {
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                PreparedStatement ps;

                    ps = con.prepareStatement("INSERT INTO facture(facture_date, sum) values(now(),?)",PreparedStatement.RETURN_GENERATED_KEYS);


                ps.setInt(1, 0);

                ps.executeUpdate();
                
                ResultSet generatedKeys = ps.getGeneratedKeys();
                
                int key = 0;

                if (generatedKeys.next()) {
                    
                    key = generatedKeys.getInt(1);
                    
                    }
                    else {
                        throw new SQLException("Creating key failed, no ID obtained.");
                    }                
                
                con.close();
                
                return key;

                
            }
            catch (Exception e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        return 0;

    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fillTheTable("");

        dateField.setConverter(dateFormatter());
        sellDateField.setConverter(dateFormatter());
        payDateField.setConverter(dateFormatter());        
        sellDateField.getEditor().setText(String.valueOf(LocalDate.now()));
        payDateField.getEditor().setText(String.valueOf(LocalDate.now()));
        
        sizeField.valueProperty().addListener((obs, oldval, newVal) -> 
        sizeField.setValue(newVal.intValue()));
        sizeField.setMax(45);
        sizeField.setMin(15);
        
        selectedSize.setText(String.valueOf(sizeField.getValue()));
        selectedSize.textProperty().bindBidirectional(sizeField.valueProperty(), NumberFormat.getIntegerInstance());        
        
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        reference.setCellValueFactory(new PropertyValueFactory<>("Reference"));
        size.setCellValueFactory(new PropertyValueFactory<>("Size"));
        brand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        addDate.setCellValueFactory(new PropertyValueFactory<>("AddDate"));
        category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        color.setCellValueFactory(new PropertyValueFactory<>("Color")); 

        productsTable.setItems(data);
        
        sizes.addAll(15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44);
        
        categories.addAll("All","Sandal","Fillete","Soiree","Sabot","Chaussure","Moccasin");
        catChoice.setItems(categories);
        catChoice.getSelectionModel().selectFirst();
        catChoice.setOnAction(Action -> {
            
            switch(catChoice.getSelectionModel().getSelectedItem().toString()){
                
                case "All" : productsTable.getItems().clear(); fillTheTable(""); break;
                case "Sandal" : productsTable.getItems().clear(); fillTheTable(" AND category = 'Sandal'"); break;
                case "Fillete" : productsTable.getItems().clear(); fillTheTable(" AND category = 'Fillete'"); break;
                case "Soiree" : productsTable.getItems().clear(); fillTheTable(" AND category = 'Soiree'"); break;
                case "Sabot" : productsTable.getItems().clear(); fillTheTable(" AND category = 'Sabot'"); break;
                case "Chaussure" : productsTable.getItems().clear(); fillTheTable(" AND category = 'Chaussure'"); break;
                case "Moccasin" : productsTable.getItems().clear(); fillTheTable(" AND category = 'Moccasin'"); break;
                
            }
        
        });
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            search();
        });
        
        if (!data.isEmpty()) {
            productsTable.getSelectionModel().select(0);
            showProduct(0);
        }
        
        productsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showProduct(productsTable.getSelectionModel().getSelectedIndex());
            }
        });

        updateImage.setOnAction(Action -> {
            updateImage();
        });
        
        updateProduct.setOnAction(Action -> {
            updateProduct();
        });        
       
        deleteProduct.setOnAction(Action -> {
            deleteProduct();
        });
        
        addProd.setOnAction(Action -> {
            try {
                
                ((Node)Action.getSource()).getScene().getWindow().hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("NewProduct.fxml"));
                Pane root = (Pane)loader.load();
                NewProductController npControl = (NewProductController)loader.getController();
                npControl.getEmployer(thisEmployer);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);                
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                 
                stage.show();
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
        });
        
        
        // SELLS TAB
        
        getAllSells(sellDateField.getEditor().getText());
        getSellStats(sellDateField.getEditor().getText(),"");
        
        sellID.setCellValueFactory(new PropertyValueFactory<>("sellID"));
        sellRef.setCellValueFactory(new PropertyValueFactory<>("sellRef"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        sellDate.setCellValueFactory(new PropertyValueFactory<>("sellDate"));
        sellActions.setCellValueFactory(new PropertyValueFactory<>("sellActions"));
        Callback<TableColumn<Sell, String>, TableCell<Sell, String>> cellFactory
                =                 //
    (final TableColumn<Sell, String> param) -> {
        final TableCell<Sell, String> cell = new TableCell<Sell, String>() {
            
            final Button update = new Button("UPDATE");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    update.setOnAction(event -> {
                        Sell sell = getTableView().getItems().get(getIndex());
            try {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSell.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        UpdateSellController usControl = (UpdateSellController)loader.getController();
                        usControl.getData(thisEmployer,sell);
                        usControl.fillFields(sell);
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();
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
                    });
                    update.setStyle("-fx-background-color : blue; -fx-text-fill: white; -fx-background-radius: 30;fx-background-insets: 0; -fx-cursor: hand;");                    
                    setGraphic(update);
                    setText(null);               
                    
                }
            }
        };
        return cell;
    };
            
        sellActions.setCellFactory(cellFactory);
        
        sellActions2.setCellValueFactory(new PropertyValueFactory<>("sellActions2"));        
                   
        Callback<TableColumn<Sell, String>, TableCell<Sell, String>> cellFactory2
                =                 //
    (final TableColumn<Sell, String> param) -> {
        final TableCell<Sell, String> cell = new TableCell<Sell, String>() {
            
            final Button delete = new Button("DELETE");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    delete.setOnAction(event -> {
                        Sell sell = getTableView().getItems().get(getIndex());
                        deleteSell(sell);
                    });
                    delete.setStyle("-fx-background-color : red; -fx-text-fill: white; -fx-background-radius: 30;fx-background-insets: 0; -fx-cursor: hand;");                    
                    setGraphic(delete);
                    setText(null);               
                    
                }
            }
        };
        return cell;
    };
        
   
        
        sellActions2.setCellFactory(cellFactory2);        
        
        sellsTable.setItems(sellsList);
        
        newSellButton.setOnAction(Action -> {
            
            try {                
                        ((Node)Action.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewSell.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        NewSellController nsControl = (NewSellController)loader.getController();
                        nsControl.getEmployer(thisEmployer);
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();
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

        });
        

        sellSearch.textProperty().addListener((obs, oldText, newText) -> {
            searchSell();
        });

        sellDateField.setOnAction(Action -> {
            sellsTable.getItems().clear();
            getAllSells(sellDateField.getEditor().getText());
            getSellStats(sellDateField.getEditor().getText(),"");
            sellsTable.setItems(sellsList);
        });        

       // Employers Tab
       
        getAllEmployers();
       
        empID.setCellValueFactory(new PropertyValueFactory<>("empID"));
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        joinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate")); 
        
        employersTable.setItems(employersList);
        
        if (!employersList.isEmpty()) {
            employersTable.getSelectionModel().select(0);
            showEmployer(0);
        }
        
        employersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showEmployer(employersTable.getSelectionModel().getSelectedIndex());
            }
        });        
        
        addEmployerButton.setOnAction(Action -> {
            
            try {                
                        ((Node)Action.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewEmployer.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        NewEmployerController nsControl = (NewEmployerController)loader.getController();
                        nsControl.getEmployer(thisEmployer);
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

        });

        updateEmployer.setOnAction(Action -> {
            
                        try {
                            Employer employer = employersTable.getSelectionModel().getSelectedItem();
                            ((Node)Action.getSource()).getScene().getWindow().hide();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateEmployer.fxml"));
                            AnchorPane root = (AnchorPane)loader.load();
                            UpdateEmployerController ueControl = (UpdateEmployerController)loader.getController();
                            ueControl.getInfo(thisEmployer,employer);
                            ueControl.fillFields(employer);                            
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
        });

        deleteEmployer.setOnAction(Action -> {

                Employer employer = employersTable.getSelectionModel().getSelectedItem();
                deleteEmployer(employer);
                


        });
        
        exBtn.setOnAction(Action -> {
            
                        try {            

                            ((Node)Action.getSource()).getScene().getWindow().hide();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExEmployers.fxml"));
                            AnchorPane root = (AnchorPane)loader.load();
                            ExEmployersController ueControl = (ExEmployersController)loader.getController();
                            ueControl.getInfo(this.thisEmployer);
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
        
        });
        
        seeRecords.setOnAction(Action -> {
            
                        try {
                            
                            
                            LocalDate ldt = LocalDate.now() ;
                            Employer employer = employersTable.getSelectionModel().getSelectedItem();
                            ((Node)Action.getSource()).getScene().getWindow().hide();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployerRecords.fxml"));
                            AnchorPane root = (AnchorPane)loader.load();
                            EmployerRecordsController erControl = (EmployerRecordsController)loader.getController();
                            erControl.getInfo(employer, thisEmployer);
                            erControl.fillTheTable(employer,ldt.getMonth().getValue());
                            erControl.fillFields(employer);
                            erControl.stats(employer, ldt.getMonth().getValue());
                            erControl.header.setText(employer.getFullname() + "'s Records");
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
        });

        changePass.setOnAction(Action -> {
            
                        try {

                            Employer employer = employersTable.getSelectionModel().getSelectedItem();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePass.fxml"));
                            AnchorPane root = (AnchorPane)loader.load();
                            ChangePassController erControl = (ChangePassController)loader.getController();
                            erControl.getEmployer(employer);
                            Scene scene = new Scene(root);
                            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                            scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                            stage.setScene(scene);                         
                            stage.show();                           
                        } catch (IOException ex) {
                            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                        }
        });        
        
        
        
        // Payments TAB
        
        getAllPayments(payDateField.getEditor().getText());
        
        payID.setCellValueFactory(new PropertyValueFactory<>("payID"));
        prodReference.setCellValueFactory(new PropertyValueFactory<>("prodReference"));
        paid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        rest.setCellValueFactory(new PropertyValueFactory<>("rest"));
        payDate.setCellValueFactory(new PropertyValueFactory<>("payDate"));
        payActions.setCellValueFactory(new PropertyValueFactory<>("payActions1"));
        payActions2.setCellValueFactory(new PropertyValueFactory<>("payActions2"));
        
        Callback<TableColumn<Payment, String>, TableCell<Payment, String>> cellFactory5
                =                 
    (final TableColumn<Payment, String> param) -> {
        final TableCell<Payment, String> cell = new TableCell<Payment, String>() {
            
            final Button update = new Button("UPDATE");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    update.setOnAction(event -> {
                        try {
                            Payment payment = getTableView().getItems().get(getIndex());
                            ((Node)event.getSource()).getScene().getWindow().hide();
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatePayment.fxml"));
                            AnchorPane root = (AnchorPane)loader.load();
                            UpdatePaymentController upControl = (UpdatePaymentController)loader.getController();
                            upControl.getInfo(thisEmployer,payment);
                            upControl.fillFields(payment);                            
                            Scene scene = new Scene(root);
                            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                            scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                            stage.setScene(scene);                         
                            stage.show();
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
                    });
                    update.setStyle("-fx-background-color : blue; -fx-text-fill: white; -fx-background-radius: 30;fx-background-insets: 0; -fx-cursor: hand;");
                    setGraphic(update);
                    setText(null);               
                    
                }
            }
        };
        return cell;
    };         
        
        Callback<TableColumn<Payment, String>, TableCell<Payment, String>> cellFactory6
                =                 //
    (final TableColumn<Payment, String> param) -> {
        final TableCell<Payment, String> cell = new TableCell<Payment, String>() {
            
            final Button delete = new Button("DELETE");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    delete.setOnAction(event -> {
                        Payment payment = getTableView().getItems().get(getIndex());
                        deletePay(payment);
                    });
                    delete.setStyle("-fx-background-color : red; -fx-text-fill: white; -fx-background-radius: 30;fx-background-insets: 0; -fx-cursor: hand;");
                    setGraphic(delete);
                    setText(null);               
                    
                }
            }
        };
        return cell;
    };
        
        payActions.setCellFactory(cellFactory5);
        payActions2.setCellFactory(cellFactory6);        

        paymentsTable.setItems(paymentsList);
        
        payDateField.setOnAction(Action -> {
            paymentsTable.getItems().clear();
            getAllPayments(payDateField.getEditor().getText());
            paymentsTable.setItems(paymentsList);
        });        
        
        newPayButton.setOnAction(Action -> {
            
            try {                
                        ((Node)Action.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewPayment.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        NewPaymentController npControl = (NewPaymentController)loader.getController();
                        npControl.getEmployer(thisEmployer);
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();
                        root.setOnMouseDragged((MouseEvent event) -> {
                            stage.setX(event.getScreenX() - xOffset);
                            stage.setY(event.getScreenY() - yOffset);
                        });                         
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        
        sellsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        newBillBtn.disableProperty().bind(Bindings.size(sellsTable.getSelectionModel().getSelectedIndices()).isEqualTo(0));
        
        Tooltip.install(
                billPane, 
                new Tooltip("In order to add a new bill select the sells you want to bill"));
        
        newBillBtn.setOnAction(Action -> {
            ObservableList<Sell> billList = sellsTable.getSelectionModel().getSelectedItems();
            int billID = insertBill();
            int factureSum = 0;
     
            for(Sell e : billList){
            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                    PreparedStatement ps;

                    ps = con.prepareStatement("UPDATE sell SET facture_id = ? WHERE sell_id = ?");
                    ps.setInt(1, billID);
                    ps.setInt(2, e.getSellID());

                    ps.executeUpdate();               
                
                    con.close();
                    
                    factureSum += e.getSellPrice();
                
            }
            catch (SQLException ex) {
                alert.show("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
            }

            try {

                Connection con = getConnection();

                if(con == null) {
                    alert.show("Connection Error", "Failed to connect to database server", Alert.AlertType.ERROR);
                }

                    PreparedStatement ps;

                    ps = con.prepareStatement("UPDATE facture SET sum = ? WHERE facture_id = ?");
                    ps.setInt(1, factureSum);
                    ps.setInt(2, billID);

                    ps.executeUpdate();               
                
                    con.close();
                
            }
            catch (SQLException ex) {
                alert.show("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }            
            
            String headerMsg = "-----------------------  H.S.Fashion's Bill Number : " + billID + "  -----------------------";
            headerMsg += "\n\n\n Date :  " + LocalDate.now().toString();
            
            String footerMsg = "\n\t\t\t\t\t\t Total : " + factureSum;
            footerMsg += "\n\n----------------------------  Thanks for your visit :)  ----------------------------";
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("NewBill.fxml"));
                AnchorPane root = (AnchorPane)loader.load();
                NewBillController upControl = (NewBillController)loader.getController();
                upControl.setInfo(billList,headerMsg,footerMsg);
                Scene scene = new Scene(root);
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);                
                scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene);            
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        });
        
        printProducts.setOnAction(Action -> {
        
            print(productsTable);
            
        } );
        
    /*    day.setOnAction(Action -> {
        
            getSellStats(sellDateField.getEditor().getText(),"");
        
        });
        
        week.setOnAction(Action -> {
        
            getSellStats("","WEEK");
        
        });

        month.setOnAction(Action -> {
        
            getSellStats("","MONTH");
        
        });

        total.setOnAction(Action -> {
        
            getSellStats("","ALL");
        
        }); */

        sellStats.setOnAction(Action -> {
        
            try {
                Stage stage = new Stage();
                AnchorPane root = FXMLLoader.load(getClass().getResource("SellStats.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
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
            
        });
        
        employerStats.setOnAction(Action -> {
        
            try {
                Stage stage = new Stage();
                AnchorPane root = FXMLLoader.load(getClass().getResource("EmployerStats.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
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
            
        });
        
        productStats.setOnAction(Action -> {
        
            try {
                Stage stage = new Stage();
                AnchorPane root = FXMLLoader.load(getClass().getResource("ProductStats.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Layout/custom.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
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
            
        });        
                
        
        
       
    }

    public void selectMenu(Event event) throws IOException{
        
        if(event.getTarget() == btn_products){
            products.setVisible(true);
            btn_products.setEffect(new Glow());
            sells.setVisible(false);
            btn_sells.setEffect(null);
            employers.setVisible(false);
            btn_employers.setEffect(null);
            payments.setVisible(false);
            btn_payment.setEffect(null);
            
        }
        else if(event.getTarget() == btn_sells){
            products.setVisible(false);
            btn_products.setEffect(null);
            sells.setVisible(true);
            btn_sells.setEffect(new Glow());
            employers.setVisible(false);
            btn_employers.setEffect(null);
            payments.setVisible(false);
            btn_payment.setEffect(null);
        }
        else if(event.getTarget() == btn_employers){
            products.setVisible(false);
            btn_products.setEffect(null);
            sells.setVisible(false);
            btn_sells.setEffect(null);
            employers.setVisible(true);
            btn_employers.setEffect(new Glow());
            payments.setVisible(false);
            btn_payment.setEffect(null);
        }
        else if(event.getTarget() == btn_payment){
            products.setVisible(false);
            btn_products.setEffect(null);
            sells.setVisible(false);
            btn_sells.setEffect(null);
            employers.setVisible(false);
            btn_employers.setEffect(null);
            payments.setVisible(true);
            btn_payment.setEffect(new Glow());
        }
        else if(event.getTarget() == btn_close){
            
                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        AnchorPane root = FXMLLoader.load(getClass().getResource("Login.fxml"));
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
                        
        }
        
    }
    
    public void returnMenu(String source){
        
        if(source.equals("products")){
            
            products.setVisible(true);
            sells.setVisible(false);
            employers.setVisible(false);
            payments.setVisible(false);            
            
        }
        else if(source.equals("sells")){
            
            products.setVisible(false);
            sells.setVisible(true);
            employers.setVisible(false);
            payments.setVisible(false);            
            
        }
        
        else if(source.equals("payments")){
            
            products.setVisible(false);
            sells.setVisible(false);
            employers.setVisible(false);
            payments.setVisible(true);            
            
        }
        
        else if(source.equals("employers")){
            
            products.setVisible(false);
            sells.setVisible(false);
            employers.setVisible(true);
            payments.setVisible(false);            
            
        }        

        
        
    }
    
    private void print(Node node) 
    {
        // Define the Job Status Message
        printProducts.textProperty().unbind();
        printProducts.setText("Creating a printer job...");
         
        // Create a printer job for the default printer
        PrinterJob job = PrinterJob.createPrinterJob();
         
        if (job != null) 
        {
            // Show the printer job status
            printProducts.textProperty().bind(job.jobStatusProperty().asString());
             
            // Print the node
            boolean printed = job.printPage(node);
 
            if (printed) 
            {
                // End the printer job
                job.endJob();
            } 
            else
            {
                // Write Error Message
                printProducts.textProperty().unbind();
                printProducts.setText("Printing failed.");
            }
        } 
        else
        {
            // Write Error Message
            printProducts.setText("Could not create a printer job.");
        }
    }    
    
}