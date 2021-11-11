package uol.co4224.campusfood;

import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Basket implements Serializable {
    private List<Item> items;
    private double total;
    private String campus;
    private String location;
    private Date date;
    private int allergenMatrix;

    public Basket(String _campus, String _location, Date _date, int _allergenMatrix) {
        campus = _campus;
        location = _location;
        items = new ArrayList<>();
        total = 0;
        date = _date;
        allergenMatrix = _allergenMatrix;
    }

    public void addItem(Item _item) {
        items.add(_item);
        total += _item.getPrice();
    }

    public void addDealItem(Item _item) {
        items.add(_item);
    }

    public void addDealPrice(double _price) {
        total += _price;
    }

    public void removeItem(Item _item) {
        if (items.remove(_item)) {
            total -= _item.getPrice();
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotal() {
        return total;
    }

    public List<Item> getBasket() {
        return items;
    }
    public String getCampus() {
        return campus;
    }
    public String getLocation() {
        return location;
    }
    public Date getDate() { return date; }
    public int getAllergenMatrix() {return allergenMatrix;}

}
