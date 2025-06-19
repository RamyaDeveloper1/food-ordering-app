package com.ramya.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String paymentMethod;
    private String orderStatus; 

    @ElementCollection
    @CollectionTable(
        name = "order_items",  
        joinColumns = @JoinColumn(name = "order_id") 
    )
    @Column(name = "order_item")   
   private List<OrderItem> orderItems = new ArrayList<>(); 


    private double totalPrice; 
    
      @Column(name = "order_date")  
    private LocalDateTime orderDate;
      
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "user_id")
private User user;



    public Order() {
    }
    

    public Order(Integer orderId, String name, String email, String mobile, String address, String city, String state, 
                 String pincode, String paymentMethod, List<OrderItem> orderItems, double totalPrice, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.paymentMethod = paymentMethod;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.orderStatus = "Pending"; 
        this.orderDate = orderDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        
    }
     public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

   

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode='" + pincode + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
