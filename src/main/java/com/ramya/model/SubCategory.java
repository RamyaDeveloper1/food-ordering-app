package com.ramya.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "subcategory")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
    private String subCategoryName;
    private String categoryDescription;  
    private Boolean isAvailable;
    private String categoryImage; 

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id") 
    private Category category;
   
  @OneToMany(mappedBy = "subCategory") 
    private Set<Food> foods;  

    public SubCategory() {

    }

    public SubCategory(Integer id, String subCategoryName, String categoryDescription, Boolean isAvailable, String categoryImage, Category category) {
        this.id = id;
        this.subCategoryName = subCategoryName;
        this.categoryDescription = categoryDescription;
        this.isAvailable = isAvailable;
        this.categoryImage = categoryImage;
        this.category = category;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
        public Set<Food> getFoods() {
        return foods;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
               "id=" + id +
               ", subCategoryName='" + subCategoryName + '\'' +
               ", categoryDescription='" + categoryDescription + '\'' +
               ", isAvailable=" + isAvailable +
               ", categoryImage='" + categoryImage + '\'' +
               '}';
    }
}
