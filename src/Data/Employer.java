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
public class Employer {
    
    private SimpleIntegerProperty empID ;
    private SimpleStringProperty fullname;
    private SimpleIntegerProperty phone;
    private SimpleIntegerProperty salary;
    private SimpleStringProperty joinDate;
    private SimpleIntegerProperty admin;
    private SimpleStringProperty image;
    private String username;
    private String password;

    public Employer() {
        
        this.empID = new SimpleIntegerProperty(0);
        this.fullname = new SimpleStringProperty("");
        this.phone = new SimpleIntegerProperty(0);
        this.salary = new SimpleIntegerProperty(0);
        this.joinDate = new SimpleStringProperty("");
        this.admin = new SimpleIntegerProperty(0);
        this.password = "";
        this.username = "";
        this.image = new SimpleStringProperty("");        
    }

    public int getEmpID() {
        return empID.getValue();
    }

    public void setEmpID(int empID) {
        this.empID = new SimpleIntegerProperty(empID);
    }

    public String getFullname() {
        return fullname.getValue();
    }

    public void setFullname(String fullname) {
        this.fullname = new SimpleStringProperty(fullname);
    }

    public int getPhone() {
        return phone.getValue();
    }

    public void setPhone(int phone) {
        this.phone = new SimpleIntegerProperty(phone);
    }

    public int getSalary() {
        return salary.getValue();
    }

    public void setSalary(int salary) {
        this.salary = new SimpleIntegerProperty(salary);
    }

    public String getJoinDate() {
        return joinDate.getValue();
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = new SimpleStringProperty(joinDate);
    }

    public int getAdmin() {
        return admin.getValue();
    }

    public void setAdmin(int admin) {
        this.admin = new SimpleIntegerProperty(admin);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image.getValue();
    }

    public void setImage(String image) {
        this.image = new SimpleStringProperty(image);
    }
    
    
    
    
    
}
