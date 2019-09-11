/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author med
 */
public class Common implements Init {
    
    private static double  xOffset;
    private static double yOffset;
    
    final static String dateFormat = "yyyy-MM-dd";     

    public static Connection getConnection()
    {
        Connection con;
        try {

            con = DriverManager.getConnection(DB_NAME_WITH_ENCODING, USER, PASSWORD);
            return con;
        }
        catch (SQLException ex) {
            return null;
        }
    }
    
    public static String generateImagePath(File selectedFile)
    {

        java.util.Date date = new java.util.Date();
 
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d-hh-mm-ss");
        
        String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
 
        return UPLOADED_FILE_PATH + sdf.format(date) + fileExtension;
    }
    
    
    public static String saveSelectedImage(File selectedFile)
    {

        String createImagePath = Common.generateImagePath(selectedFile);
        try {

            FileInputStream in = new FileInputStream(selectedFile);
            FileOutputStream out = new FileOutputStream(createImagePath);
            
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        }
        catch(Exception e) {
            e.getMessage();
        }

        return createImagePath;
    }
    
    public static void deleteImage(String filePath)
    {
        try {
            File imageToDelete = new File(filePath);
            imageToDelete.delete();
        }
        catch(Exception e) {}
    }
    
    public static void setDraggable(Parent root, Stage stage){
        
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
    
    public static StringConverter dateFormatter()
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
    
    public static boolean adminsCount(){
    
        try {
            Connection con = getConnection();

            String query = "SELECT count(*) FROM employers WHERE admin = 1";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                if(rs.getInt("count(*)") <= 1){
                    return true;
                }
            }

            con.close();
            
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    
    
    }
        
    
}
