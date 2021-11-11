/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.util.Comparator;

/**
 *
 * @author Josh Brookes
 */
public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
