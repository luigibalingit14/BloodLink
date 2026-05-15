package com.bloodlink.forms;
import com.bloodlink.utils.UserSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.bloodlink.db.DBConnection;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.Connection;




public class DashboardForm extends javax.swing.JFrame {
    
       private String currentUserRole;
       private String currentUserName;
       private javax.swing.Timer clockTimer;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardForm.class.getName());

    /**
     * Creates new form DashboardForm
     */
public DashboardForm() {
    initComponents();
    setupSidebarButtons();
    
    // ✅ FALLBACK: Load from UserSession if available
    if (com.bloodlink.utils.UserSession.isLoggedIn) {
        loadUserData(
            com.bloodlink.utils.UserSession.currentUser,
            com.bloodlink.utils.UserSession.currentRole
        );
    }   // Debug print
    System.out.println("🔍 Dashboard loading...");
    System.out.println("   isLoggedIn: " + UserSession.isLoggedIn);
    System.out.println("   currentUser: " + UserSession.currentUser);
    System.out.println("   currentRole: " + UserSession.currentRole);
    
    if (UserSession.isLoggedIn) {
        loadUserData(UserSession.currentUser, UserSession.currentRole);
        System.out.println("✅ loadUserData() called!");
    } else {
        System.out.println("⚠️ loadUserData() SKIPPED - isLoggedIn is false!");
    }
    
    
    
}

/**
 * Load summary statistics for dashboard overview
 */
/**
 * Load summary statistics for dashboard overview
 */
private void loadDashboardStats() {
    try {
        Connection con = DBConnection.connect();
        if (con == null) return;
        
        // 1. Total Donors Count ✅ FIXED
        String sql1 = "SELECT COUNT(*) as total FROM donors";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        ResultSet rs1 = pst1.executeQuery();
        if (rs1.next() && lblValueDonors != null) {  // ✅ Check null + correct label
            lblValueDonors.setText(String.valueOf(rs1.getInt("total")));
        }
        rs1.close(); pst1.close();
        
        // 2. Total Blood Units
        String sql2 = "SELECT SUM(available_units) as total FROM blood_inventory";
        PreparedStatement pst2 = con.prepareStatement(sql2);
        ResultSet rs2 = pst2.executeQuery();
        if (rs2.next() && lblValueUnits != null) {
            int total = rs2.getInt("total");
            lblValueUnits.setText(String.valueOf(total > 0 ? total : 0));
        }
        rs2.close(); pst2.close();
        
        // 3. Critical Blood Types (< 5 units)
        String sql3 = "SELECT COUNT(*) as critical FROM blood_inventory WHERE available_units < 5";
        PreparedStatement pst3 = con.prepareStatement(sql3);
        ResultSet rs3 = pst3.executeQuery();
        if (rs3.next() && lblValueCritical != null) {
            int critical = rs3.getInt("critical");
            lblValueCritical.setText(String.valueOf(critical));
            
            if (critical > 0) {
                lblValueCritical.setForeground(new Color(220, 20, 60));
                if (lblSubtitleCritical != null) lblSubtitleCritical.setText("Need Attention!");
            } else {
                lblValueCritical.setForeground(new Color(59, 130, 246));
                if (lblSubtitleCritical != null) lblSubtitleCritical.setText("All Good!");
            }
        }
        rs3.close(); pst3.close();
        
        // 4. Recent Donations (Last 7 Days)
        String sql4 = "SELECT COUNT(*) as recent FROM donors WHERE donation_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
        PreparedStatement pst4 = con.prepareStatement(sql4);
        ResultSet rs4 = pst4.executeQuery();
        if (rs4.next() && lblValueRecent != null) {
            lblValueRecent.setText(String.valueOf(rs4.getInt("recent")));
        }
        rs4.close(); pst4.close();
        
        con.close();
        
    } catch (Exception e) {
        System.err.println("❌ Error loading stats: " + e.getMessage());
        
        // ✅ Fallback values - LAHAT NG LABELS
        if (lblValueDonors != null) lblValueDonors.setText("0");
        if (lblValueUnits != null) lblValueUnits.setText("0");
        if (lblValueCritical != null) lblValueCritical.setText("0");
        if (lblValueRecent != null) lblValueRecent.setText("0");
    }
}


