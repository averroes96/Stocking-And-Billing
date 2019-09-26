/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    
    private static SpecialAlert alert = new SpecialAlert();
    
    final static String dateFormat = "yyyy-MM-dd";     

    public static Connection getConnection()
    {
        Connection con;
        try {

            con = DriverManager.getConnection(DB_NAME_WITH_ENCODING, USER, PASSWORD);
            return con;
        }
        catch (SQLException ex) {
            alert.show("Database down !", "An error occured while trying to connect to the database !", Alert.AlertType.ERROR,true);
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
        catch(IOException e) {
            alert.show("Uknown error", e.getMessage(), Alert.AlertType.ERROR,true);
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
    
    public static int adminsCount(){
    
        try {
            Connection con = getConnection();
            
            int count = 0 ;

            String query = "SELECT count(*) FROM employers WHERE admin = 1";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                
                count = rs.getInt("count(*)");
                
            }

            con.close();
            
            return count;
        } catch (SQLException ex) {
            alert.show("Uknown error", ex.getMessage(), Alert.AlertType.ERROR,true);
            return 0;
        }
    
    
    }
    
    public static void minimize(MouseEvent event){
        
        ((Stage)((Label)event.getSource()).getScene().getWindow()).setIconified(true);
        
    }

    public static int getPrice(String ref){
        
        Connection con = getConnection();
        String query = "SELECT * FROM product WHERE reference = ? LIMIT 1";
        int targetedPrice = 0;

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1, ref);
            rs = st.executeQuery();
            int count = 0;
            while (rs.next()) {
                
                targetedPrice = rs.getInt("price");
                
            }
            
            con.close();
            
            return targetedPrice;


        }
        catch (SQLException e) {
            alert.show("Uknown Error", e.getMessage(), Alert.AlertType.ERROR,true);
            return 0;
        }         
        
    }

    public static boolean refExist(String ref){
        
        Connection con = getConnection();
        String query = "SELECT * FROM product WHERE reference = ? LIMIT 1";
        boolean found = false ;

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement(query);
            st.setString(1, ref);
            rs = st.executeQuery();
            int count = 0;
            
            if(rs.next()){
                
                found = true;
            }
            
            con.close();
            
            return found;


        }
        catch (SQLException e) {
            alert.show("Uknown error !", e.getMessage(), Alert.AlertType.ERROR,true);
            return false;
        }         
        
    }     
        
    
}
