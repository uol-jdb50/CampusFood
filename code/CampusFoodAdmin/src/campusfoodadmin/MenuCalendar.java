/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campusfoodadmin;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Josh Brookes
 */
public class MenuCalendar extends javax.swing.JPanel {

    public JPanel[] panels = new JPanel[37];
    public JLabel[] labels = new JLabel[37];
    public Date[] noticePeriod = new Date[21];
    public boolean[] menuSet = new boolean[21];
    public final String[] MONTHS = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public Calendar calendar;
    public int day;
    public int currentMonth;
    public int month;
    public int currentYear;
    public int year;
    public int selectedDay;
    public int selectedIndex;
    public int startDay;
    public String verify;
    public String locationName;
    public String locationKey;
    SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form Calendar
     */
    public MenuCalendar(String _locationName, String _locationKey) {
        initComponents();
        locationName = _locationName;
        locationKey = _locationKey;
        lblTitle.setText(CampusFood.CATERER + " | " + locationName);
        initPanelListeners();
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        currentMonth = month;
        year = Calendar.getInstance().get(Calendar.YEAR);
        currentYear = year;
        updateMonthLabel();
        updateLabels();
        updateCalendarView();
        updateWarnings();
        getVerify();
    }

    public void initPanelListeners() {
        PanelListener listen = new PanelListener();
        pnlMo1.addMouseListener(listen);
        pnlTu1.addMouseListener(listen);
        pnlWe1.addMouseListener(listen);
        pnlTh1.addMouseListener(listen);
        pnlFr1.addMouseListener(listen);
        pnlSa1.addMouseListener(listen);
        pnlSu1.addMouseListener(listen);
        pnlMo2.addMouseListener(listen);
        pnlTu2.addMouseListener(listen);
        pnlWe2.addMouseListener(listen);
        pnlTh2.addMouseListener(listen);
        pnlFr2.addMouseListener(listen);
        pnlSa2.addMouseListener(listen);
        pnlSu2.addMouseListener(listen);
        pnlMo3.addMouseListener(listen);
        pnlTu3.addMouseListener(listen);
        pnlWe3.addMouseListener(listen);
        pnlTh3.addMouseListener(listen);
        pnlFr3.addMouseListener(listen);
        pnlSa3.addMouseListener(listen);
        pnlSu3.addMouseListener(listen);
        pnlMo4.addMouseListener(listen);
        pnlTu4.addMouseListener(listen);
        pnlWe4.addMouseListener(listen);
        pnlTh4.addMouseListener(listen);
        pnlFr4.addMouseListener(listen);
        pnlSa4.addMouseListener(listen);
        pnlSu4.addMouseListener(listen);
        pnlMo5.addMouseListener(listen);
        pnlTu5.addMouseListener(listen);
        pnlWe5.addMouseListener(listen);
        pnlTh5.addMouseListener(listen);
        pnlFr5.addMouseListener(listen);
        pnlSa5.addMouseListener(listen);
        pnlSu5.addMouseListener(listen);
        pnlMo6.addMouseListener(listen);
        pnlTu6.addMouseListener(listen);

    }

    public void updateMonthLabel() {
        lblMonth.setText(MONTHS[month] + " " + year);
    }
    
