/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

/**
 *
 * @author med
 */
public interface Init {

    String DB_NAME = "jdbc:mysql://localhost/sandb";
 
    String ENCODING = "?useUnicode=yes&characterEncoding=UTF-8";
 
    String DB_NAME_WITH_ENCODING = DB_NAME + ENCODING;
 
    String USER = "root";
 
    String PASSWORD = "";
    
    String UPLOADED_FILE_PATH = "C:\\Users\\med\\Documents\\sandb-files\\";
 

    
}
