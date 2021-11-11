/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Josh Brookes
 */
public class Order {
    private String orderId;
    private String userId;
    private String locationId;
    private Date orderDate;
    private Date collectDate;
    private double price;
    private boolean checkedIn;
    private boolean collected;
    private String name;
    private List<OrderItem> items = new ArrayList<>();
    private int allergenMatrix;
    
    public Order(String _orderId, String _userId, String _locationId, Date _orderDate, Date _collectDate, double _price, boolean _checkedIn, boolean _collected, String _name) {
        orderId = _orderId;
        userId = _userId;
        locationId = _locationId;
        orderDate = _orderDate;
        collectDate = _collectDate;
        price = _price;
        checkedIn = _checkedIn;
        collected = _collected;
        name = _name;
        allergenMatrix = 0;
    }
    public String getOrderId() {
        return orderId;
    }
    public String getUserId() {
        return userId;
    }
    public String getLocationId() {
        return locationId;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public Date getCollectDate() {
        return collectDate;
    }
    public double getPrice() {
        return price;
    }
    public String getCheckedIn() {
        return (checkedIn ? "Yes" : "No");
    }
    public String getCollected() {
        return (collected ? "Yes" : "No");
    }
    public String getName() {
        return name;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public String getAllergens() {
        String output = "";
        String[] names = new String[] {"Cel., ", "Glu., ", "Crus., ", "Egg, ", "Fish, ", "Lupin, ", "Milk, ", "Moll., ", "Mus., ", "Nuts, ", "Peanuts, ", "Ses., ", "Soya, ", "Sul., ", "Ve, ", "V, "};
        int[] matrix = new int[16];
        int allergen = allergenMatrix;
        for (int i = 0; i < 16; i++) {
            matrix[i] = allergen % 2;
            allergen /= 2;
        }
        for (int i = 0; i < 16; i++) {
            if (matrix[i] == 1) output += names[i];
        }
        if (output.equals("")) output += "None";
        else output = output.substring(0, output.length() - 2);
        return output;
    }
    public void setCheckedIn(boolean b) {
        checkedIn = b;
        adjustOrder();
    }
    public void setCollected(boolean b) {
        collected = b;
        adjustOrder();
    }
    public void setItems(List<OrderItem> _items) {
        items = _items;
    }
    public void setAllergens(int _allergenMatrix) {
        allergenMatrix = _allergenMatrix;
    }
    private void adjustOrder() {
        try {
            Statement stmt = CampusFood.connection.createStatement();
            String sql = "UPDATE Orders SET CheckedIn=" + (checkedIn ? 1 : 0) + ", Collected=" + (collected ? 1 : 0) + " WHERE OrderID='" + orderId + "';";
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
