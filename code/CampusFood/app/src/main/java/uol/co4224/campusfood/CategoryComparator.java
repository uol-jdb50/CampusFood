package uol.co4224.campusfood;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Category>  {
    @Override
    public int compare(Category o1, Category o2) {
        return (o1.getOrder() > o2.getOrder()) ? 1 : -1;
    }
}
