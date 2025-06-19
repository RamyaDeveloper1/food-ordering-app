package com.ramya.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodId;
    
    private String foodName;
    private String foodDescription;
    private Double foodPrice;
    private int foodStock;
    private String foodImage;
    private int foodDiscount;
    private Double foodDiscountPrice;
    private Boolean isAvailable;

   @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
@JoinColumn(name = "sub_category_id")
private SubCategory subCategory;


    // Default constructor
    public Food() {}

    // Updated constructor to match the correct relationships
    public Food(Integer foodId, String foodName, String foodDescription, Double foodPrice, 
                int foodStock, String foodImage, int foodDiscount, Double foodDiscountPrice, 
                Boolean isAvailable, Category category, SubCategory subCategory) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodStock = foodStock;
        this.foodImage = foodImage;
        this.foodDiscount = foodDiscount;
        this.foodDiscountPrice = foodDiscountPrice;
        this.isAvailable = isAvailable;
        this.category = category; 
        this.subCategory = subCategory;
    }


    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodStock() {
        return foodStock;
    }

    public void setFoodStock(int foodStock) {
        this.foodStock = foodStock;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public void setFoodDiscount(int foodDiscount) {
        this.foodDiscount = foodDiscount;
    }

    public Double getFoodDiscountPrice() {
        return foodDiscountPrice;
    }

    public void setFoodDiscountPrice(Double foodDiscountPrice) {
        this.foodDiscountPrice = foodDiscountPrice;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "Food{" +
               "foodId=" + foodId +
               ", foodName='" + foodName + '\'' +
               ", foodDescription='" + foodDescription + '\'' +
               ", foodPrice=" + foodPrice +
               ", foodStock=" + foodStock +
               ", foodImage='" + foodImage + '\'' +
               ", foodDiscount=" + foodDiscount +
               ", foodDiscountPrice=" + foodDiscountPrice +
               ", isAvailable=" + isAvailable +
               ", category=" + category +
               ", subCategory=" + subCategory +
               '}';
    }
}
