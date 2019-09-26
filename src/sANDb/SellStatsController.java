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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
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
    @FXML LineChart sumLineChart;
    @FXML DatePicker startDate,endDate;
    @FXML CheckBox sandal,fillete,soiree,sabot,ballerine,moccasin,chaussure,other;
    @FXML ObservableList<Data> list = FXCollections.observableArrayList();
    @FXML Button filter;
    SpecialAlert alert = new SpecialAlert();
    
    ArrayList<String> selectedCats = new ArrayList();    
    
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
            alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
        }         
        
    }
    
    public void loadLineChart(String startDate, String endDate, ArrayList<String> types){
        
        String whereClause = "";
        
        if(!startDate.equals("")){
            
            whereClause += " WHERE sell_date >= '" + startDate + "' " ;
            
        }
        if(!endDate.equals("")){
            
            if(whereClause.equals("")){
                whereClause = " WHERE sell_date <= '" + endDate + "' " ;
            }
            else
            {
                whereClause += " AND sell_date <= '" + endDate + "' ";
            }
            
        }
        if(!types.isEmpty()){
            
            for(String str : types){

              if(whereClause.equals("")){
                  whereClause = " WHERE category = '" + str + "' " ;
              }
              else
              {
                  whereClause = " AND category = '" + str + "' " ;
              }
                
            }
            
        }
        
        Connection con = getConnection();
        String query = "SELECT sell_date, count(*) FROM sell " + whereClause + "Group by sell_date ORDER BY sell_date";
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
                Tooltip.install(data.getNode(), new Tooltip(data.getYValue().toString()));
            });
        }            
            
            con.close();
        }
        catch (SQLException e) {
            alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
        } 

        series.setName("Sells");
                
        
    }
    
    public void loadSumChart(String startDate, String endDate, ArrayList<String> types){
        
        String whereClause = "";
        
        if(!startDate.equals("")){
            
            whereClause += " WHERE sell_date >= '" + startDate + "' " ;
            
        }
        if(!endDate.equals("")){
            
            if(whereClause.equals("")){
                whereClause = " WHERE sell_date <= '" + endDate + "' " ;
            }
            else{
                whereClause += " AND sell_date <= '" + endDate + "' ";
            }
            
        }
        if(!types.isEmpty()){
            
            for(String str : types){

              if(whereClause.equals("")){
                  whereClause += " WHERE category = '" + str + "' " ;
              }
              else{
                  whereClause += " AND category = '" + str + "' " ;
              }
                
            }
            
        }        
        
        Connection con = getConnection();
        String query = "SELECT sell_date, SUM(sellPrice) FROM sell " + whereClause + " Group by sell_date";
        PreparedStatement st;
        ResultSet rs;
        
        System.out.println(query);
        
        sumLineChart.getData().clear();
        XYChart.Series<String,Integer> series = new XYChart.Series<>();

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
           

            while (rs.next()) {

                series.getData().add(new XYChart.Data<>(rs.getString("sell_date"),rs.getInt("SUM(sellPrice)")));
                
            }
            
        sumLineChart.getData().addAll(series);
        
                for(final XYChart.Data<String, Integer> data : series.getData()){
            
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event1) -> {
                Tooltip.install(data.getNode(), new Tooltip(data.getYValue().toString()));//To change body of generated methods, choose Tools | Templates.
            });
        }            
            
            con.close();
        }
        catch (SQLException e) {
            alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
        } 

        series.setName("Total Sum");
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        startDate.setConverter(dateFormatter());
        endDate.setConverter(dateFormatter());

        startDate.getEditor().setText(String.valueOf(LocalDate.now().minusWeeks(1)));
        endDate.getEditor().setText(String.valueOf(LocalDate.now()));
        
        startDate.setValue(LocalDate.now().minusWeeks(1));
        endDate.setValue(LocalDate.now());        
        
        getAllStats();
        loadLineChart(startDate.getEditor().getText(), endDate.getEditor().getText(),selectedCats);
        loadSumChart(startDate.getEditor().getText(), endDate.getEditor().getText(),selectedCats);

        filter.setOnAction(Action -> {
            
        selectedCats.clear();
            
        if(sandal.isSelected())
            selectedCats.add(sandal.getText());
        if(fillete.isSelected())
            selectedCats.add(fillete.getText());
        if(soiree.isSelected())
            selectedCats.add(soiree.getText());
        if(sabot.isSelected())
            selectedCats.add(sabot.getText());
        if(ballerine.isSelected())
            selectedCats.add(ballerine.getText());
        if(moccasin.isSelected())
            selectedCats.add(moccasin.getText());
        if(chaussure.isSelected())
            selectedCats.add(chaussure.getText());
        if(other.isSelected())
            selectedCats.add(other.getText());
        
        if(endDate.getValue().compareTo(startDate.getValue()) > 0){
            
        if(endDate.getValue().compareTo(startDate.getValue()) <= 30){    
        
        loadLineChart(startDate.getEditor().getText(), endDate.getEditor().getText(),selectedCats);
        loadSumChart(startDate.getEditor().getText(), endDate.getEditor().getText(),selectedCats);
        
       }
        else {
            
            alert.show("LARGE INTERVAL", "The larget allowed interval is 30 days ! Please make sure to select a smaller interval", Alert.AlertType.WARNING,false);
            
        }
        
        }
        else {
            
            alert.show("ILLEGAL INTERVAL", "Start date must be inferior than the end date !", Alert.AlertType.WARNING,false);
            
        }        
        
        });
        
       
        
        
        // TODO
    }    
    
}
