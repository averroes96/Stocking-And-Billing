/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import Data.Sell;
import Include.SpecialAlert;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class NewBillController implements Initializable {

    @FXML public TextArea header,footer;
    @FXML public TableView<Sell> billTable;
    @FXML public TableColumn<Sell,Integer> id,price;
    @FXML public TableColumn<Sell,String> product,date;
    @FXML public BorderPane billPane;
    @FXML public Button cancel,print;
    
    ObservableList<Sell> billSells = FXCollections.observableArrayList();
    
    SpecialAlert alert = new SpecialAlert();
    
    public Label jobStatus ;    
    
    public void setInfo(ObservableList list, String headerMsg, String footerMsg){
        
        billTable.setItems(list);
        header.setText(headerMsg);
        footer.setText(footerMsg);
        
        
    }
    
    private void print(PrinterJob job, Node node) 
    {
        // Set the Job Status Message
        jobStatus.textProperty().bind(job.jobStatusProperty().asString());
         
        // Print the node
        boolean printed = job.printPage(node);
     
        if (printed) 
        {
            job.endJob();
        }
    }
    
    private void pageSetup(Node node) 
    {
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();
         
        if (job == null) 
        {
            return;
        }
         
        // Show the page setup dialog
        boolean proceed = job.showPageSetupDialog(this.billPane.getScene().getWindow());
         
        if (proceed) 
        {
            print(job, node);
        }
    }    
    
    
            public void handle(ActionEvent event) 
            {
                    pageSetup(billPane);
            }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        id.setCellValueFactory(new PropertyValueFactory<>("sellID"));
        product.setCellValueFactory(new PropertyValueFactory<>("sellRef"));
        price.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        date.setCellValueFactory(new PropertyValueFactory<>("sellDate"));

        cancel.setOnAction(Action -> {
        
            ((Node)Action.getSource()).getScene().getWindow().hide();
            
        });
        
        print.setOnAction(Action -> {
        
            handle(Action);
        
        });
        
    }    
    
}
