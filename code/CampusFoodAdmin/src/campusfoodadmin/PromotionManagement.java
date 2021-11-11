/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Josh Brookes
 */
public class PromotionManagement extends javax.swing.JPanel {

    public String locationName;
    public String locationKey;
    public List<Category> categories = new ArrayList<Category>();
    public List<Item> items = new ArrayList<Item>();
    public List<Item> currentDealCategoryItems = new ArrayList<Item>();
    public List<Item> currentOfferCategoryItems = new ArrayList<Item>();
    public List<String> currentDeal = new ArrayList<String>();
    public List<SpecialOffer> offers = new ArrayList<SpecialOffer>();
    public List<Deal> deals = new ArrayList<Deal>();
    public String currentDealCategory;
    public String currentOfferCategory;
    public SpecialOffer newOffer;
    public Deal newDeal;
    SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
    public boolean offerEdit = false;
    public boolean dealEdit = false;
    public int offerEditIndex;
    public int dealEditIndex;

    /**
     * Creates new form PromotionManagement
     */
    public PromotionManagement(String _locationName, String _locationKey) {
        initComponents();
        locationName = _locationName;
        locationKey = _locationKey;
        getInfo();
    }

    public void getInfo() {
        try {
            Statement stmt = CampusFood.connection.createStatement();

            String sql = "SELECT * FROM dbo.MenuCategories WHERE LocationID='" + locationKey + "';";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                categories.add(new Category(rs.getString("CategoryID"), rs.getString("CategoryName"), rs.getInt("CategoryOrder")));
            }
            categories.sort(new CategoryComparator());
            String sqlItems = "";
            for (Category c : categories) {
                sqlItems = "SELECT * FROM dbo.Items WHERE CategoryID='" + c.getID() + "';";
                ResultSet rsItems = stmt.executeQuery(sqlItems);
                while (rsItems.next()) {
                    items.add(new Item(rsItems.getString("ItemID"), rsItems.getString("Name"), rsItems.getString("Description"), rsItems.getString("CategoryID"), rsItems.getString("LocationID"), rsItems.getDouble("Price"), rsItems.getInt("AllergenMatrix"), rsItems.getInt("ListOrder")));
                }
            }

            String sqlOffer = "SELECT * FROM dbo.Offers WHERE LocationID='" + locationKey + "';";
            ResultSet rsOffer = stmt.executeQuery(sqlOffer);

            while (rsOffer.next()) {
                offers.add(new SpecialOffer(rsOffer.getString("OfferID"), findItemById(rsOffer.getString("ItemID")), rsOffer.getDate("StartDate"), rsOffer.getDate("EndDate"), rsOffer.getDouble("OfferPrice")));
            }

            String sqlDeal = "SELECT * FROM dbo.Deals WHERE LocationID='" + locationKey + "';";
            ResultSet rsDeal = stmt.executeQuery(sqlDeal);
            while (rsDeal.next()) {
                deals.add(new Deal(rsDeal.getString("DealID"), rsDeal.getString("Name"), rsDeal.getDate("StartDate"), rsDeal.getDate("EndDate"), rsDeal.getDouble("Price"), rsDeal.getString("DealString")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshCategoryLists();
        refreshPromotionList();
    }

    public void refreshPromotionList() {
        DefaultListModel model = new DefaultListModel();
        for (SpecialOffer o : offers) {
            if (!o.getDeleteStatus()) {
                model.addElement("O | " + o.getItem().getName() + " | " + o.getStartDate().toString() + " | " + o.getOfferId());
            }
        }
        for (Deal d : deals) {
            if (!d.getDeleteStatus()) {
                model.addElement("D | " + d.getName() + " | " + d.getId());
            }
        }
        lstPromotions.setModel(model);
    }

    public void refreshCategoryLists() {
        DefaultListModel model = new DefaultListModel();
        categories.sort(new CategoryComparator());
        for (Category c : categories) {
            model.addElement(c.getName());
        }
        lstDealCategory.setModel(model);
        lstOfferCategory.setModel(model);
    }

    public void refreshItemLists(int type) {
        if (type == 1) { //Deal
            DefaultListModel model = new DefaultListModel();
            currentDealCategoryItems.sort(new ItemComparator());
            for (Item i : currentDealCategoryItems) {
                model.addElement(i.getName());
            }
            lstDealItem.setModel(model);
        } else if (type == 2) { //Offer
            DefaultListModel model = new DefaultListModel();
            currentOfferCategoryItems.sort(new ItemComparator());
            for (Item i : currentOfferCategoryItems) {
                model.addElement(i.getName());
            }
            lstOfferItem.setModel(model);
        }
    }

    public void refreshDealList() {
        DefaultListModel model = new DefaultListModel();
        for (DealPart p : newDeal.getDealStringList()) {
            if (p.type == DealPart.PartType.ITEM) {
                model.addElement(findItemById(p.itemId).getName());
            } else {
                model.addElement(p.type.toString());
            }
        }
        lstCurrentDeal.setModel(model);
    }

    public void checkOfferTypeBox() {
        if (selType.getSelectedIndex() == 0) {
            txtPercent.setEnabled(false);
            txtNewPrice.setEnabled(true);
        } else {
            txtPercent.setEnabled(true);
            txtNewPrice.setEnabled(false);
        }
    }

    public Item findItemById(String itemId) {
        for (Item i : items) {
            if (i.getId().equals(itemId)) {
                return i;
            }
        }
        return null;
    }

    public String findCategoryNameById(String id) {
        for (Category c : categories) {
            if (c.getID().equals(id)) {
                return c.getName();
            }
        }
        return null;
    }

    public void setEditOffer() {
        lstOfferCategory.setSelectedValue(findCategoryNameById(newOffer.getItem().getCategoryId()), true);
        lstOfferItem.setSelectedValue(newOffer.getItem().getName(), true);
        txtNewPrice.setText(String.format("%.2f", newOffer.getOfferPrice()));
        txtOfferStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newOffer.getStartDate()));
        txtOfferEndDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newOffer.getEndDate()));
    }

    public void setEditDeal() {
        refreshDealList();
        txtDealPrice.setText(String.format("%.2f", newDeal.getPrice()));
        txtDealName.setText(newDeal.getName());
        txtDealStartDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDeal.getStartDate()));
        txtDealEndDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(newDeal.getEndDate()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        tabType = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        btnNewDeal = new javax.swing.JButton();
        btnNewOffer = new javax.swing.JButton();
        btnNewAdvert = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        lstPromotions = new javax.swing.JList<>();
        jPanel16 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstDealToolbox = new javax.swing.JList<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstDealCategory = new javax.swing.JList<>();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        lstDealItem = new javax.swing.JList<>();
        jLabel17 = new javax.swing.JLabel();
        txtItemSurcharge = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        btnAddComponent = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        lstCurrentDeal = new javax.swing.JList<>();
        jLabel13 = new javax.swing.JLabel();
        btnDealDeletePart = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnSetDeal = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtDealPrice = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtDealName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtDealStartDate = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDealEndDate = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstOfferCategory = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstOfferItem = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblUsualPrice = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        selType = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtPercent = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNewPrice = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtOfferStartDate = new javax.swing.JTextField();
        txtOfferEndDate = new javax.swing.JTextField();
        btnSaveOffer = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(1, 3));

        btnNewDeal.setText("Create new deal");
        btnNewDeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewDealActionPerformed(evt);
            }
        });

        btnNewOffer.setText("Create new special offer");
        btnNewOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOfferActionPerformed(evt);
            }
        });

        btnNewAdvert.setText("Create new advert");
        btnNewAdvert.setEnabled(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewDeal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNewOffer, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(btnNewAdvert, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNewDeal)
                .addGap(18, 18, 18)
                .addComponent(btnNewOffer)
                .addGap(18, 18, 18)
                .addComponent(btnNewAdvert)
                .addContainerGap(396, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel14);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Promotions");

        jScrollPane7.setViewportView(lstPromotions);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.add(jPanel15);

        btnEdit.setText("Edit");
        btnEdit.setToolTipText("");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEdit)
                .addGap(18, 18, 18)
                .addComponent(btnDelete)
                .addContainerGap(440, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel16);

        tabType.addTab("Current Promotions", jPanel13);

        jPanel3.setEnabled(false);
        jPanel3.setLayout(new java.awt.GridLayout(1, 3));

        lstDealToolbox.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Statement Group Open", "Statement Group Close", "Item Group Open", "Item Group Close", "AND", "OR" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstDealToolbox.setEnabled(false);
        jScrollPane3.setViewportView(lstDealToolbox);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Toolbox");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Menu Categories");

        lstDealCategory.setEnabled(false);
        lstDealCategory.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstDealCategoryValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(lstDealCategory);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Items in category");

        lstDealItem.setEnabled(false);
        lstDealItem.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstDealItemValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(lstDealItem);

        jLabel17.setText("Item Surcharge: £");

        txtItemSurcharge.setText("0.00");
        txtItemSurcharge.setEnabled(false);

        btnAddItem.setText("Add item");
        btnAddItem.setEnabled(false);
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        btnAddComponent.setText("Add component");
        btnAddComponent.setEnabled(false);
        btnAddComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddComponentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtItemSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddComponent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddComponent)
                .addGap(10, 10, 10)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtItemSurcharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddItem)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel10);

        lstCurrentDeal.setEnabled(false);
        jScrollPane6.setViewportView(lstCurrentDeal);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Current Deal");

        btnDealDeletePart.setText("Delete Selected");
        btnDealDeletePart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDealDeletePartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDealDeletePart)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDealDeletePart)
                .addContainerGap())
        );

        jPanel3.add(jPanel11);

        btnSetDeal.setText("Set Deal");
        btnSetDeal.setEnabled(false);
        btnSetDeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetDealActionPerformed(evt);
            }
        });

        jLabel14.setText("Deal Price (exc. surcharges): £");

        txtDealPrice.setText("0.00");
        txtDealPrice.setEnabled(false);
        txtDealPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDealPriceActionPerformed(evt);
            }
        });

        jLabel15.setText("Deal Name:");

        txtDealName.setText("Deal");
        txtDealName.setEnabled(false);
        txtDealName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDealNameActionPerformed(evt);
            }
        });

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Deal Properties");

        jLabel18.setText("Start Date:");

        txtDealStartDate.setText("DD/MM/YYYY");
        txtDealStartDate.setEnabled(false);
        txtDealStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDealStartDateActionPerformed(evt);
            }
        });

        jLabel19.setText("End Date:");

        txtDealEndDate.setText("DD/MM/YYYY");
        txtDealEndDate.setEnabled(false);
        txtDealEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDealEndDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSetDeal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDealPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDealName))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDealEndDate)
                            .addComponent(txtDealStartDate))))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtDealName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtDealPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtDealStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtDealEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSetDeal)
                .addContainerGap(310, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel12);

        tabType.addTab("Deals", jPanel3);

        jPanel4.setEnabled(false);
        jPanel4.setLayout(new java.awt.GridLayout(1, 3));

        lstOfferCategory.setEnabled(false);
        lstOfferCategory.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstOfferCategoryValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstOfferCategory);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Menu Categories");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel7);

        lstOfferItem.setEnabled(false);
        lstOfferItem.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstOfferItemValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstOfferItem);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Items in Category");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel8);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Properties");

        lblUsualPrice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUsualPrice.setText("Usual Price: £0.00");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Offer Type:");

        selType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Special Price", "Percent Reduction" }));
        selType.setEnabled(false);
        selType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selTypeActionPerformed(evt);
            }
        });

        jLabel6.setText("Percent Reduction (%):");

        txtPercent.setText("20");
        txtPercent.setEnabled(false);
        txtPercent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPercentActionPerformed(evt);
            }
        });

        jLabel7.setText("New Price: £");

        txtNewPrice.setText("0.00");
        txtNewPrice.setEnabled(false);
        txtNewPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNewPriceActionPerformed(evt);
            }
        });

        jLabel8.setText("Start Date:");

        jLabel9.setText("End Date:");

        txtOfferStartDate.setText("DD/MM/YYYY");
        txtOfferStartDate.setEnabled(false);
        txtOfferStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOfferStartDateActionPerformed(evt);
            }
        });

        txtOfferEndDate.setText("DD/MM/YYYY");
        txtOfferEndDate.setEnabled(false);
        txtOfferEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOfferEndDateActionPerformed(evt);
            }
        });

        btnSaveOffer.setText("Set Special Offer");
        btnSaveOffer.setEnabled(false);
        btnSaveOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveOfferActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUsualPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(8, 8, 8)
                                .addComponent(txtNewPrice))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtOfferEndDate)
                                    .addComponent(txtOfferStartDate))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnSaveOffer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(lblUsualPrice)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(selType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNewPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtOfferStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtOfferEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnSaveOffer)
                .addContainerGap(221, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel9);

        tabType.addTab("Special Offers", jPanel4);

        jPanel5.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 871, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        tabType.addTab("Adverts", jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabType)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabType)
                .addContainerGap())
        );

        tabType.getAccessibleContext().setAccessibleName("Deals");

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel6.setLayout(new java.awt.GridLayout(1, 3, 20, 0));

        jPanel1.setMaximumSize(new java.awt.Dimension(200, 16));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 16));

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCancel))
        );

        jPanel6.add(jPanel1);

        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("University of Leicester");
        jPanel6.add(lblTitle);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel6.add(btnSave);

        add(jPanel6, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        CampusFood.viewLocations();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String sqlUpdate = "";
        String sqlCheck = "";
        for (SpecialOffer o : offers) {
            sqlCheck = "SELECT * FROM Offers WHERE OfferID='" + o.getOfferId() + "'";
            try (Statement stmtCheck = CampusFood.connection.createStatement();) {
                ResultSet rs = stmtCheck.executeQuery(sqlCheck);
                if (!rs.isBeforeFirst()) {
                    if (!o.getDeleteStatus()) {
                        sqlUpdate += "INSERT INTO Offers VALUES ('" + o.getOfferId() + "', '" + o.getItemId() + "', '" + formatSql.format(o.getStartDate()) + "', '" + formatSql.format(o.getEndDate()) + "', " + o.getOfferPrice() + ", '" + locationKey + "');\n";
                    }
                }
                while (rs.next()) {
                    if (!o.getDeleteStatus()) {
                        sqlUpdate += "UPDATE Offers SET ItemID='" + o.getItemId() + "', StartDate='" + formatSql.format(o.getStartDate()) + "', EndDate='" + formatSql.format(o.getEndDate()) + "', OfferPrice=" + o.getOfferPrice() + " WHERE OfferID='" + o.getOfferId() + "';\n";
                    } else {
                        sqlUpdate += "DELETE FROM Offers WHERE OfferID='" + o.getOfferId() + "';\n";
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        for (Deal d : deals) {
            sqlCheck = "SELECT * FROM Deals WHERE DealID='" + d.getId() + "'";
            try (Statement stmtCheckDeal = CampusFood.connection.createStatement();) {
                ResultSet rsCheck = stmtCheckDeal.executeQuery(sqlCheck);
                if (!rsCheck.isBeforeFirst()) {
                    if (!d.getDeleteStatus()) {
                        sqlUpdate += "INSERT INTO Deals VALUES ('" + d.getId() + "', '" + locationKey + "', '" + d.getDealString() + "', '" + d.getName() + "', '', '" + formatSql.format(d.getStartDate()) + "', '" + formatSql.format(d.getEndDate()) + "', " + d.getPrice() + ");\n";
                    }
                }
                while (rsCheck.next()) {
                    if (!d.getDeleteStatus()) {
                        sqlUpdate += "UPDATE Deals SET DealString='" + d.getDealString() + "', Name='" + d.getName() + "', Description='', StartDate='" + formatSql.format(d.getStartDate()) + "', EndDate='" + formatSql.format(d.getEndDate()) + "', Price=" + d.getPrice() + "WHERE DealID='" + d.getId() + "';\n";
                    } else {
                        sqlUpdate += "DELETE FROM Deals WHERE DealID='" + d.getId() + "';\n";
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try (Statement stmtUpdate = CampusFood.connection.createStatement();) {
            stmtUpdate.executeUpdate(sqlUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CampusFood.viewLocations();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void lstDealCategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstDealCategoryValueChanged
        currentDealCategoryItems.clear();
        String categoryId = categories.get(lstDealCategory.getSelectedIndex()).getID();
        for (Item i : items) {
            if (i.getCategoryId().equals(categoryId)) {
                currentDealCategoryItems.add(i);
            }
        }
        currentDealCategory = categoryId;
        refreshItemLists(1);
    }//GEN-LAST:event_lstDealCategoryValueChanged

    private void lstOfferCategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstOfferCategoryValueChanged
        currentOfferCategoryItems.clear();
        String categoryId = categories.get(lstOfferCategory.getSelectedIndex()).getID();
        for (Item i : items) {
            if (i.getCategoryId().equals(categoryId)) {
                currentOfferCategoryItems.add(i);
            }
        }
        currentOfferCategory = categoryId;
        refreshItemLists(2);
    }//GEN-LAST:event_lstOfferCategoryValueChanged

    private void btnNewDealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewDealActionPerformed
        lstDealToolbox.setEnabled(true);
        lstDealCategory.setEnabled(true);
        lstDealItem.setEnabled(true);
        lstCurrentDeal.setEnabled(true);
        txtDealName.setEnabled(true);
        txtDealPrice.setEnabled(true);
        txtDealStartDate.setEnabled(true);
        txtDealEndDate.setEnabled(true);
        btnSetDeal.setEnabled(true);
        btnAddComponent.setEnabled(true);
        newDeal = new Deal();
        tabType.setSelectedIndex(1);
    }//GEN-LAST:event_btnNewDealActionPerformed

    private void btnNewOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOfferActionPerformed
        lstOfferCategory.setEnabled(true);
        lstOfferItem.setEnabled(true);
        selType.setEnabled(true);
        txtOfferStartDate.setEnabled(true);
        txtOfferEndDate.setEnabled(true);
        btnSaveOffer.setEnabled(true);
        newOffer = new SpecialOffer();
        tabType.setSelectedIndex(2);
        checkOfferTypeBox();
    }//GEN-LAST:event_btnNewOfferActionPerformed

    private void txtPercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentActionPerformed
        double percent = 0;
        try {
            percent = Double.parseDouble(txtPercent.getText());
        } catch (NumberFormatException e) {
            return;
        }
        if (percent >= 1 && percent <= 100) {
            double price = Math.round(100 * newOffer.getItem().getPrice() * (1 - (percent / 100.0))) / 100.0;
            newOffer.setOfferPrice(price);
            txtNewPrice.setText(String.format("%.2f", price));
        }
    }//GEN-LAST:event_txtPercentActionPerformed

    private void txtNewPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewPriceActionPerformed
        double newprice = 0;
        try {
            if (Double.parseDouble(txtNewPrice.getText()) >= 0) {
                newprice = Double.parseDouble(txtNewPrice.getText());
            }
        } catch (NumberFormatException e) {
            return;
        }
        if (newprice > 0) {
            newOffer.setOfferPrice(newprice);
        }
    }//GEN-LAST:event_txtNewPriceActionPerformed

    private void lstOfferItemValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstOfferItemValueChanged
        newOffer.setItem(currentOfferCategoryItems.get(lstOfferItem.getSelectedIndex()));
        lblUsualPrice.setText("Usual Price: £" + String.format("%.2f", newOffer.getItem().getPrice()));
    }//GEN-LAST:event_lstOfferItemValueChanged

    private void selTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selTypeActionPerformed
        checkOfferTypeBox();
    }//GEN-LAST:event_selTypeActionPerformed

    private void txtOfferStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOfferStartDateActionPerformed
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(txtOfferStartDate.getText());
        } catch (ParseException e) {
            return;
        }
        newOffer.setStartDate(date);
    }//GEN-LAST:event_txtOfferStartDateActionPerformed

    private void txtOfferEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOfferEndDateActionPerformed
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(txtOfferEndDate.getText());
        } catch (ParseException e) {
            return;
        }
        newOffer.setEndDate(date);
    }//GEN-LAST:event_txtOfferEndDateActionPerformed

    private void btnSaveOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveOfferActionPerformed
        if (selType.getSelectedIndex() == 0) {
            txtNewPriceActionPerformed(evt);
        } else {
            txtPercentActionPerformed(evt);
        }
        txtOfferStartDateActionPerformed(evt);
        txtOfferEndDateActionPerformed(evt);
        if (newOffer.isValid()) {
            if (offerEdit) {
                offers.set(offerEditIndex, newOffer);
            } else {
                offers.add(newOffer);
            }
            lstOfferCategory.setEnabled(false);
            lstOfferItem.setEnabled(false);
            selType.setEnabled(false);
            txtOfferStartDate.setEnabled(false);
            txtOfferEndDate.setEnabled(false);
            btnSaveOffer.setEnabled(false);
            txtPercent.setEnabled(false);
            txtNewPrice.setEnabled(false);
            txtPercent.setText("0");
            txtNewPrice.setText("0.00");
            txtOfferStartDate.setText("DD/MM/YYYY");
            txtOfferEndDate.setText("DD/MM/YYYY");
            offerEdit = false;
            refreshPromotionList();
            tabType.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Offer format incorrect. Check offer properties.");
        }
    }//GEN-LAST:event_btnSaveOfferActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int index = 0;
        if (lstPromotions.getSelectedValue().charAt(0) == 'O') {
            for (SpecialOffer o : offers) {
                if (lstPromotions.getSelectedValue().substring(lstPromotions.getSelectedValue().length() - 36).equals(o.getOfferId())) {
                    newOffer = o;
                    offerEdit = true;
                    offerEditIndex = index;
                    lstOfferCategory.setEnabled(true);
                    lstOfferItem.setEnabled(true);
                    selType.setEnabled(true);
                    txtOfferStartDate.setEnabled(true);
                    txtOfferEndDate.setEnabled(true);
                    btnSaveOffer.setEnabled(true);
                    tabType.setSelectedIndex(2);
                    checkOfferTypeBox();
                    setEditOffer();
                } else {
                    index++;
                }
            }
        } else if (lstPromotions.getSelectedValue().charAt(0) == 'D') {
            for (Deal d : deals) {
                if (lstPromotions.getSelectedValue().substring(lstPromotions.getSelectedValue().length() - 36).equals(d.getId())) {
                    newDeal = d;
                    dealEdit = true;
                    dealEditIndex = index;
                    lstDealToolbox.setEnabled(true);
                    lstDealCategory.setEnabled(true);
                    lstDealItem.setEnabled(true);
                    lstCurrentDeal.setEnabled(true);
                    txtDealName.setEnabled(true);
                    txtDealPrice.setEnabled(true);
                    txtDealStartDate.setEnabled(true);
                    txtDealEndDate.setEnabled(true);
                    btnSetDeal.setEnabled(true);
                    btnAddComponent.setEnabled(true);
                    tabType.setSelectedIndex(1);
                    setEditDeal();
                } else {
                    index++;
                }
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (lstPromotions.getSelectedValue().charAt(0) == 'O') {
            for (SpecialOffer o : offers) {
                if (lstPromotions.getSelectedValue().substring(lstPromotions.getSelectedValue().length() - 36).equals(o.getOfferId())) {
                    o.setDelete();
                    refreshPromotionList();
                }
            }
        } else if (lstPromotions.getSelectedValue().charAt(0) == 'D') {
            for (Deal d : deals) {
                if (lstPromotions.getSelectedValue().substring(lstPromotions.getSelectedValue().length() - 36).equals(d.getId())) {
                    d.setDelete();
                    refreshPromotionList();
                }
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddComponentActionPerformed
        switch (lstDealToolbox.getSelectedIndex()) {
            case 0:
                newDeal.addPartToDeal(DealPart.PartType.STATEMENT_OPEN);
                break;
            case 1:
                newDeal.addPartToDeal(DealPart.PartType.STATEMENT_CLOSE);
                break;
            case 2:
                newDeal.addPartToDeal(DealPart.PartType.ITEMGROUP_OPEN);
                break;
            case 3:
                newDeal.addPartToDeal(DealPart.PartType.ITEMGROUP_CLOSE);
                break;
            case 4:
                newDeal.addPartToDeal(DealPart.PartType.AND);
                break;
            case 5:
                newDeal.addPartToDeal(DealPart.PartType.OR);
                break;
            case -1:
                break;
        }
        refreshDealList();
    }//GEN-LAST:event_btnAddComponentActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        try {
            newDeal.addItemToDeal(currentDealCategoryItems.get(lstDealItem.getSelectedIndex()).getId(), Double.parseDouble(txtItemSurcharge.getText()));
            refreshDealList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Surcharge format incorrect.");
        }
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void lstDealItemValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstDealItemValueChanged
        btnAddItem.setEnabled(true);
        txtItemSurcharge.setEnabled(true);
    }//GEN-LAST:event_lstDealItemValueChanged

    private void btnSetDealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDealActionPerformed
        txtDealNameActionPerformed(evt);
        txtDealPriceActionPerformed(evt);
        txtDealStartDateActionPerformed(evt);
        txtDealEndDateActionPerformed(evt);
        if (newDeal.isValid()) {
            if (dealEdit) {
                deals.set(dealEditIndex, newDeal);
            } else {
                deals.add(newDeal);
            }
            lstDealCategory.setEnabled(false);
            lstDealItem.setEnabled(false);
            txtDealStartDate.setEnabled(false);
            txtDealEndDate.setEnabled(false);
            btnSetDeal.setEnabled(false);
            txtDealPrice.setEnabled(false);
            lstDealToolbox.setEnabled(false);
            btnAddComponent.setEnabled(false);
            txtItemSurcharge.setEnabled(false);
            btnAddItem.setEnabled(false);
            lstCurrentDeal.setEnabled(false);
            lstCurrentDeal.setModel(new DefaultListModel());
            txtDealName.setText("Deal");
            txtDealPrice.setText("0.00");
            txtDealStartDate.setText("DD/MM/YYYY");
            txtDealEndDate.setText("DD/MM/YYYY");
            txtDealName.setEnabled(false);
            offerEdit = false;
            refreshPromotionList();
            tabType.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Deal format incorrect. Check deal properties.");
        }
    }//GEN-LAST:event_btnSetDealActionPerformed

    private void txtDealNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDealNameActionPerformed
        if (!txtDealName.getText().equals("")) {
            newDeal.setName(txtDealName.getText());
        }
    }//GEN-LAST:event_txtDealNameActionPerformed

    private void txtDealPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDealPriceActionPerformed
        double newprice = 0;
        try {
            if (Double.parseDouble(txtDealPrice.getText()) >= 0) {
                newprice = Double.parseDouble(txtDealPrice.getText());
            }
        } catch (NumberFormatException e) {
            return;
        }
        if (newprice > 0) {
            newDeal.setPrice(newprice);
        }
    }//GEN-LAST:event_txtDealPriceActionPerformed

    private void txtDealStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDealStartDateActionPerformed
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(txtDealStartDate.getText());
        } catch (ParseException e) {
            return;
        }
        newDeal.setStartDate(date);
    }//GEN-LAST:event_txtDealStartDateActionPerformed

    private void txtDealEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDealEndDateActionPerformed
        Date date;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(txtDealEndDate.getText());
        } catch (ParseException e) {
            return;
        }
        newDeal.setEndDate(date);
    }//GEN-LAST:event_txtDealEndDateActionPerformed

    private void btnDealDeletePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDealDeletePartActionPerformed
        if (lstCurrentDeal.getSelectedIndex() != -1) {
            newDeal.removeFromDeal(lstCurrentDeal.getSelectedIndex());
            refreshDealList();
        }
    }//GEN-LAST:event_btnDealDeletePartActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddComponent;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDealDeletePart;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNewAdvert;
    private javax.swing.JButton btnNewDeal;
    private javax.swing.JButton btnNewOffer;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveOffer;
    private javax.swing.JButton btnSetDeal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsualPrice;
    private javax.swing.JList<String> lstCurrentDeal;
    private javax.swing.JList<String> lstDealCategory;
    private javax.swing.JList<String> lstDealItem;
    private javax.swing.JList<String> lstDealToolbox;
    private javax.swing.JList<String> lstOfferCategory;
    private javax.swing.JList<String> lstOfferItem;
    private javax.swing.JList<String> lstPromotions;
    private javax.swing.JComboBox<String> selType;
    private javax.swing.JTabbedPane tabType;
    private javax.swing.JTextField txtDealEndDate;
    private javax.swing.JTextField txtDealName;
    private javax.swing.JTextField txtDealPrice;
    private javax.swing.JTextField txtDealStartDate;
    private javax.swing.JTextField txtItemSurcharge;
    private javax.swing.JTextField txtNewPrice;
    private javax.swing.JTextField txtOfferEndDate;
    private javax.swing.JTextField txtOfferStartDate;
    private javax.swing.JTextField txtPercent;
    // End of variables declaration//GEN-END:variables
}
