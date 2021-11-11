/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import campusfoodadmin.DealPart.PartType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Josh Brookes
 */
public class Deal {
        
    private String id;
    private String name;
    private Date startDate;
    private Date endDate;
    private double price;
    private List<DealPart> dealString = new ArrayList<DealPart>();
    private boolean toBeDeleted;
    
    public Deal() {
        id = UUID.randomUUID().toString();
        name = "";
    }
    public Deal(String _id, String _name, Date _startDate, Date _endDate, double _price, String _dealString) {
        id = _id;
        name = _name;
        startDate = _startDate;
        endDate = _endDate;
        price = _price;
        setDealStringFromString(_dealString);
        toBeDeleted = false;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public double getPrice() {
        return price;
    }
    public List<DealPart> getDealStringList() {
        return dealString;
    }
    public String getDealString() {
        String deal = "";
        for (DealPart p: dealString) {
            switch (p.type) {
                case STATEMENT_OPEN:
                    deal += "(";
                    break;
                case STATEMENT_CLOSE:
                    deal += ")";
                    break;
                case ITEMGROUP_OPEN:
                    deal += "{";
                    break;
                case ITEMGROUP_CLOSE:
                    deal += "}";
                    break;
                case AND:
                    deal += "*";
                    break;
                case OR:
                    deal += "/";
                    break;
                case ITEM:
                    if (!deal.endsWith("{")) deal += ",";
                    deal += p.itemId;
                    if (p.surcharge > 0) deal += "[" + p.surcharge + "]";
                    break;
            }
        }
        return deal;
    }
    public boolean getDeleteStatus() {
        return toBeDeleted;
    }
    public void setId(String _id) {
        id = _id;
    }
    public void setName(String _name) {
        name = _name;
    }
    public void setStartDate(Date _date) {
        startDate = _date;
    }
    public void setEndDate(Date _date) {
        endDate = _date;
    }
    public void setPrice(double _price) {
        price = _price;
    }
    public void addPartToDeal(PartType type) {
        dealString.add(new DealPart(type));
    }
    public void addItemToDeal(String itemId, double surcharge) {
        if (surcharge == 0) {
            dealString.add(new DealPart(PartType.ITEM, itemId));
        } else {
            dealString.add(new DealPart(PartType.ITEM, itemId, surcharge));
        }
    }
    public void removeFromDeal(int index) {
        dealString.remove(index);
    }
    public void setDealStringFromString(String _dealString) {
        while (true) {
            if (_dealString.equals("")) break;
            char c = _dealString.charAt(0);
            switch (c) {
                case '{':
                    dealString.add(new DealPart(PartType.ITEMGROUP_OPEN));
                    break;
                case '}':
                    dealString.add(new DealPart(PartType.ITEMGROUP_CLOSE));
                    break;
                case '(':
                    dealString.add(new DealPart(PartType.STATEMENT_OPEN));
                    break;
                case ')':
                    dealString.add(new DealPart(PartType.STATEMENT_CLOSE));
                    break;
                case '*':
                    dealString.add(new DealPart(PartType.AND));
                    break;
                case '/':
                    dealString.add(new DealPart(PartType.OR));
                    break;
                default:
                    String item = _dealString.substring(0, 36);
                    String surcharge = "";
                    if (_dealString.charAt(36) == ',' || _dealString.charAt(36) == '}') dealString.add(new DealPart(PartType.ITEM, item));
                    _dealString = _dealString.substring(36);
                    if (_dealString.charAt(0) == '[') {
                        _dealString = _dealString.substring(1);
                        while (true) {
                            if (_dealString.charAt(0) == ']') {
                                _dealString = _dealString.substring(1);
                                break;
                            }
                            surcharge += _dealString.charAt(0);
                            _dealString = _dealString.substring(1);
                        }
                        dealString.add(new DealPart(PartType.ITEM, item, Double.parseDouble(surcharge)));
                    }
                    break;
            }
            _dealString = _dealString.substring(1);
        }
    }
    public void setDealStringFromList(List _dealString) {
        dealString = _dealString;
    }
    public void setDelete() {
        toBeDeleted = true;
    }
    public boolean isValid() {
        int index = 0;
        int partLength = dealString.size() - 1;
        boolean valid = true;
        int statementLevel = 0;
        int itemGroupLevel = 0;
        boolean containsItem = false;
        boolean groupHasItem = false;
        for (DealPart p: dealString) {
            switch (p.type) {
                case STATEMENT_OPEN:
                    statementLevel++;
                    if (itemGroupLevel == 1) return false;
                    break;
                case STATEMENT_CLOSE:
                    statementLevel--;
                    if (itemGroupLevel == 1) return false;
                    break;
                case ITEMGROUP_OPEN:
                    itemGroupLevel++;
                    groupHasItem = false;
                    break;
                case ITEMGROUP_CLOSE:
                    itemGroupLevel--;
                    if (!groupHasItem) return false;
                    break;
                case AND:
                    if (itemGroupLevel == 1) return false;
                    if (index == 0 || index == partLength) return false;
                    if (dealString.get(index - 1).type == PartType.AND || dealString.get(index - 1).type == PartType.OR) return false;
                    break;
                case OR:
                    if (itemGroupLevel == 1) return false;
                    if (index == 0 || index == partLength) return false;
                    if (dealString.get(index - 1).type == PartType.AND || dealString.get(index - 1).type == PartType.OR) return false;
                    break;
                case ITEM:
                    containsItem = true;
                    groupHasItem = true;
                    if (itemGroupLevel == 0) return false;
                    break;
            }
            if (statementLevel < 0) return false;
            else if (itemGroupLevel < 0 || itemGroupLevel > 1) return false;
            index++;
        }
        if (!containsItem) return false;
        return true;
    }
    
}
