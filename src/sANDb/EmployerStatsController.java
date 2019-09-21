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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author med
 */
public class EmployerStatsController implements Initializable {

    @FXML DatePicker startDate,endDate;
    @FXML ChoiceBox type;
    @FXML BarChart sellsPerEmployer;
    @FXML ListView<String> empList;
    
    ObservableList<String> typeList = FXCollections.observableArrayList("All","Active","Non-Active");
    ObservableList<String> list = FXCollections.observableArrayList();
    
    public SpecialAlert alert = new SpecialAlert();
    
    public void loadBarChart(String selectedDate){
        
        Connection con = getConnection();
        String query = "SELECT fullname, count(sell_id) FROM employers, sell WHERE employers.emp_id = sell.emp_id " + selectedDate + " Group by fullname";
        PreparedStatement st;
        ResultSet rs;
        
        sellsPerEmployer.getData().clear();
        empList.getItems().clear();
        list.clear();
        XYChart.Series<String,Integer> series = new XYChart.Series<>();

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
           

            while (rs.next()) {

                series.getData().add(new XYChart.Data<>(rs.getString("fullname"),rs.getInt("count(sell_id)")));
                list.add(rs.getString("fullname") + "\t" + rs.getInt("count(sell_id)"));
                
            }
            
        sellsPerEmployer.getData().addAll(series);
        empList.setItems(list);
        
                for(final XYChart.Data<String, Integer> data : series.getData()){
            
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {
                Tooltip.install(data.getNode(), new Tooltip(String.valueOf(data.getYValue().intValue())));//To change body of generated methods, choose Tools | Templates.
            });
        }            
            
            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        } 

        series.setName("Sells");
                
        
        
    }

    public void filter(String start, String end, String type){
        
        String whereClause = "";
        
        if(!start.equals("")){
            
                whereClause += " AND sell_date >= '" + start + "' " ;
            
        }
        
        if(!end.equals("")){
            
                whereClause += " AND sell_date <= '" + end + "' ";
        }
        
        if(!type.equals("")){

                if(type.equals("Active"))
                whereClause += " AND active = 1 " ;
            
                else if(type.equals("Non-Active"))
                whereClause += " AND active = 0 ";
            
        }        
        
        Connection con = getConnection();
        String query = "SELECT fullname, count(sell_id) FROM employers, sell WHERE employers.emp_id = sell.emp_id" + whereClause + "Group by fullname";
        PreparedStatement st;
        ResultSet rs;
        
        
        sellsPerEmployer.getData().clear();
        empList.getItems().clear();
        list.clear();        
        XYChart.Series<String,Integer> series = new XYChart.Series<>();

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
           

            while (rs.next()) {

                series.getData().add(new XYChart.Data<>(rs.getString("fullname"),rs.getInt("count(sell_id)")));
                list.add(rs.getString("fullname") + "\t" + rs.getInt("count(sell_id)"));
                
            }
            
        sellsPerEmployer.getData().addAll(series);
        empList.setItems(list);
        
                for(final XYChart.Data<String, Integer> data : series.getData()){
            
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {
                Tooltip.install(data.getNode(), new Tooltip(String.valueOf(data.getYValue().intValue())));//To change body of generated methods, choose Tools | Templates.
            });
        }            
            
            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        } 

        series.setName("Sells");        
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadBarChart("");
        startDate.setConverter(dateFormatter());
        endDate.setConverter(dateFormatter());
        type.setItems(typeList);
        type.setValue("ALL");

        type.setOnAction(Action -> {
        
            filter(startDate.getEditor().getText(),endDate.getEditor().getText(),type.getValue().toString());
        
        });
        startDate.setOnAction(Action -> {
        
            filter(startDate.getEditor().getText(),endDate.getEditor().getText(),type.getValue().toString());
        
        });
        endDate.setOnAction(Action -> {
        
            filter(startDate.getEditor().getText(),endDate.getEditor().getText(),type.getValue().toString());
        
        });        
        
        // TODO
    }    
    
}
