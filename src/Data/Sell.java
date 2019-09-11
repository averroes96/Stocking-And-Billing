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
public class Sell {
    
    private SimpleIntegerProperty sellID;
    private SimpleIntegerProperty sellPrice;
    private SimpleStringProperty sellDate;    
    private SimpleStringProperty sellRef;
    private Product product ;

    public Sell() {
        
        this.sellID = new SimpleIntegerProperty(0);
        this.sellDate = new SimpleStringProperty("");
        this.sellPrice = new SimpleIntegerProperty(0);
        this.product = new Product();
        this.sellRef = new SimpleStringProperty("");
        
    }

    public int getSellID() {
        return sellID.getValue();
    }

    public void setSellID(int sellID) {
        this.sellID = new SimpleIntegerProperty(sellID);
    }

    public int getSellPrice() {
        return sellPrice.getValue();
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = new SimpleIntegerProperty(sellPrice);
    }

    public String getSellDate() {
        return sellDate.getValue();
    }

    public void setSellDate(String sellDate) {
        this.sellDate = new SimpleStringProperty(sellDate);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSellRef() {
        return sellRef.getValue();
    }

    public void setSellRef(String sellRef) {
        this.sellRef = new SimpleStringProperty(sellRef);
    }


    
    
    
    
    
    
}
