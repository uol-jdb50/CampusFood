/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

/**
 *
 * @author Josh Brookes
 */
public class DealPart {
    enum PartType {
        STATEMENT_OPEN ("Statement Group Open"),
        STATEMENT_CLOSE ("Statement Group Close"),
        ITEMGROUP_OPEN ("Item Group Open"),
        ITEMGROUP_CLOSE ("Item Group Close"),
        AND ("AND"),
        OR ("OR"),
        ITEM ("");
        
        private final String name;
        
        private PartType(String s) {
            name = s;
        }
        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    public PartType type;
    public String itemId;
    public double surcharge;
    
    public DealPart(PartType _type) {
        type = _type;
    }
    public DealPart(PartType _type, String _itemId) {
        type = _type;
        itemId = _itemId;
    }
    public DealPart(PartType _type, String _itemId, double _surcharge) {
        type = _type;
        itemId = _itemId;
        surcharge = _surcharge;
    }
}
