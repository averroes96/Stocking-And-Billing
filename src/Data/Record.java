/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Record {
    
    private SimpleStringProperty date;
    private SimpleStringProperty absPres;
    private SimpleIntegerProperty payment;
    private int employerID;
    private int recordID;

    public Record() {
        
        this.date = new SimpleStringProperty("");
        this.absPres = new SimpleStringProperty("");
        this.payment = new SimpleIntegerProperty(0);
        this.employerID = 0;
    }

    public String getDate() {
        return date.getValue();
    }

    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }

    public String getAbsPres() {
        return absPres.getValue();
    }

    public void setAbsPres(String absPres) {
        this.absPres = new SimpleStringProperty(absPres);
    }

    public int getPayment() {
        return payment.getValue();
    }

    public void setPayment(int payment) {
        this.payment = new SimpleIntegerProperty(payment);
    }

    public int getEmployerID() {
        return employerID;
    }

    public void setEmployerID(int employerID) {
        this.employerID = employerID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }
    
    

    
    
}
