package uol.co4224.campusfood;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order>  {
    @Override
        public int compare(Order o1, Order o2) {
        return (o1.getCollectDate().after(o2.getCollectDate()) ? 1 : -1);
    }
}
