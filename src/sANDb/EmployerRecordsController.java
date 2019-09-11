/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Employer;
import Data.Record;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;


public class EmployerRecordsController implements Initializable {

    @FXML public ChoiceBox selectedMonth;
    @FXML public TableView<Record> recordsTable;
    @FXML public TableColumn<Record, String> date;
    @FXML public TableColumn<Record, String> absPres;
    @FXML public TableColumn<Record, Integer> payment;
    @FXML public TableColumn recordAct1,recordAct2;
    @FXML public Label fullname,image,phone,daysAbsent,paidAmount,prodSold,salary,joinDate;
    @FXML public Button newRecordButton,returnButton;
    
    private SpecialAlert alert = new SpecialAlert();
    
    private ObservableList<Record> recordsList = FXCollections.observableArrayList();
    
    private  ObservableList<String> months = FXCollections.observableArrayList();
    
    private Employer selectedEmployer = new Employer();
    private Employer employer = new Employer();
    
    public void getInfo(Employer employer, Employer admin){
        
        selectedEmployer = employer;
        this.employer = admin;
        
    }

    public void fillTheTable(Employer employer, int month)
    {
        Connection con = getConnection();
        String query = "SELECT * FROM employer_payment WHERE emp_id = ? AND EXTRACT(month FROM pay_date) = ?";

        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, employer.getEmpID());
            ps.setInt(2, month);
            rs = ps.executeQuery();

            while (rs.next()) {
                
                Record record = new Record();
                record.setDate(rs.getString("pay_date"));
                if(rs.getInt("absent") == 0){
                    record.setAbsPres("Present");
                }
                else if(rs.getInt("absent") == 1){
                    record.setAbsPres("Absent");
                }
                record.setPayment(rs.getInt("amount"));
                record.setEmployerID(rs.getInt("emp_id"));
                record.setRecordID(rs.getInt("empay_id"));

                recordsList.add(record);
            }

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void fillFields(Employer employer){
        
        fullname.setText(employer.getFullname());
        phone.setText("0" + employer.getPhone());
        salary.setText(employer.getSalary() + " DA");
        joinDate.setText(employer.getJoinDate());
        
        if (employer.getImage().equals("")) {
            image.setGraphic(null);
            image.setText("No image found");
            System.out.print("image");
            System.out.print(employer.getImage());
        }
        else {
            image.setText("");
            image.setGraphic(new ImageView(new Image(
                    new File(employer.getImage()).toURI().toString(),
                    150, 150, true, true)));
        }     
        
    }
    
    public void stats(Employer employer, int month){
        
        Connection con = getConnection();
        String query = "SELECT SUM(amount) FROM employer_payment WHERE emp_id = ? AND EXTRACT(month FROM pay_date) = ?";

        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, employer.getEmpID());
            ps.setInt(2, month);
            rs = ps.executeQuery();
            
            int sum = 0;
            int absent = 0;
            int sells = 0;

            while (rs.next()) {
                
                sum = rs.getInt("SUM(amount)");
                
            }
            
