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
public class Product {

    private SimpleIntegerProperty prodID;
    private SimpleStringProperty reference;
    private SimpleIntegerProperty size;
    private SimpleStringProperty brand;    
    private SimpleIntegerProperty price;
    private SimpleStringProperty addDate;
    private SimpleStringProperty category;
    private SimpleStringProperty color;
    private SimpleStringProperty imageURL;

    public Product() {
        this.prodID = new SimpleIntegerProperty(0);
        this.reference = new SimpleStringProperty("");
        this.size = new SimpleIntegerProperty(0);        
        this.price = new SimpleIntegerProperty(0);
        this.brand = new SimpleStringProperty("");        
        this.addDate = new SimpleStringProperty("");
        this.category = new SimpleStringProperty("");
        this.color = new SimpleStringProperty("");        
    }

    public Product(int id, String reference, int size, String brand, int price, String addDate, String category) {
        this.prodID = new SimpleIntegerProperty(id);
        this.reference = new SimpleStringProperty(reference);
        this.size = new SimpleIntegerProperty(size);
        this.brand = new SimpleStringProperty(brand);        
        this.price = new SimpleIntegerProperty(price);
        this.addDate = new SimpleStringProperty(addDate);
        this.category = new SimpleStringProperty(category);
    }
    
    public int getId() {
        return prodID.get();
    }
 
    public void setId(int id) {
        this.prodID = new SimpleIntegerProperty(id);
    }
 
    public String getReference() {
        return reference.getValue();
    }
 
    public void setReference(String reference) {
        this.reference = new SimpleStringProperty(reference);
    }
 
    public int getPrice() {
        return price.getValue();
    }
 
    public void setPrice(int price) {
        this.price = new SimpleIntegerProperty(price);
    }
 
    public String getAddDate() {
        return addDate.getValue();
    }
 
    public void setAddDate(String addDate) {
        this.addDate = new SimpleStringProperty(addDate);
    }
     
    public String getCategory() {
        return category.getValue();
    }
 
    public void setCategory(String category) {
        this.category = new SimpleStringProperty(category);
    }    

    public int getSize() {
        return size.getValue();
    }

    public void setSize(int size) {
        this.size = new SimpleIntegerProperty(size);
    }

    public String getBrand() {
        return brand.getValue();
    }

    public void setBrand(String brand) {
        this.brand = new SimpleStringProperty(brand);
    }
    
    public String getColor() {
        return color.getValue();
    }

    public void setColor(String color) {
        this.color = new SimpleStringProperty(color);
    }    
    
    public String getImage() {
        return imageURL.getValue();
    }

    public void setImage(String image) {
        this.imageURL = new SimpleStringProperty(image);
    }      
    
    
    
}
