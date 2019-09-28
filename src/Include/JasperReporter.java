/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JR;

import Include.SpecialAlert;
import java.awt.HeadlessException;
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
        
        try {         
            JasperReport report = JasperCompileManager.compileReport("C:\\Users\\med\\Documents\\NetBeansProjects\\S&B\\src\\JR\\sellBill.jrxml");
            JasperPrint jp = JasperFillManager.fillReport(report, params);
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
