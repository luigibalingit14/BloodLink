package com.bloodlink.forms;
import com.bloodlink.utils.UserSession;  
import CustomComponents.SidebarButton;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ViewDonorsForm extends javax.swing.JFrame {
    
       private String currentUserRole;
       private String currentUserName;
       private javax.swing.Timer clockTimer;
    
    
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ViewDonorsForm.class.getName());

    
      
    /**
     * Creates new form ViewDonorsForm
     */
   
    public ViewDonorsForm() {
        initComponents();
        setupViewDonorsSidebar(); 
        loadDonorsToTable(); 
        setupHeader();
        
            // ✅ ✅ ✅ DAGDAGAN MO ITO: CHECK ROLE PARA SA PANTAKIP PANEL
    if (com.bloodlink.utils.UserSession.currentRole != null && 
        com.bloodlink.utils.UserSession.currentRole.equalsIgnoreCase("Admin")) {
        pantakipPanel.setVisible(false);  // HIDE PANEL FOR ADMIN
    } else {
        pantakipPanel.setVisible(true);   // SHOW PANEL FOR STAFF
    }
    
        
            // ✅ HIDE REPORTS & SETTINGS FOR STAFF
    if (UserSession.currentRole != null && 
        UserSession.currentRole.equalsIgnoreCase("Staff")) {
        btnReports.setVisible(false);
        btnSettings.setVisible(false);
    }
                // --- FORCE GLASSMORPHISM SA TABLE ---
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder()); 
        
         tblDonors.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer)tblDonors.getDefaultRenderer(Object.class)).setOpaque(false);
         tblDonors.setShowGrid(false); 
        tblDonors.setForeground(java.awt.Color.WHITE); 
        
        
                                           

// Make header transparent
tblDonors.getTableHeader().setOpaque(false);
tblDonors.getTableHeader().setBackground(new java.awt.Color(0, 0, 0, 0));
tblDonors.getTableHeader().setForeground(java.awt.Color.WHITE);

// Set header font
tblDonors.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

// Make cells transparent
DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) tblDonors.getDefaultRenderer(Object.class);
cellRenderer.setOpaque(false);
cellRenderer.setForeground(java.awt.Color.WHITE);
        
    }
    
    /**
 * Helper: Highlight active sidebar button
 */
private void setSidebarActive(CustomComponents.SidebarButton activeBtn) {
    btnDashboard.setActive(false);
    btnRegister.setActive(false);
    btnViewDonors.setActive(false);
    btnInventory.setActive(false);
    btnReports.setActive(false);
    btnSettings.setActive(false);
    
    activeBtn.setActive(true);
}
    

/**
 * Helper: Setup header UI with current user session
 */
private void setupHeader() {
    // Set welcome message
    lblWelcome.setText(" Welcome, " + UserSession.currentUser + "!");
    
    // Set role badge
    lblRoleBadge.setText(UserSession.currentRole.toUpperCase());
    
    // Color code by role
    if (UserSession.currentRole.equalsIgnoreCase("Admin")) {
        lblRoleBadge.setForeground(new java.awt.Color(220, 20, 60)); // Crimson
        lblRoleBadge.setBackground(new java.awt.Color(255, 240, 240));
    } else {
        lblRoleBadge.setForeground(new java.awt.Color(59, 130, 246)); // Blue
        lblRoleBadge.setBackground(new java.awt.Color(239, 246, 255));
    }
    
    // Start live clock
    startClock();
}



/**
 * Setup sidebar button actions for ViewDonorsForm
 */
