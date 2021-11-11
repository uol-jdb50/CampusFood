/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.DefaultListModel;

/**
 *
 * @author Josh Brookes
 */
public class MenuPreparation extends javax.swing.JPanel {

    public String locationKey;
    public String locationName;
    public String menuKey;
    public boolean newMenu;
    public List<Category> categories = new ArrayList<Category>();
    public List<Item> items = new ArrayList<Item>();
    public List<Item> currentCategoryItems = new ArrayList<Item>();
    public List<String> menuLines = new ArrayList<String>();
    public List<String> originalMenuLines = new ArrayList<String>();
    public int day;
    public int month;
    public int year;
    public Date date;
    public String currentCategoryId;
    SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form MenuPreparation
     */
    public MenuPreparation(String _locationName, String _locationKey, int _day, int _month, int _year) {
        initComponents();
        locationKey = _locationKey;
        locationName = _locationName;
        day = _day;
        month = _month;
        year = _year - 1900;
        newMenu = false;
        date = new Date(year, month, day);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yy");
        String strDate = formatter.format(date);
        lblTitle.setText(CampusFood.CATERER + " | " + locationName + ": " + strDate);
        setCategories();
    }

    public void setCategories() {
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
            String sqlMenu = "SELECT MenuID FROM dbo.DayMenu WHERE LocationID='" + locationKey + "' AND Date='" + formatSql.format(date) + "';";
            ResultSet rsMenu = stmt.executeQuery(sqlMenu);
            if (!rsMenu.isBeforeFirst()) {
                menuKey = UUID.randomUUID().toString();
                newMenu = true;
            } else {
                while (rsMenu.next()) {
                    menuKey = rsMenu.getString("MenuID");
                }
            }
            String sqlLines = "SELECT A.ItemID, A.MenuID FROM dbo.MenuLines A INNER JOIN dbo.DayMenu B ON A.MenuID=B.MenuID WHERE B.LocationID='" + locationKey + "' AND Date='" + formatSql.format(date) + "';";
            ResultSet rsLines = stmt.executeQuery(sqlLines);
            while (rsLines.next()) {
                menuLines.add(rsLines.getString("ItemID"));
                originalMenuLines.add(rsLines.getString("ItemID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultListModel model = new DefaultListModel();
        categories.sort(new CategoryComparator());
        for (Category c : categories) {
            model.addElement(c.getName());
        }
        lstCategories.setModel(model);
    }

    public void refreshItemLists() {
        DefaultListModel modelOut = new DefaultListModel();
        currentCategoryItems.sort(new ItemComparator());
        for (Item i : currentCategoryItems) {
            modelOut.addElement(i.getName());
        }
        lstItemsOut.setModel(modelOut);

        DefaultListModel modelIn = new DefaultListModel();
        for (String i : menuLines) {
            //if (i.getCategoryId().equals(currentCategoryId)) {
            //    modelIn.addElement(i.getName());
            //}
            for (Item c : currentCategoryItems) {
                if (c.getId().equals(i)) {
                    modelIn.addElement(c.getName());
                }
            }
        }
        lstItemsIn.setModel(modelIn);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCategories = new javax.swing.JList<>();
        lblTitle1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstItemsOut = new javax.swing.JList<>();
        lblTitle2 = new javax.swing.JLabel();
        btnAddAll = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstItemsIn = new javax.swing.JList<>();
        lblTitle3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
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

        lstCategories.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lstCategories.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstCategoriesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstCategories);

        lblTitle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle1.setText("Menu Categories");

        jButton1.setText("Import menu from file");
        jButton1.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(lblTitle1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitle1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(136, 136, 136))
        );

        jPanel2.add(jPanel3);

        lstItemsOut.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lstItemsOut.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstItemsOutValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstItemsOut);

        lblTitle2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle2.setText("Items in Category");

        btnAddAll.setText("Add All Items from Category");
        btnAddAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(btnAddAll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddAll)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4);

        lstItemsIn.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lstItemsIn.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstItemsInValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(lstItemsIn);

        lblTitle3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle3.setText("Items Selected in Menu");

