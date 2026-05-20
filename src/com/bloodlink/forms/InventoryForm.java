package com.bloodlink.forms;


import com.bloodlink.utils.UserSession;
import com.bloodlink.db.DBConnection;
import CustomComponents.SidebarButton;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventoryForm extends javax.swing.JFrame {
    
    private javax.swing.Timer clockTimer;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InventoryForm.class.getName());

    /**
     * Creates new form InventoryForm
     */
    public InventoryForm() {
        initComponents();
        
        // ✅ ✅ ✅ FIX: ROLE CHECK FOR PANTAKIP PANEL
        if (UserSession.currentRole != null && UserSession.currentRole.equalsIgnoreCase("Admin")) {
            pantakipPanel.setVisible(false); // Admin sees all buttons
        } else {
            pantakipPanel.setVisible(true);  // Staff sees overlay
        }
        

    
    // ✅ ADD THIS: HIDE BUTTONS DIRECTLY FOR STAFF
    if (UserSession.currentRole != null && 
        UserSession.currentRole.equalsIgnoreCase("Staff")) {
        btnReports.setVisible(false);
        btnSettings.setVisible(false);
    }
    
        
        setupInventorySidebar();
        setupHeader();
        loadInventory();
        
        // --- GLASSMORPHISM STYLING FOR TABLE ---
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        
        tblInventory.setOpaque(false);
        tblInventory.setShowGrid(false);
        tblInventory.setForeground(Color.WHITE);
        
        // Make header transparent
        tblInventory.getTableHeader().setOpaque(false);
        tblInventory.getTableHeader().setBackground(new Color(0, 0, 0, 0));
        tblInventory.getTableHeader().setForeground(Color.WHITE);
    }

    // =====================================================================
    // 🔘 SIDEBAR & HEADER LOGIC
    // =====================================================================
    
    private void setSidebarActive(SidebarButton activeBtn) {
        btnDashboard.setActive(false);
        btnRegister.setActive(false);
        btnViewDonors.setActive(false);
        btnInventory.setActive(false);
        btnReports.setActive(false);
        btnSettings.setActive(false);
        activeBtn.setActive(true);
    }

