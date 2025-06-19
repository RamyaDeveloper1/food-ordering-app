package com.ramya.model;

import javax.persistence.Embeddable;

@Embeddable  
public class OrderItem {
    private String foodName;
    private int quantity;
    private double price;

    public OrderItem() {
    }
    

    public OrderItem(String foodName, int quantity, double price) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return price * quantity; 
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "foodName='" + foodName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