/**
 * Refresh stats (call after donor registration/deletion)
 */
private void refreshStats() {
    loadDashboardStats();
}




private void setupSidebarButtons() {
    setSidebarActive(btnDashboard);

    // ✅ DASHBOARD BUTTON: Highlight lang, HUWAG mag-oopen ng bago!
    btnDashboard.addActionListener(e -> setSidebarActive(btnDashboard));
    
    // ✅ REGISTER DONOR: Sarado current → Buksan bago
    btnRegister.addActionListener(e -> {
        this.dispose();
        new RegisterDonorForm().setVisible(true);
    });
    
    // ✅ VIEW DONORS
    btnViewDonors.addActionListener(e -> {
        this.dispose();
        new ViewDonorsForm().setVisible(true);
    });
    
    // ✅ INVENTORY
    btnInventory.addActionListener(e -> {
        this.dispose();
        new InventoryForm().setVisible(true);
    });
    
// ✅ KEEP THIS ONE (mas clean):
btnLogout.addActionListener(e -> {
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to logout?", "Confirm Logout", 
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        // ✅ Clear session before logout
        com.bloodlink.utils.UserSession.clearSession();
        
        this.dispose();
        new LoginForm().setVisible(true);
    }
});
}


// 👇 DITO ANG SECRET: Helper para i-manage ang Red/Green colors
private void setSidebarActive(CustomComponents.SidebarButton activeBtn) {
    btnDashboard.setActive(false);
    btnRegister.setActive(false);
    btnViewDonors.setActive(false);
    btnInventory.setActive(false);
    btnReports.setActive(false);
    btnSettings.setActive(false);
    
    activeBtn.setActive(true);
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
    loadDashboardStats();
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

        glassPanel7 = new customcontrols.GlassPanel();
        logoLabel11 = new customcontrols.LogoLabel();
        lblValueUnits = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        glassPanel6 = new customcontrols.GlassPanel();
        lblValueRecent = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        logoLabel13 = new customcontrols.LogoLabel();
        redGlassPanel1 = new customcontrols.RedGlassPanel();
        lblSubtitleCritical = new javax.swing.JLabel();
        logoLabel12 = new customcontrols.LogoLabel();
        lblValueCritical = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        glassPanel3 = new customcontrols.GlassPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblValueDonors = new javax.swing.JLabel();
        logoLabel14 = new customcontrols.LogoLabel();
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
        glassPanel4 = new customcontrols.GlassPanel();

        setTitle("BloodLink - Dashboard");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        glassPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/blood drop.png"))); // NOI18N
        logoLabel11.setText("logoLabel10");
        glassPanel7.add(logoLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 140));

        lblValueUnits.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblValueUnits.setForeground(new java.awt.Color(255, 255, 255));
        lblValueUnits.setText("no.");
        glassPanel7.add(lblValueUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, 110));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Blood Inventory");
        glassPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Units");
        glassPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 77, -1));

        getContentPane().add(glassPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 130, 350, 190));

        glassPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblValueRecent.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblValueRecent.setForeground(new java.awt.Color(255, 255, 255));
        lblValueRecent.setText("no.");
        glassPanel6.add(lblValueRecent, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 90, 110));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Recent Donations");
        glassPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 100, -1));

        logoLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel13.setText("logoLabel10");
        glassPanel6.add(logoLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 140));

        getContentPane().add(glassPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 350, 350, 190));

        redGlassPanel1.setPreferredSize(new java.awt.Dimension(350, 190));
        redGlassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSubtitleCritical.setForeground(new java.awt.Color(255, 255, 255));
        lblSubtitleCritical.setText("jLabel7");
        redGlassPanel1.add(lblSubtitleCritical, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 120, -1));

        logoLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/warning 2.png"))); // NOI18N
        logoLabel12.setText("logoLabel10");
        redGlassPanel1.add(logoLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 350, 140));

        lblValueCritical.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblValueCritical.setForeground(new java.awt.Color(255, 255, 255));
        lblValueCritical.setText("no.");
        redGlassPanel1.add(lblValueCritical, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 90, 110));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Low Stock Alert");
        redGlassPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 100, -1));

        getContentPane().add(redGlassPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 350, 340, 190));

        glassPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Donors");
        glassPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 21, 77, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Registered");
        glassPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 77, -1));

        lblValueDonors.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lblValueDonors.setForeground(new java.awt.Color(255, 255, 255));
        lblValueDonors.setText("no.");
        glassPanel3.add(lblValueDonors, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, 110));

        logoLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/donation icon.png"))); // NOI18N
        logoLabel14.setText("logoLabel10");
        glassPanel3.add(logoLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 350, 140));

        getContentPane().add(glassPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 350, 190));

        glassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, ");
        glassPanel1.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 420, 40));
        glassPanel1.add(lblRoleBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, 60, 25));

        lblDateTime.setForeground(new java.awt.Color(255, 255, 255));
        lblDateTime.setText("TIME");
        glassPanel1.add(lblDateTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 170, 25));

        getContentPane().add(glassPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 750, 100));

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
        getContentPane().add(MainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 700));

        javax.swing.GroupLayout glassPanel4Layout = new javax.swing.GroupLayout(glassPanel4);
        glassPanel4.setLayout(glassPanel4Layout);
        glassPanel4Layout.setHorizontalGroup(
            glassPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        glassPanel4Layout.setVerticalGroup(
            glassPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );

        getContentPane().add(glassPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 130, 250, 110));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegisterActionPerformed

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customcontrols.ModernLabel MainPanel;
    private CustomComponents.GlassPanel SidePanel;
    private CustomComponents.SidebarButton btnDashboard;
    private CustomComponents.SidebarButton btnInventory;
    private CustomComponents.SidebarButton btnLogout;
    private CustomComponents.SidebarButton btnRegister;
    private CustomComponents.SidebarButton btnReports;
    private CustomComponents.SidebarButton btnSettings;
    private CustomComponents.SidebarButton btnViewDonors;
    private customcontrols.GlassPanel glassPanel1;
    private customcontrols.GlassPanel glassPanel2;
    private customcontrols.GlassPanel glassPanel3;
    private customcontrols.GlassPanel glassPanel4;
    private customcontrols.GlassPanel glassPanel6;
    private customcontrols.GlassPanel glassPanel7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblDateTime;
    private CustomComponents.RoleBadge lblRoleBadge;
    private javax.swing.JLabel lblSubtitleCritical;
    private javax.swing.JLabel lblValueCritical;
    private javax.swing.JLabel lblValueDonors;
    private javax.swing.JLabel lblValueRecent;
    private javax.swing.JLabel lblValueUnits;
    private javax.swing.JLabel lblWelcome;
    private customcontrols.LogoLabel logoLabel1;
    private customcontrols.LogoLabel logoLabel11;
    private customcontrols.LogoLabel logoLabel12;
    private customcontrols.LogoLabel logoLabel13;
    private customcontrols.LogoLabel logoLabel14;
    private customcontrols.LogoLabel logoLabel2;
    private customcontrols.LogoLabel logoLabel3;
    private customcontrols.LogoLabel logoLabel4;
    private customcontrols.LogoLabel logoLabel5;
    private customcontrols.LogoLabel logoLabel6;
    private customcontrols.LogoLabel logoLabel7;
    private customcontrols.LogoLabel logoLabel8;
    private customcontrols.LogoLabel logoLabel9;
    private javax.swing.JPanel pantakipPanel;
    private customcontrols.RedGlassPanel redGlassPanel1;
    // End of variables declaration//GEN-END:variables
}