private void setupViewDonorsSidebar() {
    setSidebarActive(btnViewDonors);

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
    
    // 🔘 VIEW DONORS: Highlight lang
    btnViewDonors.addActionListener(e -> {
        setSidebarActive(btnViewDonors);
        loadDonorsToTable(); // Optional: Refresh table
    });
    
    // 🔘 INVENTORY
    btnInventory.addActionListener(e -> {
        this.dispose();
        new InventoryForm().setVisible(true);
    });
    
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
    

private void loadDonorsToTable() {
    DefaultTableModel model = (DefaultTableModel) tblDonors.getModel();
    model.setRowCount(0); // Clear existing rows
    
    try {
        java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
        String sql = "SELECT id, name, age, blood_group, phone, address, donation_date FROM donors ORDER BY id DESC";
        java.sql.PreparedStatement pst = con.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            Object[] row = {
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("blood_group"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("donation_date")
            };
            model.addRow(row);
        }
        
        rs.close();
        pst.close();
        con.close();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Error loading data: " + e.getMessage());
    }
}




private void searchDonors() {
    String keyword = txtSearch.getText().trim();
    DefaultTableModel model = (DefaultTableModel) tblDonors.getModel();
    model.setRowCount(0);
    
    try {
        java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
        String sql = "SELECT * FROM donors WHERE name LIKE ? OR blood_group LIKE ? ORDER BY id DESC";
        java.sql.PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        
        java.sql.ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            Object[] row = {
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("blood_group"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("donation_date")
            };
            model.addRow(row);
        }
        
        rs.close();
        pst.close();
        con.close();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "❌ Search error: " + e.getMessage());
    }
}



private void deleteDonor() {
    int selectedRow = tblDonors.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "⚠️ Please select a donor to delete!", 
            "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int id = Integer.parseInt(tblDonors.getValueAt(selectedRow, 0).toString());
    String name = tblDonors.getValueAt(selectedRow, 1).toString();
    
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Delete donor: " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
            String sql = "DELETE FROM donors WHERE id = ?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "✅ Donor deleted!");
                loadDonorsToTable();
            }
            pst.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Delete failed: " + e.getMessage());
        }
    }
}


public void loadUserData(String name, String role) {
    this.currentUserName = name;
    this.currentUserRole = role;
    
    // 1. Update welcome label
    lblWelcome.setText(" Welcome, " + name + "!");
    
    // 2. Update role badge colors
    if (lblRoleBadge != null) {
        lblRoleBadge.setText(role.toUpperCase());
        
        if (role.equalsIgnoreCase("Admin")) {
            lblRoleBadge.setForeground(new Color(220, 20, 60)); // Crimson
            lblRoleBadge.setBackground(new Color(255, 240, 240));
        } else {
            lblRoleBadge.setForeground(new Color(59, 130, 246)); // Blue
            lblRoleBadge.setBackground(new Color(239, 246, 255));
        }
    }
    
        // ✅ ✅ ✅ VISIBILITY LOGIC - DAPAT LABAS NG IF-ELSE! ✅ ✅ ✅
    boolean isAdmin = role.equalsIgnoreCase("Admin");
    
    // Hide/Show Buttons
    btnReports.setVisible(isAdmin);
    btnSettings.setVisible(isAdmin);
    
    // Hide/Show Cover Panel (pantakipPanel)
    if (pantakipPanel != null) {
        pantakipPanel.setVisible(!isAdmin); // Show for Staff, Hide for Admin
    }
    
    // 3. Update icons (optional)
    updateIconVisibility(role);
    
    // 4. Start clock
    startClock();
}

/**
 * Start the live date/time updater
 */
private void startClock() {
    // Stop existing timer if any (prevent duplicates)
    if (clockTimer != null) {
        clockTimer.stop();
    }
    
    clockTimer = new javax.swing.Timer(1000, e -> {
        try {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter fmt = 
                java.time.format.DateTimeFormatter.ofPattern("EEE, MMM dd • hh:mm:ss a");
            lblDateTime.setText(now.format(fmt));
        } catch (Exception ex) {
            lblDateTime.setText("Error loading time");
        }
    });
    clockTimer.start();
}



/**
 * Helper method to update icon colors/visibility based on role
 * Call this from loadUserData() to sync UI with user role
 */