        jButton2.setText("Export menu to file");
        jButton2.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTitle3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(28, Short.MAX_VALUE))
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

    private void lstCategoriesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstCategoriesValueChanged
        //Change items in/out to show new category
        lstCategories.setEnabled(false);
        lstItemsIn.setEnabled(false);
        lstItemsOut.setEnabled(false);
        currentCategoryItems.clear();
        String categoryId = categories.get(lstCategories.getSelectedIndex()).getID();
        for (Item i : items) {
            if (i.getCategoryId().equals(categoryId)) {
                currentCategoryItems.add(i);
            }
        }
        currentCategoryId = categoryId;
        refreshItemLists();
        lstCategories.setEnabled(true);
        lstItemsIn.setEnabled(true);
        lstItemsOut.setEnabled(true);
    }//GEN-LAST:event_lstCategoriesValueChanged

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        CampusFood.returnToCalendar();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String sqlUpdate = "";
        if (newMenu == true) {
            sqlUpdate += "INSERT INTO dbo.DayMenu (MenuID, LocationID, Date) VALUES ('" + menuKey + "', '" + locationKey + "', '" + formatSql.format(date) + "');";
        }
        for (String i : originalMenuLines) {
            if (!menuLines.contains(i)) {
                sqlUpdate += "DELETE FROM dbo.MenuLines WHERE MenuID='" + menuKey + "' AND ItemID='" + i + "';";
            }
        }
        for (String i : menuLines) {
            if (!originalMenuLines.contains(i)) {
                sqlUpdate += "INSERT INTO dbo.MenuLines VALUES ('" + menuKey + "', '" + i + "');";
            }
        }
        try (Statement stmtUpdate = CampusFood.connection.createStatement();) {
            stmtUpdate.executeUpdate(sqlUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        CampusFood.returnToCalendar();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void lstItemsOutValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstItemsOutValueChanged
        //Move item to in list
        lstCategories.setEnabled(false);
        lstItemsIn.setEnabled(false);
        lstItemsOut.setEnabled(false);
        if (!evt.getValueIsAdjusting()) {
            if (lstItemsOut.getSelectedIndex() != -1) {
                boolean found = false;
                for (String i : menuLines) {
                    if (i.equals(currentCategoryItems.get(lstItemsOut.getSelectedIndex()).getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    menuLines.add(currentCategoryItems.get(lstItemsOut.getSelectedIndex()).getId());
                }
                //currentCategoryItems.remove(lstItemsOut.getSelectedIndex());
                refreshItemLists();
            }
        }
        lstCategories.setEnabled(true);
        lstItemsIn.setEnabled(true);
        lstItemsOut.setEnabled(true);
    }//GEN-LAST:event_lstItemsOutValueChanged

    private void lstItemsInValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstItemsInValueChanged
        //Move item to out list
        //currentCategoryItems.add(menuLines.get(lstItemsOut.getSelectedIndex()));
        lstCategories.setEnabled(false);
        lstItemsIn.setEnabled(false);
        lstItemsOut.setEnabled(false);
        if (!evt.getValueIsAdjusting()) {
            if (lstItemsIn.getSelectedIndex() != -1) {
                //if (menuLines.get(i).getName().equals(itemName)) {
                //    currentCategoryItems.add(menuLines.get(i));
                menuLines.remove(currentCategoryItems.get(lstItemsIn.getSelectedIndex()).getId());
                //}
                refreshItemLists();
            }
        }
        lstCategories.setEnabled(true);
        lstItemsIn.setEnabled(true);
        lstItemsOut.setEnabled(true);
    }//GEN-LAST:event_lstItemsInValueChanged

    private void btnAddAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAllActionPerformed
        for (Item i: currentCategoryItems) {
            if (!menuLines.contains(i.getId())) {
                menuLines.add(i.getId());
            }
        }
        refreshItemLists();
    }//GEN-LAST:event_btnAddAllActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAll;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JLabel lblTitle2;
    private javax.swing.JLabel lblTitle3;
    private javax.swing.JList<String> lstCategories;
    private javax.swing.JList<String> lstItemsIn;
    private javax.swing.JList<String> lstItemsOut;
    // End of variables declaration//GEN-END:variables
}
