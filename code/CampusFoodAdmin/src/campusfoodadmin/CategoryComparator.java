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
public class CategoryComparator implements Comparator<Category> {

    @Override
    public int compare(Category o1, Category o2) {
        return o1.getOrder() - o2.getOrder();
    }
    
}
