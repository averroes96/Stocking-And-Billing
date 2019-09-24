/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sANDb;

import static Include.Common.getConnection;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;


public class ProductStatsController implements Initializable {

    @FXML public PieChart myChart;
    @FXML public RadioButton reference,size,color,category,brand;
    @FXML public DatePicker startDate,endDate;
    @FXML public ChoiceBox type;
    @FXML public Button filter;
    
    private ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
    
    
    
    public void loadChart(){
        
        try {
            Connection con = getConnection();
            String query = "SELECT reference, count(reference) FROM product WHERE sold = 0 Group by reference ORDER BY count(reference) LIMIT 10";
            PreparedStatement st;
            ResultSet rs;
            
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while(rs.next()){
                
                list.add(new PieChart.Data(rs.getString("reference"),rs.getInt("count(reference)")));
                
            }
            
            myChart.setData(list);
            for(final PieChart.Data data : myChart.getData()){
                
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
        
        loadChart();
        
    }    
    
}
