/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

/**
 *
 * @author Josh Brookes
 */
public class OrderItem extends Item {
    
    private int quantity;
    
    public OrderItem(String _name, int _quantity) {
        super(_name);
        quantity = _quantity;
    }
    public int getQuantity() {
        return quantity;
    }
}
