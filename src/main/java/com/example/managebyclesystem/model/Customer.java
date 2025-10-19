package com.example.managebyclesystem.model;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "customer")
@Check(constraints = "cardType IN ('Brown','Silver','Gold','Diamond') AND customerStatus IN('Able','Disable')")

public class Customer {
    enum CardType{
    Brown,
    Silver,
    Gold,
    Diamond
}
    enum CustomerStatus{
        Able,
        Disable
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String customerName;
    private String customerPhone;
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private CardType cardType;
    private int rewardPoints;
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    public Customer(){}
    public int getId(){
        return id;
    }
    public String getCustomerName(){
        return customerName;
    }
    public void setCustomerName(String name){
        this.customerName=name;
    }
    public String getCustomerPhone(){
        return customerPhone;
    }
    public void setCustomerPhone(String phone){
        this.customerPhone = phone;
    }
    public String getCustomerEmail(){
        return customerEmail;
    }




}
