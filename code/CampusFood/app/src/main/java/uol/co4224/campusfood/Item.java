package uol.co4224.campusfood;

import java.io.Serializable;

public class Item implements BasketItem, Serializable {
    private String id;
    private String category;
    private int allergenMatrix;
    private boolean[] allergens = new boolean[15];
    private int order;
    private String name;
    private String desc;
    private double price;

    public Item(String _name, String _desc, String _category, double _price, int _allergenMatrix, int _order) {
        category = _category;
        allergenMatrix = _allergenMatrix;
        order = _order;
        name = _name;
        desc = _desc;
        price = _price;
    }
    public Item(String _id, String _name, String _desc, String _category, double _price, int _allergenMatrix, int _order) {
        id = _id;
        category = _category;
        allergenMatrix = _allergenMatrix;
        order = _order;
        name = _name;
        desc = _desc;
        price = _price;
    }
    public String getCategory() {return category;}
    public void setCategory(String _category) { category = _category; }
    public String getAllergenString() {
        String output = "Allergens: ";
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
        if (output.equals("Allergens: ")) output += "None listed";
        else output = output.substring(0, output.length() - 2);
        return output;
    }
    public int getOrder() {return order;}

    public String getId() { return id; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public double getPrice() {
        return price;
    }
    public void setPrice(double _price) { price = _price; }
}
