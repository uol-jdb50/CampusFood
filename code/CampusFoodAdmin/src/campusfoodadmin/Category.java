/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.util.UUID;

/**
 *
 * @author Josh Brookes
 */
public class Category {
    private String categoryID;
    private String name;
    private int order;
    private boolean toBeDeleted;
    
    public Category(String _name, int _order) {
        categoryID = UUID.randomUUID().toString();
        name = _name;
        order = _order;
        toBeDeleted = false;
    }
    public Category(String _id, String _name, int _order) {
        categoryID = _id;
        name = _name;
        order = _order;
        toBeDeleted = false;
    }
    public void setName(String _name) {
        name = _name;
    }
    public void setOrder(int _order) {
        order = _order;
    }
    public void setDelete() {
        toBeDeleted = true;
    }
    public String getID() {
        return categoryID;
    }
    public String getName() {
        return name;
    }
    public int getOrder() {
        return order;
    }
    public boolean getDeleteStatus() {
        return toBeDeleted;
    }
}