private void setupInventorySidebar() {
    setSidebarActive(btnInventory);

    // 🔘 DASHBOARD
    btnDashboard.addActionListener(e -> {
        this.dispose();
        new DashboardForm().setVisible(true);
    });
    
    // 🔘 REGISTER DONOR
    btnRegister.addActionListener(e -> {
        this.dispose();
        new RegisterDonorForm().setVisible(true);
    });
    
    // 🔘 VIEW DONORS
    btnViewDonors.addActionListener(e -> {
        this.dispose();
        new ViewDonorsForm().setVisible(true);
    });
    
    // 🔘 INVENTORY: Highlight lang
    btnInventory.addActionListener(e -> setSidebarActive(btnInventory));
    
    // 🔘 REPORTS & ANALYTICS ✨ NEW
    btnReports.addActionListener(e -> {
        this.dispose();
        new ReportsForm().setVisible(true);
    });
    
    // 🔘 SYSTEM SETTINGS ✨ NEW (Admin Only)
    btnSettings.addActionListener(e -> {
        if ("Admin".equalsIgnoreCase(UserSession.currentRole)) {
            this.dispose();
            new SettingsForm().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "⛔ Access Denied!\nSystem Settings are for Admins only.", 
                "Unauthorized", JOptionPane.WARNING_MESSAGE);
        }
    });
    
    // 🔘 LOGOUT
    btnLogout.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Logout from BloodLink?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            UserSession.clearSession();
            this.dispose();
            new LoginForm().setVisible(true);
        }
    });
}

    private void setupHeader() {
        lblWelcome.setText(" Welcome, " + UserSession.currentUser + "!");
        lblRoleBadge.setText(UserSession.currentRole.toUpperCase());
        
        if (UserSession.currentRole.equalsIgnoreCase("Admin")) {
            lblRoleBadge.setForeground(new Color(220, 20, 60));
            lblRoleBadge.setBackground(new Color(255, 240, 240));
        } else {
            lblRoleBadge.setForeground(new Color(59, 130, 246));
            lblRoleBadge.setBackground(new Color(239, 246, 255));
        }
        startClock();
    }

    private void startClock() {
        if (clockTimer != null) clockTimer.stop();
        clockTimer = new javax.swing.Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("EEE, MMM dd • hh:mm:ss a");
            lblDateTime.setText(now.format(fmt));
        });
        clockTimer.start();
    }

    // =====================================================================
    // 🩸 INVENTORY LOGIC (COLOR-CODED)
    // =====================================================================

    private void loadInventory() {
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.setRowCount(0); // Clear table
        
        try {
            Connection con = DBConnection.connect();
            String sql = "SELECT blood_group, available_units FROM blood_inventory ORDER BY blood_group ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String group = rs.getString("blood_group");
                int units = rs.getInt("available_units");
                String status = (units < 5) ? "CRITICAL" : "NORMAL";
                
                model.addRow(new Object[]{group, units, status});
            }
            
            rs.close(); pst.close(); con.close();
            
            // ✅ ✅ ✅ COLOR-CODING RENDERER
            tblInventory.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (!isSelected) {
                        Object statusObj = table.getValueAt(row, 2); // Status column index
                        if (statusObj != null && statusObj.toString().equals("CRITICAL")) {
                            c.setForeground(Color.RED); // 🔴 Critical Stock
                            c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        } else {
                            c.setForeground(Color.WHITE); // ✅ Normal Stock
                            c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        }
                    } else {
                        c.setForeground(table.getSelectionForeground());
                    }
                    return c;
                }
            });
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading inventory: " + e.getMessage());
        }
    }

    private void searchInventory() {
        String keyword = txtSearch.getText().trim().toUpperCase();
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.setRowCount(0);
        
        try {
            Connection con = DBConnection.connect();
            String sql = "SELECT blood_group, available_units FROM blood_inventory WHERE blood_group LIKE ? ORDER BY blood_group ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                String group = rs.getString("blood_group");
                int units = rs.getInt("available_units");
                String status = (units < 5) ? "CRITICAL" : "NORMAL";
                model.addRow(new Object[]{group, units, status});
            }
            
            rs.close(); pst.close(); con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Search error: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        if (clockTimer != null) clockTimer.stop();
        super.dispose();
    }

    // ... (Rest of your auto-generated initComponents code here) ...

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        glassPanel3 = new customcontrols.GlassPanel();
        txtSearch = new customcontrols.ModernTextbox();
        lblTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInventory = new customcontrols.GlassTable();
        logoLabel13 = new customcontrols.LogoLabel();
        btnRefresh = new CustomComponents.RoundedButton();
        logoLabel10 = new customcontrols.LogoLabel();
        btnBack = new CustomComponents.SidebarButton();
        glassPanel1 = new customcontrols.GlassPanel();
        lblWelcome = new javax.swing.JLabel();
        lblRoleBadge = new CustomComponents.RoleBadge();
        lblDateTime = new javax.swing.JLabel();
        SidePanel = new CustomComponents.GlassPanel();
        pantakipPanel = new javax.swing.JPanel();
        logoLabel4 = new customcontrols.LogoLabel();
        logoLabel8 = new customcontrols.LogoLabel();
        logoLabel7 = new customcontrols.LogoLabel();
        logoLabel5 = new customcontrols.LogoLabel();
        logoLabel6 = new customcontrols.LogoLabel();
        logoLabel2 = new customcontrols.LogoLabel();
        btnRegister = new CustomComponents.SidebarButton();
        logoLabel1 = new customcontrols.LogoLabel();
        btnDashboard = new CustomComponents.SidebarButton();
        btnViewDonors = new CustomComponents.SidebarButton();
        btnInventory = new CustomComponents.SidebarButton();
        btnReports = new CustomComponents.SidebarButton();
        btnSettings = new CustomComponents.SidebarButton();
        btnLogout = new CustomComponents.SidebarButton();
        logoLabel3 = new customcontrols.LogoLabel();
        glassPanel2 = new customcontrols.GlassPanel();
        logoLabel9 = new customcontrols.LogoLabel();
        MainPanel = new customcontrols.ModernLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BloodLink - Blood Inventory");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        glassPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        glassPanel3.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 30));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Blood Inventory Overview");
        glassPanel3.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        tblInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Blood Group", "Available Units", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInventory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(tblInventory);

        glassPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 760, 380));

        logoLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel13.setText("logoLabel4");
        glassPanel3.add(logoLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 1040, 600));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);
        glassPanel3.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, -1));

        logoLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iconback.png"))); // NOI18N
        logoLabel10.setText("logoLabel10");
        glassPanel3.add(logoLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 520, 30, 40));

        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("       Back to Dashboard");
        btnBack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnBack.addActionListener(this::btnBackActionPerformed);
        glassPanel3.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 520, 180, 40));

        getContentPane().add(glassPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 810, 580));

        glassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, ");
        glassPanel1.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 420, 40));
        glassPanel1.add(lblRoleBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, 60, 25));

        lblDateTime.setForeground(new java.awt.Color(255, 255, 255));
        lblDateTime.setText("TIME");
        glassPanel1.add(lblDateTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 40, 170, 25));

        getContentPane().add(glassPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 810, 100));

        SidePanel.setPreferredSize(new java.awt.Dimension(250, 700));
        SidePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pantakipPanel.setBackground(new java.awt.Color(193, 184, 184));

        javax.swing.GroupLayout pantakipPanelLayout = new javax.swing.GroupLayout(pantakipPanel);
        pantakipPanel.setLayout(pantakipPanelLayout);
        pantakipPanelLayout.setHorizontalGroup(
            pantakipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        pantakipPanelLayout.setVerticalGroup(
            pantakipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        SidePanel.add(pantakipPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, 90));

        logoLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logout.png"))); // NOI18N
        logoLabel4.setText("logoLabel4");
        SidePanel.add(logoLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 50, 50));

        logoLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings.png"))); // NOI18N
        logoLabel8.setText("logoLabel8");
        SidePanel.add(logoLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 60, 50));

        logoLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reports.png"))); // NOI18N
        logoLabel7.setText("logoLabel7");
        SidePanel.add(logoLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 310, 50, 45));

        logoLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/view.png"))); // NOI18N
        logoLabel5.setText("logoLabel4");
        logoLabel5.setPreferredSize(new java.awt.Dimension(10, 20));
        SidePanel.add(logoLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 40, 50));

        logoLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/inventory.png"))); // NOI18N
        logoLabel6.setText("logoLabel6");
        SidePanel.add(logoLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 60, 50));

        logoLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/register.png"))); // NOI18N
        logoLabel2.setText("logoLabel2");
        logoLabel2.setPreferredSize(new java.awt.Dimension(10, 20));
        SidePanel.add(logoLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 45, 45));

        btnRegister.setText("           Register Donor");
        btnRegister.addActionListener(this::btnRegisterActionPerformed);
        SidePanel.add(btnRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 169, 250, 41));

        logoLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dashboard.png"))); // NOI18N
        logoLabel1.setText("logoLabel1");
        SidePanel.add(logoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 45, 45));

        btnDashboard.setText("           Dashboard Overview");
        SidePanel.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 122, 250, 41));

        btnViewDonors.setText("           View Donors");
        SidePanel.add(btnViewDonors, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 216, 250, 41));

        btnInventory.setText("           Blood Inventory");
        btnInventory.addActionListener(this::btnInventoryActionPerformed);
        SidePanel.add(btnInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 263, 250, 41));

        btnReports.setText("           Reports & Analytics");
        SidePanel.add(btnReports, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 250, 41));

        btnSettings.setText("           System Settings");
        SidePanel.add(btnSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 357, 250, 41));

        btnLogout.setText("           Logout");
        SidePanel.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 604, 250, 41));

        logoLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bloodlink_logo.png"))); // NOI18N
        logoLabel3.setText("logoLabel1");
        SidePanel.add(logoLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 100, 120));

        javax.swing.GroupLayout glassPanel2Layout = new javax.swing.GroupLayout(glassPanel2);
        glassPanel2.setLayout(glassPanel2Layout);
        glassPanel2Layout.setHorizontalGroup(
            glassPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        glassPanel2Layout.setVerticalGroup(
            glassPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        SidePanel.add(glassPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        logoLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BLOOD_LINK BADGE.png"))); // NOI18N
        logoLabel9.setText("BLOODLINK");
        SidePanel.add(logoLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 150, 80));

        getContentPane().add(SidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        MainPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Screenshot 2026-05-06 232929.png"))); // NOI18N
        getContentPane().add(MainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 700));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
          txtSearch.setText("");
          loadInventory();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
     if (txtSearch.getText().trim().isEmpty()) {
        loadInventory();
        } else {
        searchInventory();
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
            this.dispose();
            new DashboardForm().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customcontrols.ModernLabel MainPanel;
    private CustomComponents.GlassPanel SidePanel;
    private CustomComponents.SidebarButton btnBack;
    private CustomComponents.SidebarButton btnDashboard;
    private CustomComponents.SidebarButton btnInventory;
    private CustomComponents.SidebarButton btnLogout;
    private CustomComponents.RoundedButton btnRefresh;
    private CustomComponents.SidebarButton btnRegister;
    private CustomComponents.SidebarButton btnReports;
    private CustomComponents.SidebarButton btnSettings;
    private CustomComponents.SidebarButton btnViewDonors;
    private customcontrols.GlassPanel glassPanel1;
    private customcontrols.GlassPanel glassPanel2;
    private customcontrols.GlassPanel glassPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDateTime;
    private CustomComponents.RoleBadge lblRoleBadge;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcome;
    private customcontrols.LogoLabel logoLabel1;
    private customcontrols.LogoLabel logoLabel10;
    private customcontrols.LogoLabel logoLabel13;
    private customcontrols.LogoLabel logoLabel2;
    private customcontrols.LogoLabel logoLabel3;
    private customcontrols.LogoLabel logoLabel4;
    private customcontrols.LogoLabel logoLabel5;
    private customcontrols.LogoLabel logoLabel6;
    private customcontrols.LogoLabel logoLabel7;
    private customcontrols.LogoLabel logoLabel8;
    private customcontrols.LogoLabel logoLabel9;
    private javax.swing.JPanel pantakipPanel;
    private customcontrols.GlassTable tblInventory;
    private customcontrols.ModernTextbox txtSearch;
    // End of variables declaration//GEN-END:variables
}