private void updateIconVisibility(String role) {
    boolean isAdmin = role.equalsIgnoreCase("Admin");
    
if (!isAdmin) {
    // Swap to lock icon for staff
    try {
        ImageIcon lockIcon = new ImageIcon(getClass().getResource("/images/lock_small.png"));
        if (logoLabel7 != null) logoLabel7.setIcon(lockIcon);
        if (logoLabel8 != null) logoLabel8.setIcon(lockIcon);
    } catch (Exception e) {
        // Fallback: just gray out if icon not found
        if (logoLabel7 != null) logoLabel7.setForeground(new Color(150,150,150));
        if (logoLabel8 != null) logoLabel8.setForeground(new Color(150,150,150));
    }
} else {
    // Restore original icons for admin
    try {
        ImageIcon reportsIcon = new ImageIcon(getClass().getResource("/images/reports.png"));
        ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/images/settings.png"));
        if (logoLabel7 != null) logoLabel7.setIcon(reportsIcon);
        if (logoLabel8 != null) logoLabel8.setIcon(settingsIcon);
    } catch (Exception e) {
        // Ignore if icons not found
    }
}
   
    
    // Force UI refresh to apply changes immediately
    SidePanel.revalidate();
    SidePanel.repaint();
}





/**
 * Clean up timer when form closes (prevent memory leaks)
 */
@Override
public void dispose() {
    if (clockTimer != null) {
        clockTimer.stop();
    }
    super.dispose();
}


    
    
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDonors = new customcontrols.GlassTable();
        glassPanel3 = new customcontrols.GlassPanel();
        logoLabel13 = new customcontrols.LogoLabel();
        btnSearch = new CustomComponents.RoundedButton();
        btnRefresh = new CustomComponents.RoundedButton();
        txtSearch = new customcontrols.ModernTextbox();
        lblTitle = new javax.swing.JLabel();
        btnDelete = new CustomComponents.RoundedButton();
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
        setTitle("BloodLink - View Donors");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblDonors.setForeground(new java.awt.Color(0, 0, 0));
        tblDonors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Full Name", "Age", "Blood Group", "Phone", "Address", "Donation Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDonors.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblDonors.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tblDonors);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 252, 870, 370));

        glassPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel13.setText("logoLabel4");
        glassPanel3.add(logoLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 1030, 580));

        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        glassPanel3.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 80, 80, 30));

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setForeground(new java.awt.Color(0, 0, 0));
        btnRefresh.setText(" Refresh");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);
        glassPanel3.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 100, 30));

        txtSearch.setForeground(new java.awt.Color(255, 255, 255));
        glassPanel3.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 30));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Donor Records");
        glassPanel3.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        btnDelete.setText("Delete ");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        glassPanel3.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, -1, -1));

        logoLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iconback.png"))); // NOI18N
        logoLabel10.setText("logoLabel10");
        glassPanel3.add(logoLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 530, 30, 40));

        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("       Back to Dashboard");
        btnBack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnBack.addActionListener(this::btnBackActionPerformed);
        glassPanel3.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 530, 180, 40));

        getContentPane().add(glassPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 890, 580));

        glassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, ");
        glassPanel1.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 470, 40));
        glassPanel1.add(lblRoleBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 60, 25));

        lblDateTime.setForeground(new java.awt.Color(255, 255, 255));
        lblDateTime.setText("TIME");
        glassPanel1.add(lblDateTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 170, 25));

        getContentPane().add(glassPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 890, 100));

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
        getContentPane().add(MainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 700));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
         searchDonors();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
           txtSearch.setText("");
           loadDonorsToTable();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        new DashboardForm().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
      deleteDonor();
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customcontrols.ModernLabel MainPanel;
    private CustomComponents.GlassPanel SidePanel;
    private CustomComponents.SidebarButton btnBack;
    private CustomComponents.SidebarButton btnDashboard;
    private CustomComponents.RoundedButton btnDelete;
    private CustomComponents.SidebarButton btnInventory;
    private CustomComponents.SidebarButton btnLogout;
    private CustomComponents.RoundedButton btnRefresh;
    private CustomComponents.SidebarButton btnRegister;
    private CustomComponents.SidebarButton btnReports;
    private CustomComponents.RoundedButton btnSearch;
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
    private customcontrols.GlassTable tblDonors;
    private customcontrols.ModernTextbox txtSearch;
    // End of variables declaration//GEN-END:variables
}
