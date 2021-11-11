package uol.co4224.campusfood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collections;

public class DealPart implements Serializable {

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
    public Item item;

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