            ps = con.prepareStatement("SELECT count(absent) from employer_payment WHERE absent = 1 AND emp_id = ? AND EXTRACT(month FROM pay_date) = ?");
            ps.setInt(1, employer.getEmpID());
            ps.setInt(2, month);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                
                absent = rs.getInt("count(absent)");
                
            }
            
            ps = con.prepareStatement("SELECT COUNT(sell_id) FROM sell WHERE emp_id = ? AND EXTRACT(month FROM sell_date) = ?");
            ps.setInt(1, employer.getEmpID());
            ps.setInt(2, month);
            rs = ps.executeQuery();            

            while (rs.next()) {
                
                sells = rs.getInt("COUNT(sell_id)");
                
            }            

            paidAmount.setText(String.valueOf(sum) + " DA");
            daysAbsent.setText(String.valueOf(absent) + " Day(s)");
            prodSold.setText(String.valueOf(sells) + " Product(s)");

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
   
    }
       
    
    @FXML
    public void cancel(ActionEvent event) throws IOException {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        MainController mControl = (MainController)loader.getController();
                        mControl.getEmployer(this.employer);
                        mControl.returnMenu("employers");
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
        
        months.addAll("January","February","Mars","April","May","June","July","August","September","October","November","December");
        
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        payment.setCellValueFactory(new PropertyValueFactory<>("Payment"));
        absPres.setCellValueFactory(new PropertyValueFactory<>("absPres"));
        
        recordAct1.setCellValueFactory(new PropertyValueFactory<>("recordAct1"));
        recordAct2.setCellValueFactory(new PropertyValueFactory<>("recordAct2"));
        
        Callback<TableColumn<Record, String>, TableCell<Record, String>> cellFactory1
                =                 //
    (final TableColumn<Record, String> param) -> {
        final TableCell<Record, String> cell = new TableCell<Record, String>() {
            
            final Button update = new Button("UPDATE");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    update.setOnAction(event -> {
                    Record record = getTableView().getItems().get(getIndex());    
            try {

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateRecord.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        UpdateRecordController usControl = (UpdateRecordController)loader.getController();
                        usControl.getInfo(employer,record,selectedEmployer);
                        usControl.fillTheFields(record);
                        Scene scene = new Scene(root);
                        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        scene.getStylesheets().add(getClass().getResource("Layout/buttons.css").toExternalForm());                          
                        stage.setScene(scene);
                        stage.show();
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
        
        Callback<TableColumn<Record, String>, TableCell<Record, String>> cellFactory2
                =                 //
    (final TableColumn<Record, String> param) -> {
        final TableCell<Record, String> cell = new TableCell<Record, String>() {
            
            final Button delete = new Button("DELETE");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    delete.setOnAction(event -> {

                    });
                    delete.setStyle("-fx-background-color : red; -fx-text-fill: white; -fx-background-radius: 30;fx-background-insets: 0; -fx-cursor: hand;");
                    setGraphic(delete);
                    setText(null);               
                    
                }
            }
        };
        return cell;
    };  
        
        recordAct1.setCellFactory(cellFactory1);
        recordAct2.setCellFactory(cellFactory2);          
        
        recordsTable.setItems(recordsList);
        selectedMonth.setItems(months);
        
        selectedMonth.setOnAction(Action -> {
            
            if(selectedMonth.getSelectionModel().getSelectedItem().equals("January")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,1);
            stats(selectedEmployer,1);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("February")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,2);
            stats(selectedEmployer,2);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("Mars")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,3);
            stats(selectedEmployer,3);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("April")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,4);
            stats(selectedEmployer,4);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("May")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,5);
            stats(selectedEmployer,5);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("June")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,6);
            stats(selectedEmployer,6);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("July")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,7);
            stats(selectedEmployer,7);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("August")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,8);
            stats(selectedEmployer,8);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("September")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,9);
            stats(selectedEmployer,9);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("October")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,10);
            stats(selectedEmployer,10);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("November")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,11);
            stats(selectedEmployer,11);
            recordsTable.setItems(recordsList);                
            }
            else if(selectedMonth.getSelectionModel().getSelectedItem().equals("December")){
            recordsTable.getItems().clear();
            fillTheTable(selectedEmployer,12);
            stats(selectedEmployer,12);
            recordsTable.setItems(recordsList);                
            }             
            
        });
        
        switch(LocalDate.now().getMonth().getValue()){
            
            case 1 : selectedMonth.getSelectionModel().select(0); break;
            case 2 : selectedMonth.getSelectionModel().select(1); break;
            case 3 : selectedMonth.getSelectionModel().select(2); break;
            case 4 : selectedMonth.getSelectionModel().select(3); break;
            case 5 : selectedMonth.getSelectionModel().select(4); break;
            case 6 : selectedMonth.getSelectionModel().select(5); break;
            case 7 : selectedMonth.getSelectionModel().select(6); break;
            case 8 : selectedMonth.getSelectionModel().select(7); break;
            case 9 : selectedMonth.getSelectionModel().select(8); break;
            case 10 : selectedMonth.getSelectionModel().select(9); break;
            case 11 : selectedMonth.getSelectionModel().select(10); break;
            case 12 : selectedMonth.getSelectionModel().select(11); break;
        }
        
        newRecordButton.setOnAction(Action -> {
            
            try {                
                        ((Node)Action.getSource()).getScene().getWindow().hide();
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewRecord.fxml"));
                        AnchorPane root = (AnchorPane)loader.load();
                        NewRecordController npControl = (NewRecordController)loader.getController();
                        npControl.getEmployer(employer,selectedEmployer);
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
        
    }    
    
}
