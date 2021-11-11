/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

/**
 *
 * @author Josh Brookes
 */
public class Item {
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String locationId;
    private double price;
    private boolean[] allergens = new boolean[16];
    private int order;
    private boolean toBeDeleted;
    
    public Item(String _name) {
        name = _name;
    }
    public Item(String _name, String _categoryId, String _locationId, int _order) {
        id = UUID.randomUUID().toString();
        name = _name;
        description = "";
        categoryId = _categoryId;
        locationId = _locationId;
        price = 0;
        for (boolean b: allergens) {
            b = false;
        }
        order = _order;
        toBeDeleted = false;
    }
    public Item(String _id, String _name, String _description, String _categoryId, String _locationId, double _price, int _allergenMatrix, int _order) {
        id = _id;
        name = _name;
        description = _description;
        categoryId = _categoryId;
        locationId = _locationId;
        price = _price;
        String matrix = Integer.toBinaryString(_allergenMatrix);
        int count = 0;
        while (true) {
            if (matrix.equals("")) break;
            allergens[count] = matrix.charAt(0) == '1';
            matrix = matrix.substring(1);
            count++;
        }
        order = _order;
        toBeDeleted = false;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public String getLocationId() {
        return locationId;
    }
    public double getPrice() {
        return price;
    }
    public boolean getAllergen(int index) {
        return allergens[index];
    }
    public int getOrder() {
        return order;
    }
    public boolean getDeleteStatus() {
        return toBeDeleted;
    }
    public int getAllergenMatrix() {
        int matrix = 0;
        for (int i = 0; i < 15; i++) {
            if (allergens[i] == true) matrix += Math.pow(2, i);
        }
        return matrix;
    }
    public void setId(String _id) {
        id = _id;
    }
    public void setName(String _name) {
        name = _name;
    }
    public void setDescription(String _desc) {
        description = _desc;
    }
    public void setCategoryId(String _id) {
        categoryId = _id;
    }
    public void setLocationId(String _id) {
        locationId = _id;
    }
    public void setPrice(double _price) {
        price = _price;
    }
    public void setAllergen(boolean b, int index) {
        allergens[index] = b;
    }
    public void setOrder(int _order) {
        order = _order;
    }
    public void setDelete() {
        toBeDeleted = true;
    }
}
