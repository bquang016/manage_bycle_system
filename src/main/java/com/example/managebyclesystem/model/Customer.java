package com.example.managebyclesystem.model;

import com.example.managebyclesystem.constants.CustomerCardType;
import com.example.managebyclesystem.constants.CustomerStatus;
import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int rewardPoints = 0;
    @Enumerated(EnumType.STRING)
    private CustomerCardType cardType = CustomerCardType.BROWN;
    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ABLE;

    public Customer(String customerName, String customerPhone, String customerEmail,
                    int rewardPoints, CustomerCardType cardType){
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.rewardPoints = rewardPoints;
        this.cardType = cardType;
    }
    public Customer(String name, String phone, String email) {
        this.customerName = name;
        this.customerPhone = phone;
        this.customerEmail = email;
    }

    public Customer(){

    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int id) { this.customerId = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public int getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(int rewardPoints) { this.rewardPoints = rewardPoints; }

    public CustomerCardType getCardType() { return cardType; }
    public void setCardType(CustomerCardType cardType) { this.cardType = cardType; }

    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }

}