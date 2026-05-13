package com.bloodlink.forms;
import com.bloodlink.utils.UserSession;
import CustomComponents.SidebarButton;
import CustomComponents.GlassPanel;
import CustomComponents.RoleBadge;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


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

        setTitle("BloodLink - Dashboard");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
    private javax.swing.JLabel lblDateTime;
    private CustomComponents.RoleBadge lblRoleBadge;
    private javax.swing.JLabel lblWelcome;
    private customcontrols.LogoLabel logoLabel1;
    private customcontrols.LogoLabel logoLabel2;
    private customcontrols.LogoLabel logoLabel3;
    private customcontrols.LogoLabel logoLabel4;
    private customcontrols.LogoLabel logoLabel5;
    private customcontrols.LogoLabel logoLabel6;
    private customcontrols.LogoLabel logoLabel7;
    private customcontrols.LogoLabel logoLabel8;
    private customcontrols.LogoLabel logoLabel9;
    private javax.swing.JPanel pantakipPanel;
    // End of variables declaration//GEN-END:variables
}
