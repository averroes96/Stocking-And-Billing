/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JR;

import static Include.Common.getConnection;
import Include.SpecialAlert;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author med
 */
public class JasperReporter extends JFrame{
    
    SpecialAlert alert = new SpecialAlert();
    public HashMap params = new HashMap() ; 
    

    public JasperReporter() throws HeadlessException {
        
    }

    public void ShowReport(String type){
        
        Connection conn = getConnection();
        
        if(type.equals("sellsReport")){
        
        try {
            JasperReport report = JasperCompileManager.compileReport("C:\\Users\\med\\Documents\\NetBeansProjects\\S&B\\src\\JR\\sellDay.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(report, params,conn);
            JRViewer viewer = new JRViewer(jp);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            
            this.add(viewer);
            this.setSize(900, 500);
            this.setVisible(true);
        } catch (JRException ex) {
            alert.show("JR Error", ex.getMessage(), Alert.AlertType.ERROR, true);
        }
        }
        else if(type.equals("productsReport")){
            
        try {
            JasperReport report = JasperCompileManager.compileReport("C:\\Users\\med\\Documents\\NetBeansProjects\\S&B\\src\\JR\\products.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(report, params,conn);
            JRViewer viewer = new JRViewer(jp);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            
            this.add(viewer);
            this.setSize(900, 500);
            this.setVisible(true);
        } catch (JRException ex) {
            alert.show("JR Error", ex.getMessage(), Alert.AlertType.ERROR, true);
        }            
            
        }
        else if(type.equals("versement")){
            
        try {
            JasperReport report = JasperCompileManager.compileReport("C:\\Users\\med\\Documents\\NetBeansProjects\\S&B\\src\\JR\\versment.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(report, params,conn);
            JRViewer viewer = new JRViewer(jp);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            
            this.add(viewer);
            this.setSize(900, 500);            
            this.setVisible(true);
        } catch (JRException ex) {
            alert.show("JR Error", ex.getMessage(), Alert.AlertType.ERROR, true);
        }            
            
        }
        else if(type.equals("paymentsReport")){
            
        try {
            JasperReport report = JasperCompileManager.compileReport("C:\\Users\\med\\Documents\\NetBeansProjects\\S&B\\src\\JR\\payReport.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(report, params,conn);
            JRViewer viewer = new JRViewer(jp);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            
            this.add(viewer);
            this.setSize(900, 500);
            this.setVisible(true);
            
        } catch (JRException ex) {
            alert.show("JR Error", ex.getMessage(), Alert.AlertType.ERROR, true);
        }            
            
        }

        
    }
    
    
    
}
