package uol.co4224.campusfood;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item>  {
    @Override
    public int compare(Item o1, Item o2) {
        return (o1.getOrder() > o2.getOrder()) ? 1 : -1;
    }
}
