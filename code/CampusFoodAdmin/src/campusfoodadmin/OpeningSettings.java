/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.swing.JOptionPane;

/**
 *
 * @author Josh Brookes
 */
public class OpeningSettings extends javax.swing.JPanel {

    /**
     * Creates new form OpeningSettings
     *
     * @param day
     * @param month
     * @param year
     */
    String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    boolean defaultMode;
    String locationName;
    String locationKey;
    int day;
    int month;
    int year;
    Date timeOpen;
    Date timeClosed;
    int slideOpenValue;
    int slideCloseValue;
    int[][] defaultTimes = new int[7][3];//Second dimension: 0-open time 1-close time 2-opened
    boolean open;
    boolean specificEdit;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");

    public OpeningSettings(int _day, int _month, int _year, String _locationName, String _locationKey) {
        initComponents();
        locationName = _locationName;
        locationKey = _locationKey;
        day = _day;
        month = _month;
        year = _year;
        open = true;
        defaultMode = false;
        collectSpecificInfo();
        setInfo(false);
    }

    public OpeningSettings(String _locationName) {
        initComponents();
        locationName = _locationName;
        lblTitle.setText(CampusFood.CATERER + " | " + locationName);
        collectDefaultsInfo();
        defaultMode = true;
        setInfo(true);
    }

