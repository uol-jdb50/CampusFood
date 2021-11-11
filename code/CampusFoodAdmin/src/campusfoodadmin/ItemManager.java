/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Josh Brookes
 */
public class ItemManager extends javax.swing.JPanel {

    public String locationName;
    public String locationKey;
    public String itemName;
    public String categoryKey;
    public List<Item> items = new ArrayList<Item>();

    /**
     * Creates new form ItemManager
     *
     * @param _locationName
     * @param _locationKey
     * @param _categoryName
     * @param _categoryKey
     */
    public ItemManager(String _locationName, String _locationKey, String _categoryName, String _categoryKey) {
        initComponents();
        locationName = _locationName;
        locationKey = _locationKey;
        itemName = _categoryName;
        categoryKey = _categoryKey;
        lblTitle.setText(CampusFood.CATERER + " | " + locationName + " - " + itemName);
        try {
            Statement stmt = CampusFood.connection.createStatement();
            String sql = "SELECT * FROM dbo.Items WHERE LocationID='" + locationKey + "' AND CategoryID='" + categoryKey + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Item item = new Item(rs.getString("ItemID"), rs.getString("Name"),
                        rs.getString("Description"), rs.getString("CategoryID"),
                        rs.getString("LocationID"), rs.getDouble("Price"), rs.getInt("AllergenMatrix"), rs.getInt("ListOrder"));
                items.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        refreshList();
    }

    public void refreshList() {
        DefaultListModel model = new DefaultListModel();
        items.sort(new ItemComparator());
        for (Item i : items) {
            if (!i.getDeleteStatus()) {
                model.addElement(i.getName());
            }
        }
        lstItems.setModel(model);
    }

    public void checkOrder() {
        if (lstItems.isSelectionEmpty()) {
            btnMoveItemUp.setEnabled(false);
            btnMoveItemDown.setEnabled(false);
            btnEditItem.setEnabled(false);
            btnRemoveItem.setEnabled(false);
            chkCelery.setEnabled(false);
            chkCereal.setEnabled(false);
            chkCrustacean.setEnabled(false);
            chkEgg.setEnabled(false);
            chkFish.setEnabled(false);
            chkLupin.setEnabled(false);
            chkMilk.setEnabled(false);
            chkMollusc.setEnabled(false);
            chkMustard.setEnabled(false);
            chkNut.setEnabled(false);
            chkPeanut.setEnabled(false);
            chkSesame.setEnabled(false);
            chkSoya.setEnabled(false);
            chkSulphur.setEnabled(false);
            chkVegan.setEnabled(false);
            chkVegetarian.setEnabled(false);
            txtDescription.setEnabled(false);
            txtPrice.setEnabled(false);
            btnSaveItem.setEnabled(false);
            btnRevert.setEnabled(false);
        } else {
            btnMoveItemUp.setEnabled(true);
            btnMoveItemDown.setEnabled(true);
            btnEditItem.setEnabled(true);
            btnRemoveItem.setEnabled(true);
            chkCelery.setEnabled(true);
            chkCereal.setEnabled(true);
            chkCrustacean.setEnabled(true);
            chkEgg.setEnabled(true);
            chkFish.setEnabled(true);
            chkLupin.setEnabled(true);
            chkMilk.setEnabled(true);
            chkMollusc.setEnabled(true);
            chkMustard.setEnabled(true);
            chkNut.setEnabled(true);
            chkPeanut.setEnabled(true);
            chkSesame.setEnabled(true);
            chkSoya.setEnabled(true);
            chkSulphur.setEnabled(true);
            chkVegan.setEnabled(true);
            chkVegetarian.setEnabled(true);
            txtDescription.setEnabled(true);
            txtPrice.setEnabled(true);
            btnSaveItem.setEnabled(true);
            btnRevert.setEnabled(true);
            if (lstItems.getSelectedIndex() == 0) {
                btnMoveItemUp.setEnabled(false);
            }
            if (lstItems.getSelectedIndex() == lstItems.getModel().getSize() - 1) {
                btnMoveItemDown.setEnabled(false);
            }
        }
    }

    public void correctOrder() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getDeleteStatus()) {
                items.get(i).setOrder(items.size() + 1);
            } else {
                items.get(i).setOrder(i + 1);
            }
        }
    }

    public void getProperties() {
        if (lstItems.getSelectedIndex() != -1) {
            txtDescription.setText(items.get(lstItems.getSelectedIndex()).getDescription());
            txtPrice.setText(String.format("%.2f", items.get(lstItems.getSelectedIndex()).getPrice()));
            chkCelery.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(0));
            chkCereal.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(1));
            chkCrustacean.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(2));
            chkEgg.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(3));
            chkFish.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(4));
            chkLupin.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(5));
            chkMilk.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(6));
            chkMollusc.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(7));
            chkMustard.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(8));
            chkNut.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(9));
            chkPeanut.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(10));
            chkSesame.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(11));
            chkSoya.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(12));
            chkSulphur.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(13));
            chkVegan.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(14));
            chkVegetarian.setSelected(items.get(lstItems.getSelectedIndex()).getAllergen(15));
        } else {
            txtDescription.setText("");
            txtPrice.setText("0.00");
            chkCelery.setSelected(false);
            chkCereal.setSelected(false);
            chkCrustacean.setSelected(false);
            chkEgg.setSelected(false);
            chkFish.setSelected(false);
            chkLupin.setSelected(false);
            chkMilk.setSelected(false);
            chkMollusc.setSelected(false);
            chkMustard.setSelected(false);
            chkNut.setSelected(false);
            chkPeanut.setSelected(false);
            chkSesame.setSelected(false);
            chkSoya.setSelected(false);
            chkSulphur.setSelected(false);
            chkVegan.setSelected(false);
            chkVegetarian.setSelected(false);
        }
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
        jPanel3 = new javax.swing.JPanel();
        btnAddItem = new javax.swing.JButton();
        btnRemoveItem = new javax.swing.JButton();
        btnMoveItemUp = new javax.swing.JButton();
        btnMoveItemDown = new javax.swing.JButton();
        btnEditItem = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstItems = new javax.swing.JList<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        chkCelery = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        chkCereal = new javax.swing.JCheckBox();
        chkCrustacean = new javax.swing.JCheckBox();
        chkEgg = new javax.swing.JCheckBox();
        chkFish = new javax.swing.JCheckBox();
        chkLupin = new javax.swing.JCheckBox();
        chkMilk = new javax.swing.JCheckBox();
        chkMollusc = new javax.swing.JCheckBox();
        chkMustard = new javax.swing.JCheckBox();
        chkNut = new javax.swing.JCheckBox();
        chkPeanut = new javax.swing.JCheckBox();
        chkSesame = new javax.swing.JCheckBox();
        chkSoya = new javax.swing.JCheckBox();
        chkSulphur = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        chkVegetarian = new javax.swing.JCheckBox();
        chkVegan = new javax.swing.JCheckBox();
        btnSaveItem = new javax.swing.JButton();
        btnRevert = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(1, 3));

        btnAddItem.setText("Add new item");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        btnRemoveItem.setText("Remove item");
        btnRemoveItem.setEnabled(false);
        btnRemoveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveItemActionPerformed(evt);
            }
        });

        btnMoveItemUp.setText("Move item up");
        btnMoveItemUp.setEnabled(false);
        btnMoveItemUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveItemUpActionPerformed(evt);
            }
        });

        btnMoveItemDown.setText("Move item down");
        btnMoveItemDown.setEnabled(false);
        btnMoveItemDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveItemDownActionPerformed(evt);
            }
        });

        btnEditItem.setText("Edit item name");
        btnEditItem.setEnabled(false);
        btnEditItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemoveItem, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(btnMoveItemUp, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(btnMoveItemDown, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(btnEditItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveItemUp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveItemDown)
                .addContainerGap(408, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3);

        lstItems.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lstItems.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstItemsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstItems);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel4);

        jLabel1.setText("Item Description:");

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        txtDescription.setEnabled(false);
        jScrollPane2.setViewportView(txtDescription);

        jLabel2.setText("Item Price: £");

        txtPrice.setText("0.00");
        txtPrice.setEnabled(false);

        chkCelery.setText("Celery");
        chkCelery.setEnabled(false);

        jLabel3.setText("Allergens:");

        chkCereal.setText("Cereals (inc. Gluten)");
        chkCereal.setEnabled(false);

        chkCrustacean.setText("Crustaceans");
        chkCrustacean.setEnabled(false);

        chkEgg.setText("Eggs");
        chkEgg.setEnabled(false);

        chkFish.setText("Fish");
        chkFish.setEnabled(false);

        chkLupin.setText("Lupin");
        chkLupin.setEnabled(false);

        chkMilk.setText("Milk");
        chkMilk.setEnabled(false);

        chkMollusc.setText("Molluscs");
        chkMollusc.setEnabled(false);

        chkMustard.setText("Mustard");
        chkMustard.setEnabled(false);

        chkNut.setText("Nuts");
        chkNut.setEnabled(false);

        chkPeanut.setText("Peanuts");
        chkPeanut.setEnabled(false);

        chkSesame.setText("Sesame Seeds");
        chkSesame.setEnabled(false);

        chkSoya.setText("Soya");
        chkSoya.setEnabled(false);

        chkSulphur.setText("Sulphur Dioxide");
        chkSulphur.setEnabled(false);

        jLabel4.setText("Dietary Requirements:");

        chkVegetarian.setText("Vegetarian");
        chkVegetarian.setEnabled(false);

        chkVegan.setText("Vegan");
        chkVegan.setEnabled(false);

        btnSaveItem.setText("Save item properties");
        btnSaveItem.setEnabled(false);
        btnSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveItemActionPerformed(evt);
            }
        });

        btnRevert.setText("Revert");
        btnRevert.setEnabled(false);
        btnRevert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(btnSaveItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRevert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78))
                            .addComponent(jLabel4)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkCelery)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(chkCereal)
                                    .addComponent(chkCrustacean)
                                    .addComponent(chkEgg)
                                    .addComponent(chkFish)
                                    .addComponent(chkLupin)
                                    .addComponent(chkMilk)
                                    .addComponent(chkVegetarian))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkVegan)
                                    .addComponent(chkMollusc)
                                    .addComponent(chkMustard)
                                    .addComponent(chkNut)
                                    .addComponent(chkPeanut)
                                    .addComponent(chkSesame)
                                    .addComponent(chkSoya)
                                    .addComponent(chkSulphur))))
                        .addGap(0, 23, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkCelery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkCereal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkCrustacean)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkEgg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFish)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkLupin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkMilk))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(chkMollusc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkMustard)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkNut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPeanut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSesame)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSoya)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSulphur)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkVegetarian)
                    .addComponent(chkVegan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSaveItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRevert)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5);

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

    private void lstItemsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstItemsValueChanged
        //btnRemoveItem.setEnabled(true);
        checkOrder();
        getProperties();
    }//GEN-LAST:event_lstItemsValueChanged

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String sqlUpdate = "";
        String sqlCheck = "";
        for (Item i : items) {
            sqlCheck = "SELECT * FROM Items WHERE ItemID='" + i.getId() + "'";
            try (Statement stmtCheck = CampusFood.connection.createStatement();) {
                ResultSet rs = stmtCheck.executeQuery(sqlCheck);
                if (!rs.isBeforeFirst()) {
                    if (!i.getDeleteStatus()) {
                        sqlUpdate += "INSERT INTO Items VALUES ('" + i.getId() + "', '" + i.getName() + "', '" + i.getDescription() + "', '" + i.getCategoryId() + "', '" + i.getLocationId() + "', " + i.getPrice() + ", " + i.getOrder() + ", " + i.getAllergenMatrix() + ");\n";
                    }
                }
                while (rs.next()) {
                    if (!i.getDeleteStatus()) {
                        sqlUpdate += "UPDATE Items SET Name='" + i.getName() + "', Description='" + i.getDescription() + "', Price=" + i.getPrice() + ", ListOrder=" + i.getOrder() + ", AllergenMatrix=" + i.getAllergenMatrix() + " WHERE ItemID='" + i.getId() + "';\n";
                    } else {
                        sqlUpdate += "DELETE FROM Items WHERE ItemID='" + i.getId() + "';\n";
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
        CampusFood.returnToCategories();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        Item item;
        String itemName = "";
        itemName = JOptionPane.showInputDialog(null, "Type the name of the item:", "Add item", 1);
        if (!(itemName == null || (itemName != null && ("".equals(itemName))))) {
            item = new Item(itemName, categoryKey, locationKey, items.size());
            items.add(item);
            refreshList();
        }
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnEditItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditItemActionPerformed
        String itemName = "";
        itemName = JOptionPane.showInputDialog(null, "Type the name of the item:", "Add item", 1);
        if (!(itemName == null || (itemName != null && ("".equals(itemName))))) {
            items.get(lstItems.getSelectedIndex()).setName(itemName);
            refreshList();
        }
    }//GEN-LAST:event_btnEditItemActionPerformed

    private void btnRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveItemActionPerformed
        items.get(lstItems.getSelectedIndex()).setDelete();
        correctOrder();
        refreshList();
    }//GEN-LAST:event_btnRemoveItemActionPerformed

    private void btnMoveItemUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveItemUpActionPerformed
        Collections.swap(items, lstItems.getSelectedIndex(), lstItems.getSelectedIndex() - 1);
        correctOrder();
        refreshList();
    }//GEN-LAST:event_btnMoveItemUpActionPerformed

    private void btnMoveItemDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveItemDownActionPerformed
        Collections.swap(items, lstItems.getSelectedIndex(), lstItems.getSelectedIndex() + 1);
        correctOrder();
        refreshList();
    }//GEN-LAST:event_btnMoveItemDownActionPerformed

    private void btnRevertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertActionPerformed
        getProperties();
    }//GEN-LAST:event_btnRevertActionPerformed

    private void btnSaveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveItemActionPerformed
        try {
            items.get(lstItems.getSelectedIndex()).setPrice(Double.parseDouble(txtPrice.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Price not valid.");
            return;
        }
        items.get(lstItems.getSelectedIndex()).setDescription(txtDescription.getText());
        items.get(lstItems.getSelectedIndex()).setAllergen(chkCelery.isSelected(), 0);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkCereal.isSelected(), 1);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkCrustacean.isSelected(), 2);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkEgg.isSelected(), 3);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkFish.isSelected(), 4);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkLupin.isSelected(), 5);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkMilk.isSelected(), 6);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkMollusc.isSelected(), 7);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkMustard.isSelected(), 8);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkNut.isSelected(), 9);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkPeanut.isSelected(), 10);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkSesame.isSelected(), 11);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkSoya.isSelected(), 12);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkSulphur.isSelected(), 13);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkVegan.isSelected(), 14);
        items.get(lstItems.getSelectedIndex()).setAllergen(chkVegetarian.isSelected(), 15);
    }//GEN-LAST:event_btnSaveItemActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        CampusFood.returnToCategories();
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEditItem;
    private javax.swing.JButton btnMoveItemDown;
    private javax.swing.JButton btnMoveItemUp;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JButton btnRevert;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveItem;
    private javax.swing.JCheckBox chkCelery;
    private javax.swing.JCheckBox chkCereal;
    private javax.swing.JCheckBox chkCrustacean;
    private javax.swing.JCheckBox chkEgg;
    private javax.swing.JCheckBox chkFish;
    private javax.swing.JCheckBox chkLupin;
    private javax.swing.JCheckBox chkMilk;
    private javax.swing.JCheckBox chkMollusc;
    private javax.swing.JCheckBox chkMustard;
    private javax.swing.JCheckBox chkNut;
    private javax.swing.JCheckBox chkPeanut;
    private javax.swing.JCheckBox chkSesame;
    private javax.swing.JCheckBox chkSoya;
    private javax.swing.JCheckBox chkSulphur;
    private javax.swing.JCheckBox chkVegan;
    private javax.swing.JCheckBox chkVegetarian;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JList<String> lstItems;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables
}
