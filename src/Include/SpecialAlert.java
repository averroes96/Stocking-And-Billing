/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Include;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*
 *
 * @author med
 */

public class SpecialAlert {
    
    Alert alert = new Alert(AlertType.NONE);

    public void show(String title, String message, AlertType alertType) {
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();

    }
}    
    