    public void getVerify() {
        String sql = "";
        try {
            Statement stmt = CampusFood.connection.createStatement();
            sql = "SELECT Verify FROM Location WHERE LocationID='" + locationKey + "';";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                verify = rs.getString("Verify");
            }
            lblVerifyNum.setText("Check In No.:\n" + verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLabels() {
        labels[0] = lblMo1;
        labels[1] = lblTu1;
        labels[2] = lblWe1;
        labels[3] = lblTh1;
        labels[4] = lblFr1;
        labels[5] = lblSa1;
        labels[6] = lblSu1;
        labels[7] = lblMo2;
        labels[8] = lblTu2;
        labels[9] = lblWe2;
        labels[10] = lblTh2;
        labels[11] = lblFr2;
        labels[12] = lblSa2;
        labels[13] = lblSu2;
        labels[14] = lblMo3;
        labels[15] = lblTu3;
        labels[16] = lblWe3;
        labels[17] = lblTh3;
        labels[18] = lblFr3;
        labels[19] = lblSa3;
        labels[20] = lblSu3;
        labels[21] = lblMo4;
        labels[22] = lblTu4;
        labels[23] = lblWe4;
        labels[24] = lblTh4;
        labels[25] = lblFr4;
        labels[26] = lblSa4;
        labels[27] = lblSu4;
        labels[28] = lblMo5;
        labels[29] = lblTu5;
        labels[30] = lblWe5;
        labels[31] = lblTh5;
        labels[32] = lblFr5;
        labels[33] = lblSa5;
        labels[34] = lblSu5;
        labels[35] = lblMo6;
        labels[36] = lblTu6;

    }

    public void updateCalendarView() {
        for (int i = 0; i < 37; i++) {
            labels[i].setForeground(Color.BLACK);
        }
        LocalDate firstOfMonth = LocalDate.of(year, month + 1, 1);
        startDay = firstOfMonth.getDayOfWeek().getValue();
        int currentDay = 1;

        panels[0] = pnlMo1;
        panels[1] = pnlTu1;
        panels[2] = pnlWe1;
        panels[3] = pnlTh1;
        panels[4] = pnlFr1;
        panels[5] = pnlSa1;
        panels[6] = pnlSu1;
        panels[7] = pnlMo2;
        panels[8] = pnlTu2;
        panels[9] = pnlWe2;
        panels[10] = pnlTh2;
        panels[11] = pnlFr2;
        panels[12] = pnlSa2;
        panels[13] = pnlSu2;
        panels[14] = pnlMo3;
        panels[15] = pnlTu3;
        panels[16] = pnlWe3;
        panels[17] = pnlTh3;
        panels[18] = pnlFr3;
        panels[19] = pnlSa3;
        panels[20] = pnlSu3;
        panels[21] = pnlMo4;
        panels[22] = pnlTu4;
        panels[23] = pnlWe4;
        panels[24] = pnlTh4;
        panels[25] = pnlFr4;
        panels[26] = pnlSa4;
        panels[27] = pnlSu4;
        panels[28] = pnlMo5;
        panels[29] = pnlTu5;
        panels[30] = pnlWe5;
        panels[31] = pnlTh5;
        panels[32] = pnlFr5;
        panels[33] = pnlSa5;
        panels[34] = pnlSu5;
        panels[35] = pnlMo6;
        panels[36] = pnlTu6;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String sql = "";
        for (int i = 0; i < 21; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            noticePeriod[i] = cal.getTime();
            try {
                Statement stmt = CampusFood.connection.createStatement();
                sql = "SELECT COUNT(A.ItemID) FROM MenuLines A INNER JOIN DayMenu B ON A.MenuID=B.MenuID WHERE B.Date='" + formatSql.format(noticePeriod[i]) + "' AND B.LocationID='" + locationKey + "'";

                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        menuSet[i] = true;
                    }
                }
                if (menuSet[i] == false) {
                    sql = "SELECT IsOpen FROM DayMenu WHERE Date='" + formatSql.format(noticePeriod[i]) + "' AND LocationID='" + locationKey + "'";
                    rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        menuSet[i] = !rs.getBoolean("IsOpen");
                    }
                    if (menuSet[i] == false) {
                        sql = "SELECT IsOpen FROM LocationTimes WHERE LocationID='" + locationKey + "' AND DayNumber=" + cal.get(Calendar.DAY_OF_WEEK) + ";";
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            menuSet[i] = !rs.getBoolean("IsOpen");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnSetMenu.setEnabled(false);
        btnSetOpening.setEnabled(false);

        lblMo1.setText("");
        lblTu1.setText("");
        lblWe1.setText("");
        lblTh1.setText("");
        lblFr1.setText("");
        lblSa1.setText("");
        lblSu1.setText("");

        switch (startDay) {
            case 1:
                lblMo1.setText(Integer.toString(currentDay));
                currentDay++;
            case 2:
                lblTu1.setText(Integer.toString(currentDay));
                currentDay++;
            case 3:
                lblWe1.setText(Integer.toString(currentDay));
                currentDay++;
            case 4:
                lblTh1.setText(Integer.toString(currentDay));
                currentDay++;
            case 5:
                lblFr1.setText(Integer.toString(currentDay));
                currentDay++;
            case 6:
                lblSa1.setText(Integer.toString(currentDay));
                currentDay++;
            case 7:
                lblSu1.setText(Integer.toString(currentDay));
                currentDay++;
        }
        lblMo2.setText(Integer.toString(currentDay));
        lblMo3.setText(Integer.toString(currentDay + 7));
        lblMo4.setText(Integer.toString(currentDay + 14));
        currentDay++;
        lblTu2.setText(Integer.toString(currentDay));
        lblTu3.setText(Integer.toString(currentDay + 7));
        lblTu4.setText(Integer.toString(currentDay + 14));
        currentDay++;
        lblWe2.setText(Integer.toString(currentDay));
        lblWe3.setText(Integer.toString(currentDay + 7));
        lblWe4.setText(Integer.toString(currentDay + 14));
        currentDay++;
        lblTh2.setText(Integer.toString(currentDay));
        lblTh3.setText(Integer.toString(currentDay + 7));
        lblTh4.setText(Integer.toString(currentDay + 14));
        currentDay++;
        lblFr2.setText(Integer.toString(currentDay));
        lblFr3.setText(Integer.toString(currentDay + 7));
        lblFr4.setText(Integer.toString(currentDay + 14));
        currentDay++;
        lblSa2.setText(Integer.toString(currentDay));
        lblSa3.setText(Integer.toString(currentDay + 7));
        lblSa4.setText(Integer.toString(currentDay + 14));
        currentDay++;
        lblSu2.setText(Integer.toString(currentDay));
        lblSu3.setText(Integer.toString(currentDay + 7));
        lblSu4.setText(Integer.toString(currentDay + 14));
        currentDay += 15;

        int daysInMonth = firstOfMonth.lengthOfMonth();

        lblMo5.setText("");
        lblTu5.setText("");
        lblWe5.setText("");
        lblTh5.setText("");
        lblFr5.setText("");
        lblSa5.setText("");
        lblSu5.setText("");
        lblMo6.setText("");
        lblTu6.setText("");

        if (currentDay > daysInMonth) {
            return;
        }
        lblMo5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblTu5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblWe5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblTh5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblFr5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblSa5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblSu5.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblMo6.setText(Integer.toString(currentDay++));
        if (currentDay > daysInMonth) {
            return;
        }
        lblTu6.setText(Integer.toString(currentDay++));

    }
    
    public void updateWarnings() {
        for (int i = 0; i < 37; i++) {
            labels[i].setForeground(Color.BLACK);
        }
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 21; i++) {
            cal.setTime(noticePeriod[i]);
            if (cal.get(Calendar.MONTH) != month) continue;
            if (menuSet[i] == false) labels[cal.get(Calendar.DAY_OF_MONTH) + startDay - 2].setForeground(Color.RED.darker());
        }
    }

    private class PanelListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof JPanel) {
                for (int i = 0; i < 37; i++) {
                    if (panels[i] == source) {
                        if (labels[i].getText().equals("")) {
                            return;
                        } else {
                            selectedDay = Integer.parseInt(labels[i].getText());
                            selectedIndex = i;
                            btnSetMenu.setEnabled(true);
                            btnSetOpening.setEnabled(true);
                            updateLabels();
                            updateWarnings();
                            labels[i].setForeground(Color.BLUE);
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
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
        jButton2 = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnPrevMonth = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        lblMonth = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnNextMonth = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pnlMo1 = new javax.swing.JPanel();
        lblMo1 = new javax.swing.JLabel();
        pnlTu1 = new javax.swing.JPanel();
        lblTu1 = new javax.swing.JLabel();
        pnlWe1 = new javax.swing.JPanel();
        lblWe1 = new javax.swing.JLabel();
        pnlTh1 = new javax.swing.JPanel();
        lblTh1 = new javax.swing.JLabel();
        pnlFr1 = new javax.swing.JPanel();
        lblFr1 = new javax.swing.JLabel();
        pnlSa1 = new javax.swing.JPanel();
        lblSa1 = new javax.swing.JLabel();
        pnlSu1 = new javax.swing.JPanel();
        lblSu1 = new javax.swing.JLabel();
        pnlMo2 = new javax.swing.JPanel();
        lblMo2 = new javax.swing.JLabel();
        pnlTu2 = new javax.swing.JPanel();
        lblTu2 = new javax.swing.JLabel();
        pnlWe2 = new javax.swing.JPanel();
        lblWe2 = new javax.swing.JLabel();
        pnlTh2 = new javax.swing.JPanel();
        lblTh2 = new javax.swing.JLabel();
        pnlFr2 = new javax.swing.JPanel();
        lblFr2 = new javax.swing.JLabel();
        pnlSa2 = new javax.swing.JPanel();
        lblSa2 = new javax.swing.JLabel();
        pnlSu2 = new javax.swing.JPanel();
        lblSu2 = new javax.swing.JLabel();
        pnlMo3 = new javax.swing.JPanel();
        lblMo3 = new javax.swing.JLabel();
        pnlTu3 = new javax.swing.JPanel();
        lblTu3 = new javax.swing.JLabel();
        pnlWe3 = new javax.swing.JPanel();
        lblWe3 = new javax.swing.JLabel();
        pnlTh3 = new javax.swing.JPanel();
        lblTh3 = new javax.swing.JLabel();
        pnlFr3 = new javax.swing.JPanel();
        lblFr3 = new javax.swing.JLabel();
        pnlSa3 = new javax.swing.JPanel();
        lblSa3 = new javax.swing.JLabel();
        pnlSu3 = new javax.swing.JPanel();
        lblSu3 = new javax.swing.JLabel();
        pnlMo4 = new javax.swing.JPanel();
        lblMo4 = new javax.swing.JLabel();
        pnlTu4 = new javax.swing.JPanel();
        lblTu4 = new javax.swing.JLabel();
        pnlWe4 = new javax.swing.JPanel();
        lblWe4 = new javax.swing.JLabel();
        pnlTh4 = new javax.swing.JPanel();
        lblTh4 = new javax.swing.JLabel();
        pnlFr4 = new javax.swing.JPanel();
        lblFr4 = new javax.swing.JLabel();
        pnlSa4 = new javax.swing.JPanel();
        lblSa4 = new javax.swing.JLabel();
        pnlSu4 = new javax.swing.JPanel();
        lblSu4 = new javax.swing.JLabel();
        pnlMo5 = new javax.swing.JPanel();
        lblMo5 = new javax.swing.JLabel();
        pnlTu5 = new javax.swing.JPanel();
        lblTu5 = new javax.swing.JLabel();
        pnlWe5 = new javax.swing.JPanel();
        lblWe5 = new javax.swing.JLabel();
        pnlTh5 = new javax.swing.JPanel();
        lblTh5 = new javax.swing.JLabel();
        pnlFr5 = new javax.swing.JPanel();
        lblFr5 = new javax.swing.JLabel();
        pnlSa5 = new javax.swing.JPanel();
        lblSa5 = new javax.swing.JLabel();
        pnlSu5 = new javax.swing.JPanel();
        lblSu5 = new javax.swing.JLabel();
        pnlMo6 = new javax.swing.JPanel();
        lblMo6 = new javax.swing.JLabel();
        pnlTu6 = new javax.swing.JPanel();
        lblTu6 = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        lblVerifyNum = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        btnCreateMenu = new javax.swing.JButton();
        jPanel57 = new javax.swing.JPanel();
        btnSetMenu = new javax.swing.JButton();
        jPanel58 = new javax.swing.JPanel();
        btnSetOpening = new javax.swing.JButton();
        jPanel59 = new javax.swing.JPanel();
        btnSetOpening1 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(1, 3, 20, 0));

        jButton2.setText("Change Location");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("University of Leicester | chi");
        jPanel2.add(lblTitle);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 26));
        jPanel1.setPreferredSize(new java.awt.Dimension(290, 26));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel1);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.GridLayout(8, 7));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5);

        btnPrevMonth.setText("<<");
        btnPrevMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrevMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrevMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel6);

        lblMonth.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMonth.setText("January 2021");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel7);

        btnNextMonth.setText(">>");
        btnNextMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNextMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNextMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel8);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel9);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel10);

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Mon");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel11);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tue");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel12);

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Wed");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel13);

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Thu");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel14);

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Fri");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel15);

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Sat");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel16);

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Sun");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel17);

        pnlMo1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblMo1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblMo1.setText("XX");

        javax.swing.GroupLayout pnlMo1Layout = new javax.swing.GroupLayout(pnlMo1);
        pnlMo1.setLayout(pnlMo1Layout);
        pnlMo1Layout.setHorizontalGroup(
            pnlMo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlMo1Layout.setVerticalGroup(
            pnlMo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlMo1);

        pnlTu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTu1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTu1.setText("XX");

        javax.swing.GroupLayout pnlTu1Layout = new javax.swing.GroupLayout(pnlTu1);
        pnlTu1.setLayout(pnlTu1Layout);
        pnlTu1Layout.setHorizontalGroup(
            pnlTu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTu1Layout.setVerticalGroup(
            pnlTu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTu1);

        pnlWe1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblWe1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblWe1.setText("XX");

        javax.swing.GroupLayout pnlWe1Layout = new javax.swing.GroupLayout(pnlWe1);
        pnlWe1.setLayout(pnlWe1Layout);
        pnlWe1Layout.setHorizontalGroup(
            pnlWe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlWe1Layout.setVerticalGroup(
            pnlWe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlWe1);

        pnlTh1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTh1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTh1.setText("XX");

        javax.swing.GroupLayout pnlTh1Layout = new javax.swing.GroupLayout(pnlTh1);
        pnlTh1.setLayout(pnlTh1Layout);
        pnlTh1Layout.setHorizontalGroup(
            pnlTh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTh1Layout.setVerticalGroup(
            pnlTh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTh1);

        pnlFr1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFr1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFr1.setText("XX");

        javax.swing.GroupLayout pnlFr1Layout = new javax.swing.GroupLayout(pnlFr1);
        pnlFr1.setLayout(pnlFr1Layout);
        pnlFr1Layout.setHorizontalGroup(
            pnlFr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlFr1Layout.setVerticalGroup(
            pnlFr1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlFr1);

        pnlSa1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSa1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSa1.setText("XX");

        javax.swing.GroupLayout pnlSa1Layout = new javax.swing.GroupLayout(pnlSa1);
        pnlSa1.setLayout(pnlSa1Layout);
        pnlSa1Layout.setHorizontalGroup(
            pnlSa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSa1Layout.setVerticalGroup(
            pnlSa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSa1);

        pnlSu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSu1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSu1.setText("XX");

        javax.swing.GroupLayout pnlSu1Layout = new javax.swing.GroupLayout(pnlSu1);
        pnlSu1.setLayout(pnlSu1Layout);
        pnlSu1Layout.setHorizontalGroup(
            pnlSu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu1)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSu1Layout.setVerticalGroup(
            pnlSu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSu1);

        pnlMo2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblMo2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblMo2.setText("XX");

        javax.swing.GroupLayout pnlMo2Layout = new javax.swing.GroupLayout(pnlMo2);
        pnlMo2.setLayout(pnlMo2Layout);
        pnlMo2Layout.setHorizontalGroup(
            pnlMo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlMo2Layout.setVerticalGroup(
            pnlMo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlMo2);

        pnlTu2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTu2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTu2.setText("XX");

        javax.swing.GroupLayout pnlTu2Layout = new javax.swing.GroupLayout(pnlTu2);
        pnlTu2.setLayout(pnlTu2Layout);
        pnlTu2Layout.setHorizontalGroup(
            pnlTu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTu2Layout.setVerticalGroup(
            pnlTu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTu2);

        pnlWe2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblWe2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblWe2.setText("XX");

        javax.swing.GroupLayout pnlWe2Layout = new javax.swing.GroupLayout(pnlWe2);
        pnlWe2.setLayout(pnlWe2Layout);
        pnlWe2Layout.setHorizontalGroup(
            pnlWe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlWe2Layout.setVerticalGroup(
            pnlWe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlWe2);

        pnlTh2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTh2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTh2.setText("XX");

        javax.swing.GroupLayout pnlTh2Layout = new javax.swing.GroupLayout(pnlTh2);
        pnlTh2.setLayout(pnlTh2Layout);
        pnlTh2Layout.setHorizontalGroup(
            pnlTh2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTh2Layout.setVerticalGroup(
            pnlTh2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTh2);

        pnlFr2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFr2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFr2.setText("XX");

        javax.swing.GroupLayout pnlFr2Layout = new javax.swing.GroupLayout(pnlFr2);
        pnlFr2.setLayout(pnlFr2Layout);
        pnlFr2Layout.setHorizontalGroup(
            pnlFr2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlFr2Layout.setVerticalGroup(
            pnlFr2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlFr2);

        pnlSa2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSa2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSa2.setText("XX");

        javax.swing.GroupLayout pnlSa2Layout = new javax.swing.GroupLayout(pnlSa2);
        pnlSa2.setLayout(pnlSa2Layout);
        pnlSa2Layout.setHorizontalGroup(
            pnlSa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSa2Layout.setVerticalGroup(
            pnlSa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSa2);

        pnlSu2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSu2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSu2.setText("XX");

        javax.swing.GroupLayout pnlSu2Layout = new javax.swing.GroupLayout(pnlSu2);
        pnlSu2.setLayout(pnlSu2Layout);
        pnlSu2Layout.setHorizontalGroup(
            pnlSu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu2)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSu2Layout.setVerticalGroup(
            pnlSu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSu2);

        pnlMo3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblMo3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblMo3.setText("XX");

        javax.swing.GroupLayout pnlMo3Layout = new javax.swing.GroupLayout(pnlMo3);
        pnlMo3.setLayout(pnlMo3Layout);
        pnlMo3Layout.setHorizontalGroup(
            pnlMo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlMo3Layout.setVerticalGroup(
            pnlMo3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlMo3);

        pnlTu3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTu3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTu3.setText("XX");

        javax.swing.GroupLayout pnlTu3Layout = new javax.swing.GroupLayout(pnlTu3);
        pnlTu3.setLayout(pnlTu3Layout);
        pnlTu3Layout.setHorizontalGroup(
            pnlTu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTu3Layout.setVerticalGroup(
            pnlTu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTu3);

        pnlWe3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblWe3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblWe3.setText("XX");

        javax.swing.GroupLayout pnlWe3Layout = new javax.swing.GroupLayout(pnlWe3);
        pnlWe3.setLayout(pnlWe3Layout);
        pnlWe3Layout.setHorizontalGroup(
            pnlWe3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlWe3Layout.setVerticalGroup(
            pnlWe3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlWe3);

        pnlTh3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTh3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTh3.setText("XX");

        javax.swing.GroupLayout pnlTh3Layout = new javax.swing.GroupLayout(pnlTh3);
        pnlTh3.setLayout(pnlTh3Layout);
        pnlTh3Layout.setHorizontalGroup(
            pnlTh3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTh3Layout.setVerticalGroup(
            pnlTh3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTh3);

        pnlFr3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFr3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFr3.setText("XX");

        javax.swing.GroupLayout pnlFr3Layout = new javax.swing.GroupLayout(pnlFr3);
        pnlFr3.setLayout(pnlFr3Layout);
        pnlFr3Layout.setHorizontalGroup(
            pnlFr3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlFr3Layout.setVerticalGroup(
            pnlFr3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlFr3);

        pnlSa3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSa3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSa3.setText("XX");

        javax.swing.GroupLayout pnlSa3Layout = new javax.swing.GroupLayout(pnlSa3);
        pnlSa3.setLayout(pnlSa3Layout);
        pnlSa3Layout.setHorizontalGroup(
            pnlSa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSa3Layout.setVerticalGroup(
            pnlSa3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSa3);

        pnlSu3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSu3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSu3.setText("XX");

        javax.swing.GroupLayout pnlSu3Layout = new javax.swing.GroupLayout(pnlSu3);
        pnlSu3.setLayout(pnlSu3Layout);
        pnlSu3Layout.setHorizontalGroup(
            pnlSu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu3)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSu3Layout.setVerticalGroup(
            pnlSu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSu3);

        pnlMo4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblMo4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblMo4.setText("XX");

        javax.swing.GroupLayout pnlMo4Layout = new javax.swing.GroupLayout(pnlMo4);
        pnlMo4.setLayout(pnlMo4Layout);
        pnlMo4Layout.setHorizontalGroup(
            pnlMo4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlMo4Layout.setVerticalGroup(
            pnlMo4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlMo4);

        pnlTu4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTu4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTu4.setText("XX");

        javax.swing.GroupLayout pnlTu4Layout = new javax.swing.GroupLayout(pnlTu4);
        pnlTu4.setLayout(pnlTu4Layout);
        pnlTu4Layout.setHorizontalGroup(
            pnlTu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTu4Layout.setVerticalGroup(
            pnlTu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTu4);

        pnlWe4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblWe4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblWe4.setText("XX");

        javax.swing.GroupLayout pnlWe4Layout = new javax.swing.GroupLayout(pnlWe4);
        pnlWe4.setLayout(pnlWe4Layout);
        pnlWe4Layout.setHorizontalGroup(
            pnlWe4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlWe4Layout.setVerticalGroup(
            pnlWe4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlWe4);

        pnlTh4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTh4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTh4.setText("XX");

        javax.swing.GroupLayout pnlTh4Layout = new javax.swing.GroupLayout(pnlTh4);
        pnlTh4.setLayout(pnlTh4Layout);
        pnlTh4Layout.setHorizontalGroup(
            pnlTh4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTh4Layout.setVerticalGroup(
            pnlTh4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTh4);

        pnlFr4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFr4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFr4.setText("XX");

        javax.swing.GroupLayout pnlFr4Layout = new javax.swing.GroupLayout(pnlFr4);
        pnlFr4.setLayout(pnlFr4Layout);
        pnlFr4Layout.setHorizontalGroup(
            pnlFr4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlFr4Layout.setVerticalGroup(
            pnlFr4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlFr4);

        pnlSa4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSa4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSa4.setText("XX");

        javax.swing.GroupLayout pnlSa4Layout = new javax.swing.GroupLayout(pnlSa4);
        pnlSa4.setLayout(pnlSa4Layout);
        pnlSa4Layout.setHorizontalGroup(
            pnlSa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSa4Layout.setVerticalGroup(
            pnlSa4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSa4);

        pnlSu4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSu4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSu4.setText("XX");

        javax.swing.GroupLayout pnlSu4Layout = new javax.swing.GroupLayout(pnlSu4);
        pnlSu4.setLayout(pnlSu4Layout);
        pnlSu4Layout.setHorizontalGroup(
            pnlSu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu4)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSu4Layout.setVerticalGroup(
            pnlSu4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSu4);

        pnlMo5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblMo5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblMo5.setText("XX");

        javax.swing.GroupLayout pnlMo5Layout = new javax.swing.GroupLayout(pnlMo5);
        pnlMo5.setLayout(pnlMo5Layout);
        pnlMo5Layout.setHorizontalGroup(
            pnlMo5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlMo5Layout.setVerticalGroup(
            pnlMo5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlMo5);

        pnlTu5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTu5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTu5.setText("XX");

        javax.swing.GroupLayout pnlTu5Layout = new javax.swing.GroupLayout(pnlTu5);
        pnlTu5.setLayout(pnlTu5Layout);
        pnlTu5Layout.setHorizontalGroup(
            pnlTu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTu5Layout.setVerticalGroup(
            pnlTu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTu5);

        pnlWe5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblWe5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblWe5.setText("XX");

        javax.swing.GroupLayout pnlWe5Layout = new javax.swing.GroupLayout(pnlWe5);
        pnlWe5.setLayout(pnlWe5Layout);
        pnlWe5Layout.setHorizontalGroup(
            pnlWe5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlWe5Layout.setVerticalGroup(
            pnlWe5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWe5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWe5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlWe5);

        pnlTh5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTh5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTh5.setText("XX");

        javax.swing.GroupLayout pnlTh5Layout = new javax.swing.GroupLayout(pnlTh5);
        pnlTh5.setLayout(pnlTh5Layout);
        pnlTh5Layout.setHorizontalGroup(
            pnlTh5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTh5Layout.setVerticalGroup(
            pnlTh5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTh5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTh5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTh5);

        pnlFr5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFr5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblFr5.setText("XX");

        javax.swing.GroupLayout pnlFr5Layout = new javax.swing.GroupLayout(pnlFr5);
        pnlFr5.setLayout(pnlFr5Layout);
        pnlFr5Layout.setHorizontalGroup(
            pnlFr5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlFr5Layout.setVerticalGroup(
            pnlFr5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFr5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFr5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlFr5);

        pnlSa5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSa5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSa5.setText("XX");

        javax.swing.GroupLayout pnlSa5Layout = new javax.swing.GroupLayout(pnlSa5);
        pnlSa5.setLayout(pnlSa5Layout);
        pnlSa5Layout.setHorizontalGroup(
            pnlSa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSa5Layout.setVerticalGroup(
            pnlSa5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSa5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSa5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSa5);

        pnlSu5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSu5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblSu5.setText("XX");

        javax.swing.GroupLayout pnlSu5Layout = new javax.swing.GroupLayout(pnlSu5);
        pnlSu5.setLayout(pnlSu5Layout);
        pnlSu5Layout.setHorizontalGroup(
            pnlSu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu5)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlSu5Layout.setVerticalGroup(
            pnlSu5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSu5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSu5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlSu5);

        pnlMo6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblMo6.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblMo6.setText("XX");

        javax.swing.GroupLayout pnlMo6Layout = new javax.swing.GroupLayout(pnlMo6);
        pnlMo6.setLayout(pnlMo6Layout);
        pnlMo6Layout.setHorizontalGroup(
            pnlMo6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo6)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlMo6Layout.setVerticalGroup(
            pnlMo6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMo6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMo6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlMo6);

        pnlTu6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTu6.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblTu6.setText("XX");

        javax.swing.GroupLayout pnlTu6Layout = new javax.swing.GroupLayout(pnlTu6);
        pnlTu6.setLayout(pnlTu6Layout);
        pnlTu6Layout.setHorizontalGroup(
            pnlTu6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu6)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        pnlTu6Layout.setVerticalGroup(
            pnlTu6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTu6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTu6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(pnlTu6);

        lblVerifyNum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVerifyNum.setText("Check In Number");

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVerifyNum, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVerifyNum, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel54);

        btnCreateMenu.setText("Create Menu");
        btnCreateMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCreateMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCreateMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel55);

        btnSetMenu.setText("Set Menu");
        btnSetMenu.setEnabled(false);
        btnSetMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSetMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSetMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel57);

        btnSetOpening.setText("Set Opening");
        btnSetOpening.setEnabled(false);
        btnSetOpening.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetOpeningActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSetOpening, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSetOpening, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel58);

        btnSetOpening1.setText("Set Defaults");
        btnSetOpening1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetOpening1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel59Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSetOpening1, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSetOpening1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel59);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CampusFood.viewLocations();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnPrevMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevMonthActionPerformed
        month--;
        if (month == -1) {
            year--;
            month = 11;
        }
        updateMonthLabel();
        updateCalendarView();
        updateWarnings();
    }//GEN-LAST:event_btnPrevMonthActionPerformed

    private void btnNextMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextMonthActionPerformed
        month++;
        if (month == 12) {
            year++;
            month = 0;
        }
        updateMonthLabel();
        updateCalendarView();
        updateWarnings();
    }//GEN-LAST:event_btnNextMonthActionPerformed

    private void btnSetOpeningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetOpeningActionPerformed
        CampusFood.viewOpening(selectedDay, month, year, locationName, locationKey);
    }//GEN-LAST:event_btnSetOpeningActionPerformed

    private void btnSetOpening1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetOpening1ActionPerformed
        CampusFood.viewOpeningDefaults(locationName);
    }//GEN-LAST:event_btnSetOpening1ActionPerformed

    private void btnCreateMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateMenuActionPerformed
        CampusFood.viewCategoryManager(locationName, locationKey);
    }//GEN-LAST:event_btnCreateMenuActionPerformed

    private void btnSetMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetMenuActionPerformed
        CampusFood.setDayMenu(locationName, locationKey, selectedDay, month, year);
    }//GEN-LAST:event_btnSetMenuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateMenu;
    private javax.swing.JButton btnNextMonth;
    private javax.swing.JButton btnPrevMonth;
    private javax.swing.JButton btnSetMenu;
    private javax.swing.JButton btnSetOpening;
    private javax.swing.JButton btnSetOpening1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblFr1;
    private javax.swing.JLabel lblFr2;
    private javax.swing.JLabel lblFr3;
    private javax.swing.JLabel lblFr4;
    private javax.swing.JLabel lblFr5;
    private javax.swing.JLabel lblMo1;
    private javax.swing.JLabel lblMo2;
    private javax.swing.JLabel lblMo3;
    private javax.swing.JLabel lblMo4;
    private javax.swing.JLabel lblMo5;
    private javax.swing.JLabel lblMo6;
    private javax.swing.JLabel lblMonth;
    private javax.swing.JLabel lblSa1;
    private javax.swing.JLabel lblSa2;
    private javax.swing.JLabel lblSa3;
    private javax.swing.JLabel lblSa4;
    private javax.swing.JLabel lblSa5;
    private javax.swing.JLabel lblSu1;
    private javax.swing.JLabel lblSu2;
    private javax.swing.JLabel lblSu3;
    private javax.swing.JLabel lblSu4;
    private javax.swing.JLabel lblSu5;
    private javax.swing.JLabel lblTh1;
    private javax.swing.JLabel lblTh2;
    private javax.swing.JLabel lblTh3;
    private javax.swing.JLabel lblTh4;
    private javax.swing.JLabel lblTh5;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTu1;
    private javax.swing.JLabel lblTu2;
    private javax.swing.JLabel lblTu3;
    private javax.swing.JLabel lblTu4;
    private javax.swing.JLabel lblTu5;
    private javax.swing.JLabel lblTu6;
    private javax.swing.JLabel lblVerifyNum;
    private javax.swing.JLabel lblWe1;
    private javax.swing.JLabel lblWe2;
    private javax.swing.JLabel lblWe3;
    private javax.swing.JLabel lblWe4;
    private javax.swing.JLabel lblWe5;
    private javax.swing.JPanel pnlFr1;
    private javax.swing.JPanel pnlFr2;
    private javax.swing.JPanel pnlFr3;
    private javax.swing.JPanel pnlFr4;
    private javax.swing.JPanel pnlFr5;
    private javax.swing.JPanel pnlMo1;
    private javax.swing.JPanel pnlMo2;
    private javax.swing.JPanel pnlMo3;
    private javax.swing.JPanel pnlMo4;
    private javax.swing.JPanel pnlMo5;
    private javax.swing.JPanel pnlMo6;
    private javax.swing.JPanel pnlSa1;
    private javax.swing.JPanel pnlSa2;
    private javax.swing.JPanel pnlSa3;
    private javax.swing.JPanel pnlSa4;
    private javax.swing.JPanel pnlSa5;
    private javax.swing.JPanel pnlSu1;
    private javax.swing.JPanel pnlSu2;
    private javax.swing.JPanel pnlSu3;
    private javax.swing.JPanel pnlSu4;
    private javax.swing.JPanel pnlSu5;
    private javax.swing.JPanel pnlTh1;
    private javax.swing.JPanel pnlTh2;
    private javax.swing.JPanel pnlTh3;
    private javax.swing.JPanel pnlTh4;
    private javax.swing.JPanel pnlTh5;
    private javax.swing.JPanel pnlTu1;
    private javax.swing.JPanel pnlTu2;
    private javax.swing.JPanel pnlTu3;
    private javax.swing.JPanel pnlTu4;
    private javax.swing.JPanel pnlTu5;
    private javax.swing.JPanel pnlTu6;
    private javax.swing.JPanel pnlWe1;
    private javax.swing.JPanel pnlWe2;
    private javax.swing.JPanel pnlWe3;
    private javax.swing.JPanel pnlWe4;
    private javax.swing.JPanel pnlWe5;
    // End of variables declaration//GEN-END:variables
}
