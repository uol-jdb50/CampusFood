package uol.co4224.campusfood;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Deal implements BasketItem, Serializable {

    private String dealString;
    private List<DealPart> deal;
    public List<Item> itemDirectory = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private String name;
    private String desc;
    private double price;

    public Deal(String _dealString, String _name, String _desc, Date _endDate, double _price) {
        dealString = _dealString;
        endDate = _endDate;
        name = _name;
        desc = _desc;
        price = _price;
        convertDealString(dealString);
    }
    public String getDealString() {
        return dealString;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void convertDealString(String _dealString) {
        deal = new ArrayList<>();
        while (true) {
            if (_dealString.equals("")) break;
            char c = _dealString.charAt(0);
            switch (c) {
                case '{':
                    deal.add(new DealPart(DealPart.PartType.ITEMGROUP_OPEN));
                    break;
                case '}':
                    deal.add(new DealPart(DealPart.PartType.ITEMGROUP_CLOSE));
                    break;
                case '(':
                    deal.add(new DealPart(DealPart.PartType.STATEMENT_OPEN));
                    break;
                case ')':
                    deal.add(new DealPart(DealPart.PartType.STATEMENT_CLOSE));
                    break;
                case '*':
                    deal.add(new DealPart(DealPart.PartType.AND));
                    break;
                case '/':
                    deal.add(new DealPart(DealPart.PartType.OR));
                    break;
                default:
                    String item = _dealString.substring(0, 36);
                    String surcharge = "";
                    if (_dealString.charAt(36) == ',' || _dealString.charAt(36) == '}') deal.add(new DealPart(DealPart.PartType.ITEM, item));
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
                        deal.add(new DealPart(DealPart.PartType.ITEM, item, Double.parseDouble(surcharge)));
                    }
                    break;
            }
            _dealString = _dealString.substring(1);
        }
    }
    public List<DealPart> getDeal() {
        return deal;
    }

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

    public List<Item> getItemGroup(int position) {
        List<Item> itemGroup = new ArrayList<>();
        int count = 0;
        for (DealPart p: deal) {
            if (p.type == DealPart.PartType.ITEMGROUP_OPEN) {
                count++;
            }
            if (p.type == DealPart.PartType.ITEM && count == position) {
                itemGroup.add(p.item);
            }
        }
        return itemGroup;
    }
    public List<String> getAllItemId() {
        List<String> itemIdList = new ArrayList<>();
        for (DealPart p: deal) {
            if (p.type == DealPart.PartType.ITEM) {
                itemIdList.add(p.itemId);
            }
        }
        return itemIdList;
    }
    public Item getItem(String id) {
        for (Item i: itemDirectory) {
            if (i.getId().equals(id)) return i;
        }
        return null;
    }
    public void setLastItemToDealPart(String id) {
        for (DealPart p: deal) {
            if (p.type == DealPart.PartType.ITEM) {
                if (p.itemId.equals(id)) {
                    p.item = itemDirectory.get(itemDirectory.size() - 1);
                }
            }
        }
    }
    public String shortenDeal() {
        String shortened = "";
        int count = 1;
        for (DealPart p: deal) {
            switch (p.type) {
                case STATEMENT_OPEN:
                    shortened += "[";
                    break;
                case STATEMENT_CLOSE:
                    shortened += "]";
                    break;
                case AND:
                    shortened += "*";
                    break;
                case OR:
                    shortened += "/";
                    break;
                case ITEMGROUP_OPEN:
                    shortened += "{" + count + "}";
                    count++;
                    break;
            }
        }
        return shortened;
    }
}
