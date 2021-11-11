package uol.co4224.campusfood;

public class Category {
    private String name;
    private int order;

    public Category(String _name, int _order) {
        name = _name;
        order = _order;
    }
    public String getName() {
        return name;
    }
    public int getOrder() {
        return order;
    }
}
