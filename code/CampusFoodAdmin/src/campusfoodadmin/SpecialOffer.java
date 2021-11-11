/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Josh Brookes
 */
public class SpecialOffer {
    private String offerId;
    private Item item;
    private Date startDate;
    private Date endDate;
    private double offerPrice;
    private boolean toBeDeleted;
    
    public SpecialOffer() {
        offerId = UUID.randomUUID().toString();
        toBeDeleted = false;
    }
    public SpecialOffer(String _offerId, Item _item, Date _startDate, Date _endDate, double _offerPrice) {
        offerId = _offerId;
        item = _item;
        startDate = _startDate;
        endDate = _endDate;
        offerPrice = _offerPrice;
        toBeDeleted = false;
    }
    public String getOfferId() {
        return offerId;
    }
    public String getItemId() {
        return item.getId();
    }
    public Item getItem() {
        return item;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public double getOfferPrice() {
        return offerPrice;
    }
    public boolean getDeleteStatus() {
        return toBeDeleted;
    }
    public void setOfferId(String _offerId) {
        offerId = _offerId;
    }
    public void setItem(Item _item) {
        item = _item;
    }
    public void setStartDate(Date _startDate) {
        startDate = _startDate;
    }
    public void setEndDate(Date _endDate) {
        endDate = _endDate;
    }
    public void setOfferPrice(double _offerPrice) {
        offerPrice = _offerPrice;
    }
    public void setDelete() {
        toBeDeleted = true;
    }
    public boolean isValid() {
        return item != null && startDate != null && endDate != null && offerPrice != 0;
    }
}