    public void collectDefaultsInfo() {
        try {
            Statement stmt = CampusFood.connection.createStatement();
            String sql = "SELECT A.LocationID, B.LocationID, DayNumber, IsOpen, OpenTime, CloseTime FROM dbo.LocationTimes A INNER JOIN Location B ON A.LocationID = B.LocationID WHERE B.Name = '" + locationName + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                locationKey = rs.getString("LocationID");
                defaultTimes[rs.getInt("DayNumber")][0] = rs.getInt("OpenTime");
                defaultTimes[rs.getInt("DayNumber")][1] = rs.getInt("CloseTime");
                defaultTimes[rs.getInt("DayNumber")][2] = (rs.getBoolean("IsOpen")) ? 1 : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void collectSpecificInfo() {
        try {
            Statement stmt = CampusFood.connection.createStatement();
            String sql = "SELECT MenuID, LocationID, Date, IsOpen, OpenTime, CloseTime FROM dbo.DayMenu WHERE LocationID='" + locationKey + "'";
            ResultSet rs = stmt.executeQuery(sql);
            
            if (!rs.isBeforeFirst()) specificEdit = false;
            else {
                specificEdit = true;
                while (rs.next()) {
                    slideOpenValue = rs.getInt("OpenTime");
                    slideCloseValue = rs.getInt("CloseTime");
                    open = rs.getBoolean("IsOpen");
                }
            }
        } catch (Exception ex) {  
            ex.printStackTrace();
        }
    }

    public void setInfo(boolean defaults) {
        timeOpen = new Date();
        timeClosed = new Date();
        if (defaults == false) {
            lblSetOpening.setVisible(false);
            selDay.setVisible(false);
            Date date = new Date(year, month, day);
            SimpleDateFormat formatterDate = new SimpleDateFormat("E dd MMMM yy");
            String strDate = formatterDate.format(date);
            lblDate.setText("Open on " + strDate + ":");
            timeOpen = new Date(year, month, day, 0, 0, 0);
            timeClosed = new Date(year, month, day, 0, 0, 0);
            slideOpen.setValue(slideOpenValue);
            slideClose.setValue(slideCloseValue);
        } else {
            lblDate.setText("Open on " + selDay.getItemAt(selDay.getSelectedIndex()) + ":");
            slideOpen.setValue(defaultTimes[selDay.getSelectedIndex()][0]);
            slideClose.setValue(defaultTimes[selDay.getSelectedIndex()][1]);
            btnOpen.setSelected((defaultTimes[selDay.getSelectedIndex()][2] != 0));
            open = (defaultTimes[selDay.getSelectedIndex()][2] != 0);
            timeOpen = new Date(2000, 1, 1, (defaultTimes[selDay.getSelectedIndex()][0] / 2), (defaultTimes[selDay.getSelectedIndex()][0] % 2 == 1 ? 30 : 0), 0);
            timeClosed = new Date(2000, 1, 1, (defaultTimes[selDay.getSelectedIndex()][1] / 2), (defaultTimes[selDay.getSelectedIndex()][1] % 2 == 1 ? 30 : 0), 0);
        }
        updateLabels();
    }

    public void updateLabels() {
        lblOpenTime.setText(formatter.format(timeOpen));
        lblCloseTime.setText(formatter.format(timeClosed));
        btnOpen.setText((open) ? "Open" : "Closed");
        btnOpen.setSelected(open);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        slideOpen = new javax.swing.JSlider();
        btnSave = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblOpenTime = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        slideClose = new javax.swing.JSlider();
        lblCloseTime = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        lblDate = new javax.swing.JLabel();
        btnOpen = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        selDay = new javax.swing.JComboBox<>();
        lblSetOpening = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));

        lblTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("University of Leicester | chi");

        slideOpen.setMajorTickSpacing(1);
        slideOpen.setMaximum(48);
        slideOpen.setSnapToTicks(true);
        slideOpen.setValue(0);
        slideOpen.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideOpenStateChanged(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Open Time:");

        lblOpenTime.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblOpenTime.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblOpenTime.setText("00:00");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Closing Time:");

        slideClose.setMajorTickSpacing(1);
        slideClose.setMaximum(48);
        slideClose.setSnapToTicks(true);
        slideClose.setValue(0);
        slideClose.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideCloseStateChanged(evt);
            }
        });

        lblCloseTime.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblCloseTime.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCloseTime.setText("00:00");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblDate.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblDate.setText("Open on 30th September 2022:");

        btnOpen.setSelected(true);
        btnOpen.setText("Open");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        selDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mondays", "Tuesdays", "Wednesdays", "Thursdays", "Fridays", "Saturdays", "Sundays" }));
        selDay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selDayItemStateChanged(evt);
            }
        });

        lblSetOpening.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblSetOpening.setText("Set opening for:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(33, 33, 33)
                        .addComponent(slideOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblOpenTime))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(slideClose, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCloseTime))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(278, 278, 278)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDate)
                                    .addComponent(lblSetOpening, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(selDay, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSetOpening))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate)
                    .addComponent(btnOpen))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(lblOpenTime)
                    .addComponent(slideOpen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(lblCloseTime)
                    .addComponent(slideClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(338, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void slideOpenStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideOpenStateChanged
        if (!defaultMode) {
            timeOpen = new Date(year, month, day, (slideOpen.getValue() / 2), (slideOpen.getValue() % 2 == 1 ? 30 : 0), 0);
        } else {
            timeOpen = new Date(2000, 1, 1, (slideOpen.getValue() / 2), (slideOpen.getValue() % 2 == 1 ? 30 : 0), 0);
            defaultTimes[selDay.getSelectedIndex()][0] = slideOpen.getValue();
        }
        updateLabels();
    }//GEN-LAST:event_slideOpenStateChanged

    private void slideCloseStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideCloseStateChanged
        if (!defaultMode) {
            timeClosed = new Date(year, month, day, (slideClose.getValue() / 2), (slideClose.getValue() % 2 == 1 ? 30 : 0), 0);
        } else {
            timeClosed = new Date(2000, 1, 1, (slideClose.getValue() / 2), (slideClose.getValue() % 2 == 1 ? 30 : 0), 0);
            defaultTimes[selDay.getSelectedIndex()][1] = slideClose.getValue();
        }
        updateLabels();
    }//GEN-LAST:event_slideCloseStateChanged

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!defaultMode) {
            if (timeOpen.before(timeClosed) || !open) {
                //DayMenu update or insert depending on specificEdit value
                String sqlUpdate = "";
                String date = formatSql.format(new Date(year - 1900, month, day, 0, 0, 0));
                if (specificEdit) {
                    sqlUpdate = "UPDATE dbo.DayMenu (IsOpen, OpenTime, CloseTime) VALUES (" + (open ? 1 : 0) + ", " + slideOpen.getValue() + ", " + slideClose.getValue() + ") WHERE LocationID='" + locationKey + "' AND Date='" + date + "';";
                } else {
                    sqlUpdate = "INSERT INTO dbo.DayMenu VALUES ('" + UUID.randomUUID().toString() + "', '" + locationKey + "', '" + date + "', " + (open ? 1 : 0) + ", " + slideOpen.getValue() + ", " + slideClose.getValue() + ");";
                }
                try {
                    Statement stmt = CampusFood.connection.createStatement();
                    stmt.executeUpdate(sqlUpdate);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                CampusFood.returnToCalendar();
            } else {
                JOptionPane.showMessageDialog(null, "Opening time must be after closing time.", "", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            String errorMessage = "";
            for (int i = 0; i < 7; i++) {
                if (defaultTimes[i][0] >= defaultTimes[i][1] && defaultTimes[i][2] == 1) {
                    errorMessage += days[i] + "'s opening time must be after closing time.\n";
                }
            }
            if (!errorMessage.equals("")) {
                JOptionPane.showMessageDialog(null, errorMessage, "", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    Statement stmt = CampusFood.connection.createStatement();
                    String sql = "";
                    for (int i = 0; i < 7; i++) {
                        if (defaultTimes[i][2] == 1) {
                            sql += "UPDATE dbo.LocationTimes SET IsOpen=" + defaultTimes[i][2] + ", OpenTime=" + defaultTimes[i][0] + ", CloseTime=" + defaultTimes[i][1] + " WHERE LocationTimes.LocationID='" + locationKey + "' AND DayNumber=" + i + ";\n";
                        } else {
                            sql += "UPDATE dbo.LocationTimes SET IsOpen=" + defaultTimes[i][2] + ", OpenTime=18, CloseTime=34 WHERE LocationTimes.LocationID='" + locationKey + "' AND DayNumber=" + i + ";\n";
                        }
                    }
                    stmt.executeUpdate(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CampusFood.returnToCalendar();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        CampusFood.returnToCalendar();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        open = !open;
        if (defaultMode) {
            defaultTimes[selDay.getSelectedIndex()][2] = (open ? 1 : 0);
        }
        updateLabels();
    }//GEN-LAST:event_btnOpenActionPerformed

    private void selDayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selDayItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setInfo(true);
        }
    }//GEN-LAST:event_selDayItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JToggleButton btnOpen;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCloseTime;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblOpenTime;
    private javax.swing.JLabel lblSetOpening;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JComboBox<String> selDay;
    private javax.swing.JSlider slideClose;
    private javax.swing.JSlider slideOpen;
    // End of variables declaration//GEN-END:variables
}
