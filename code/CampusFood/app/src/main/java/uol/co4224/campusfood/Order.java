package uol.co4224.campusfood;

import java.util.Date;

public class Order {
    private String id;
    private String campus;
    private String location;
    private Date collectDate;
    private double price;
    private String collectName;

    public Order(String _id, String _campus, String _location, Date _collectDate, double _price, String _collectName) {
        id = _id;
        campus = _campus;
        location = _location;
        collectDate = _collectDate;
        price = _price;
        collectName = _collectName;
    }
    public String getId() { return id; }
    public String getCampus() { return campus; }
    public String getLocation() { return location; }
    public Date getCollectDate() { return collectDate; }
    public double getPrice() { return price; }
    public String getCollectName() { return collectName; }
}
