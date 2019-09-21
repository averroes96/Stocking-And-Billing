/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author med
 */
public class SellStatsController implements Initializable {

    
    @FXML Label weekSells,weekSum,monthSells,monthSum,yearSells,yearSum,totalSells,totalSum ;
    @FXML LineChart sellLineChart ;
    @FXML PieChart sellPieChart;
    @FXML ChoiceBox statsFilter;
    @FXML ChoiceBox statsFilter2;
    @FXML ObservableList<Data> list = FXCollections.observableArrayList();
    @FXML ObservableList<String> statsList = FXCollections.observableArrayList("Last Week", "Last Month", "Last Year","Total");
    
    SpecialAlert alert = new SpecialAlert();
    
    public void getAllStats(){
        
        Connection con = getConnection();
        String weekQuery = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ";
        String monthQuery = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 30 day ) ";
        String yearQuery = "SELECT count(*), SUM(sellPrice) FROM sell WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 365 day ) ";
        String query = "SELECT count(*), SUM(sellPrice) FROM sell";
        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(weekQuery);
            rs = st.executeQuery();
            
            int weeksum = 0;
            int weeksells = 0;
            int monthsum = 0;
            int monthsells = 0;
            int yearsells = 0;
            int yearsum = 0;
            int Sum = 0;
            int Sells = 0;            

            while (rs.next()) {
                
                weeksum = rs.getInt("SUM(sellPrice)");
                weeksells = rs.getInt("count(*)");
                
            }
            
            st = con.prepareStatement(monthQuery);
            rs = st.executeQuery();            
            
            while (rs.next()) {
                
                monthsum = rs.getInt("SUM(sellPrice)");
                monthsells = rs.getInt("count(*)");
                
            }
            
            st = con.prepareStatement(yearQuery);
            rs = st.executeQuery();            
            
            while (rs.next()) {
                
                yearsum = rs.getInt("SUM(sellPrice)");
                yearsells = rs.getInt("count(*)");
                
            }

            st = con.prepareStatement(yearQuery);
            rs = st.executeQuery();            
            
            while (rs.next()) {
                
                Sum = rs.getInt("SUM(sellPrice)");
                Sells = rs.getInt("count(*)");
                
            }

            st = con.prepareStatement(query);
            rs = st.executeQuery();            
            
            while (rs.next()) {
                
                Sum = rs.getInt("SUM(sellPrice)");
                Sells = rs.getInt("count(*)");
                
            }            
            
            weekSum.setText(String.valueOf(weeksum) + " DA");
            weekSells.setText(String.valueOf(weeksells) + " Sell(s)");
            monthSum.setText(String.valueOf(monthsum) + " DA");
            monthSells.setText(String.valueOf(monthsells) + " Sell(s)");            
            yearSum.setText(String.valueOf(yearsum) + " DA");
            yearSells.setText(String.valueOf(yearsells) + " Sell(s)");
            totalSum.setText(String.valueOf(Sum) + " DA");
            totalSells.setText(String.valueOf(Sells) + " Sell(s)");                         
            

            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }         
        
    }
    
    public void loadLineChart(String selectedDate){
        
        Connection con = getConnection();
        String query = "SELECT sell_date, count(*) FROM sell " + selectedDate + " Group by sell_date ORDER BY sell_date";
        PreparedStatement st;
        ResultSet rs;
        
        sellLineChart.getData().clear();
        XYChart.Series<String,Integer> series = new XYChart.Series<>();

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
           

            while (rs.next()) {

                series.getData().add(new XYChart.Data<>(rs.getString("sell_date"),rs.getInt("count(*)")));
                
            }
            
        sellLineChart.getData().addAll(series);
        
                for(final XYChart.Data<String, Integer> data : series.getData()){
            
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {
                Tooltip.install(data.getNode(), new Tooltip(data.getXValue()));//To change body of generated methods, choose Tools | Templates.
            });
        }            
            
            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        } 

        series.setName("Sells");
                
        
    }
    
    public void loadPieChart(String selectedDate){
        
        try {
            Connection con = getConnection();
            String query = "SELECT category, count(category) FROM product,sell WHERE sold = 1 " + selectedDate + " AND product.prod_id = sell.prod_id Group by category";
            PreparedStatement st;
            ResultSet rs;
            
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            
            System.out.println(query);
            
            
            while(rs.next()){
                
                list.add(new PieChart.Data(rs.getString("category"),rs.getInt("count(category)")));
                
            }
            
            sellPieChart.setData(list);
            for(final PieChart.Data data : sellPieChart.getData()){
                
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {        
                    Tooltip.install(data.getNode(), new Tooltip(String.valueOf((int)data.getPieValue())));//To change body of generated methods, choose Tools | Templates.
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(SellStatsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        getAllStats();
        loadLineChart(" WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ");
        loadPieChart(" AND sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ");
        
        statsFilter.setOnAction(Action -> {

                switch(statsFilter.getValue().toString()){
                    
                    case "Last Week" : sellLineChart.getData().clear();
                    sellPieChart.getData().clear();
                    loadLineChart(" WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ");
                    loadPieChart(" AND sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 7 day ) ");
                    break;
                    
                    case "Last Month": sellLineChart.getData().clear();
                    sellPieChart.getData().clear();
                    loadLineChart(" WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 30 day ) ");
                    loadPieChart(" AND sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 30 day ) ");
                    break;
                    
                    case "Last Year": sellLineChart.getData().clear();
                    sellPieChart.getData().clear();
                    loadLineChart(" WHERE sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 365 day ) ");
                    loadPieChart(" AND sell_date <= curdate() AND sell_date >= date(curdate() - INTERVAL 365 day ) ");
                    break;
                    
                    case "Total": sellLineChart.getData().clear();
                    sellPieChart.getData().clear();
                    loadLineChart("");
                    loadPieChart("");
                    break;
                }   
            
        });

        statsFilter.setItems(statsList);
        statsFilter.getSelectionModel().select(0);
        
       
        
        
        // TODO
    }    
    
}
