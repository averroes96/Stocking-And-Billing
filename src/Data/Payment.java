/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author med
 */
public class Payment {
    
    private SimpleIntegerProperty payID;
    private SimpleIntegerProperty paid;
    private SimpleIntegerProperty rest;    
    private SimpleStringProperty payDate;
    private SimpleStringProperty prodReference;
    private Sell sell;

    public Payment() {
        
        this.payID = new SimpleIntegerProperty(0);
        this.payDate = new SimpleStringProperty("");
        this.paid = new SimpleIntegerProperty(0);
        this.prodReference = new SimpleStringProperty("");
        this.rest = new SimpleIntegerProperty(0);
    }
    
    

    public int getPayID() {
        return payID.getValue();
    }

    public void setPayID(int payID) {
        this.payID = new SimpleIntegerProperty(payID);
    }

    public int getPaid() {
        return paid.getValue();
    }

    public void setPaid(int paid) {
        this.paid = new SimpleIntegerProperty(paid);
    }

    public int getRest() {
        return rest.getValue();
    }

    public void setRest(int rest) {
        this.rest = new SimpleIntegerProperty(rest);
    }

    public String getPayDate() {
        return payDate.getValue();
    }

    public void setPayDate(String payDate) {
        this.payDate = new SimpleStringProperty(payDate);
    }

    public Sell getSell() {
        return sell;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }
    
    public String getProdReference() {
        return prodReference.getValue();
    }

    public void setProdReference(String prodRef) {
        this.prodReference = new SimpleStringProperty(prodRef);
    }
        
    
    
    
}
