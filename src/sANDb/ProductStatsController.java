/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import static Include.Common.dateFormatter;
import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;


public class ProductStatsController implements Initializable {

    @FXML public PieChart myChart;
    @FXML public RadioButton reference,size,color,category,brand;
    @FXML public DatePicker startDate,endDate;
    @FXML public ChoiceBox type;
    @FXML public Button filter;
    
    private ToggleGroup columns = new ToggleGroup();
    
    private ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
    private ObservableList<String> typeList = FXCollections.observableArrayList("All","Sold","In Stock");    
    
    private SpecialAlert alert = new SpecialAlert();
    
    
    public void loadChart(String target){
        
        try {
            Connection con = getConnection();
            String query = "SELECT " + target + ", count(" + target  +") FROM product WHERE sold = 0 Group by " + target + " ORDER BY count(" + target  +") DESC LIMIT 10";
            PreparedStatement st;
            ResultSet rs;
            
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while(rs.next()){
                
                list.add(new PieChart.Data(rs.getString(target),rs.getInt("count(" + target  +")")));
                
            }
            
            myChart.setData(list);
            for(final PieChart.Data data : myChart.getData()){
                
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {        
                    Tooltip.install(data.getNode(), new Tooltip(String.valueOf((int)data.getPieValue())));//To change body of generated methods, choose Tools | Templates.
                });
            }
        } catch (SQLException ex) {
            alert.show("Uknown error", ex.getMessage(), Alert.AlertType.ERROR,true);
        }        
        
    }
    
    public void filter(String start, String end, String type, String target){
        
        String whereClause = "";
        
        if(!start.equals("")){
            
            if(whereClause.equals("")){
            
                whereClause += " WHERE add_date >= '" + start + "' " ;
            }
            else {
                
                whereClause += " AND add_date >= '" + start + "' " ;
            }
            
        }
        
        if(!end.equals("")){
            
            if(whereClause.equals("")){
            
                whereClause += " WHERE add_date >= '" + start + "' " ;
            }
            else {
                
                whereClause += " AND add_date >= '" + start + "' " ;
            }
        }
        
        if(!type.equals("All")){
            
            if(whereClause.equals("")){

                if(type.equals("Sold"))
                whereClause += " WHERE sold = 1 " ;
            
                else if(type.equals("In Stock"))
                whereClause += " WHERE sold = 0 ";
            }
            else{
                
                if(type.equals("Sold"))
                whereClause += " AND sold = 1 " ;
            
                else if(type.equals("In Stock"))
                whereClause += " AND sold = 0 ";                
                
            }
            
        }        
        
        Connection con = getConnection();
        String query = "SELECT " + target + ", count(" + target + ") FROM product " + whereClause + "Group by " + target + " ORDER BY count(" + target  +") LIMIT 10";
        PreparedStatement st;
        ResultSet rs;
        
        myChart.getData().clear();      

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
           

            while (rs.next()) {

                list.add(new PieChart.Data(rs.getString(target),rs.getInt("count(" + target  +")")));
                
            }
            
            myChart.setData(list);
            
        
            for(final PieChart.Data data : myChart.getData()){
                
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {        
                    Tooltip.install(data.getNode(), new Tooltip(String.valueOf((int)data.getPieValue())));//To change body of generated methods, choose Tools | Templates.
                });
            }          
            
            con.close();
        }
        catch (SQLException e) {
            alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
        }       
        
    }    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        startDate.setConverter(dateFormatter());
        startDate.getEditor().setText(LocalDate.now().minusDays(30).toString());
        endDate.setConverter(dateFormatter());
        endDate.getEditor().setText(LocalDate.now().toString());
        startDate.setValue(LocalDate.now().minusDays(30));
        endDate.setValue(LocalDate.now());          
        type.setItems(typeList);
        type.getSelectionModel().select(0);
        
        reference.setToggleGroup(columns);
        size.setToggleGroup(columns);
        color.setToggleGroup(columns);
        category.setToggleGroup(columns);
        brand.setToggleGroup(columns);
        
        loadChart("reference");
        
        filter.setOnAction(Action -> {
            
        if(endDate.getValue().compareTo(startDate.getValue()) > 0){            
            
            if(reference.isSelected()){
            filter(startDate.getEditor().getText(),startDate.getEditor().getText(), type.getValue().toString(),reference.getText());
            }
            if(size.isSelected()){
            filter(startDate.getEditor().getText(),startDate.getEditor().getText(), type.getValue().toString(),size.getText());
            }  
            if(color.isSelected()){
            filter(startDate.getEditor().getText(),startDate.getEditor().getText(), type.getValue().toString(),color.getText());
            }  
            if(category.isSelected()){
            filter(startDate.getEditor().getText(),startDate.getEditor().getText(), type.getValue().toString(),category.getText());
            }  
            if(brand.isSelected()){
            filter(startDate.getEditor().getText(),startDate.getEditor().getText(), type.getValue().toString(),brand.getText());
            }
        }
        else {
            
            alert.show("ILLEGAL INTERVAL", "Start date must be inferior than the end date !", Alert.AlertType.WARNING,false);
            
        }  
        });
        
        
        
    }    
    
}
