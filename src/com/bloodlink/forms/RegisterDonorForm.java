package com.bloodlink.forms;
import com.bloodlink.utils.UserSession;  // ← ← ← DAGDAGAN MO ITO


import com.bloodlink.db.DBConnection;
import CustomComponents.SidebarButton;
import customcontrols.GlassPanel;
import customcontrols.ModernLabel;
import javax.swing.JOptionPane;

public class RegisterDonorForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = 
        java.util.logging.Logger.getLogger(RegisterDonorForm.class.getName());

    
      private javax.swing.Timer clockTimer;
    
    
    /**
     * Creates new form RegisterDonorForm
     */
   public RegisterDonorForm() {
    initComponents();
    setupRegisterSidebar();
    // ✅ SETUP HEADER with UserSession data
    setupHeader();
    
       // ✅ HIDE REPORTS & SETTINGS FOR STAFF
    if (UserSession.currentRole != null && 
        UserSession.currentRole.equalsIgnoreCase("Staff")) {
        btnReports.setVisible(false);
        btnSettings.setVisible(false);
    }
    
    
        // ✅ ✅ ✅ DAGDAGAN MO ITO: CHECK ROLE PARA SA PANTAKIP PANEL
    if (com.bloodlink.utils.UserSession.currentRole != null && 
        com.bloodlink.utils.UserSession.currentRole.equalsIgnoreCase("Admin")) {
        pantakipPanel.setVisible(false);  // HIDE PANEL FOR ADMIN
    } else {
        pantakipPanel.setVisible(true);   // SHOW PANEL FOR STAFF
    }
    
    txtName.requestFocus();
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
 * Clean up timer when form closes (prevent memory leaks)
 */
@Override
public void dispose() {
    if (clockTimer != null) {
        clockTimer.stop();
    }
    super.dispose();
}

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


    // =====================================================================
    // 🔘 SIDEBAR LOGIC METHODS
    // =====================================================================
    
    /**
     * Helper: Highlight active sidebar button
     */
    private void setSidebarActive(SidebarButton activeBtn) {
        btnDashboard.setActive(false);
        btnRegister.setActive(false);
        btnViewDonors.setActive(false);
        btnInventory.setActive(false);
        btnReports.setActive(false);
        btnSettings.setActive(false);
        
        activeBtn.setActive(true);
    }
    
    /**
     * Setup sidebar button actions for RegisterDonorForm
     */
    private void setupRegisterSidebar() {
        // ✅ Highlight "Register Donor" button (since nandito ka na)
        setSidebarActive(btnRegister);

        // 🔘 Dashboard Button
        btnDashboard.addActionListener(e -> {
            this.dispose();
            new DashboardForm().setVisible(true);
        });

        // 🔘 Register Donor Button (Stay/Refresh)
        btnRegister.addActionListener(e -> setSidebarActive(btnRegister));

        // 🔘 View Donors
        btnViewDonors.addActionListener(e -> {
            this.dispose();
            new ViewDonorsForm().setVisible(true);
        });

        // 🔘 Blood Inventory
        btnInventory.addActionListener(e -> {
            this.dispose();
            new InventoryForm().setVisible(true);
        });

        // 🔘 Reports & Settings (Placeholder)
        btnReports.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "📊 Reports module coming soon!", 
                "Under Development", JOptionPane.INFORMATION_MESSAGE));
        
        btnSettings.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "⚙️ Settings module coming soon!", 
                "Under Development", JOptionPane.INFORMATION_MESSAGE));

 // 🔘 Logout
btnLogout.addActionListener(e -> {
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Logout from BloodLink?", "Confirm", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        // ✅ Clear session before logout
        UserSession.clearSession();
        
        this.dispose();
        new LoginForm().setVisible(true);
    }
});
}   
    
    
    
    
    
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RegistrationPanel = new customcontrols.GlassPanel();
        lblTitle = new javax.swing.JLabel();
        logoLabel10 = new customcontrols.LogoLabel();
        txtAge = new CustomComponents.ModernTextField();
        lblPhone = new javax.swing.JLabel();
        txtPhone = new CustomComponents.ModernTextField();
        lblAge1 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        cmbBlood = new javax.swing.JComboBox<>();
        lblBlood1 = new javax.swing.JLabel();
        txtName = new CustomComponents.ModernTextField();
        lblName = new javax.swing.JLabel();
        txtAddress = new CustomComponents.ModernTextField();
        lblAddress = new javax.swing.JLabel();
        btnSave = new CustomComponents.RoundedButton();
        btnClear = new CustomComponents.RoundedButton();
        dateChooser = new com.toedter.calendar.JDateChooser();
        logoLabel11 = new customcontrols.LogoLabel();
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
        setTitle("BloodLink - Register Donor");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        RegistrationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Register New Donor");
        RegistrationPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 420, 40));

        logoLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/register white.png"))); // NOI18N
        logoLabel10.setText("logoLabel2");
        logoLabel10.setPreferredSize(new java.awt.Dimension(10, 20));
        RegistrationPanel.add(logoLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 45, 45));

        txtAge.addActionListener(this::txtAgeActionPerformed);
        RegistrationPanel.add(txtAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, 100, 35));

        lblPhone.setForeground(new java.awt.Color(255, 255, 255));
        lblPhone.setText("Phone:");
        RegistrationPanel.add(lblPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 170, 25));

        txtPhone.addActionListener(this::txtPhoneActionPerformed);
        RegistrationPanel.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 200, 35));

        lblAge1.setForeground(new java.awt.Color(255, 255, 255));
        lblAge1.setText("Age:");
        RegistrationPanel.add(lblAge1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 170, 25));

        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setText("Donaion Date:");
        RegistrationPanel.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 170, 25));

        cmbBlood.setBackground(new java.awt.Color(255, 255, 255));
        cmbBlood.setForeground(new java.awt.Color(0, 0, 0));
        cmbBlood.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" }));
        cmbBlood.addActionListener(this::cmbBloodActionPerformed);
        RegistrationPanel.add(cmbBlood, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 200, 35));

        lblBlood1.setForeground(new java.awt.Color(255, 255, 255));
        lblBlood1.setText("Blood Group:");
        RegistrationPanel.add(lblBlood1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 170, 25));

        txtName.addActionListener(this::txtNameActionPerformed);
        RegistrationPanel.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 300, 35));

        lblName.setForeground(new java.awt.Color(255, 255, 255));
        lblName.setText("Full Name:");
        RegistrationPanel.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 170, 25));

        txtAddress.addActionListener(this::txtAddressActionPerformed);
        RegistrationPanel.add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 420, 35));
        txtAddress.getAccessibleContext().setAccessibleName("");

        lblAddress.setForeground(new java.awt.Color(255, 255, 255));
        lblAddress.setText("Address:");
        RegistrationPanel.add(lblAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 170, 25));

        btnSave.setBackground(new java.awt.Color(11, 168, 117));
        btnSave.setText("Save Donor");
        btnSave.addActionListener(this::btnSaveActionPerformed);
        RegistrationPanel.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 120, -1));

        btnClear.setText("Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);
        RegistrationPanel.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 470, 80, -1));

        dateChooser.setBackground(new java.awt.Color(255, 255, 255));
        RegistrationPanel.add(dateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 200, 35));

        logoLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iconback.png"))); // NOI18N
        logoLabel11.setText("logoLabel10");
        RegistrationPanel.add(logoLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 460, 30, 40));

        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("       Back to Dashboard");
        btnBack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnBack.addActionListener(this::btnBackActionPerformed);
        RegistrationPanel.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 460, 180, 40));

        getContentPane().add(RegistrationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 730, 530));

        glassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, ");
        glassPanel1.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 450, 40));
        glassPanel1.add(lblRoleBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 60, 25));

        lblDateTime.setForeground(new java.awt.Color(255, 255, 255));
        lblDateTime.setText("TIME");
        glassPanel1.add(lblDateTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 170, 25));

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

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void txtAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void cmbBloodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBloodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBloodActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // 1. Collect values
    String name = txtName.getText().trim();
    String ageText = txtAge.getText().trim();
    String bloodGroup = cmbBlood.getSelectedItem().toString();
    String phone = txtPhone.getText().trim();
    String address = txtAddress.getText().trim();
    
    // 2. Get date from JDateChooser
    java.util.Date selectedDate = dateChooser.getDate();
    if (selectedDate == null) {
        JOptionPane.showMessageDialog(this, 
            "⚠️ Please select a donation date!", 
            "Date Required", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
    String donationDate = sqlDate.toString(); // "YYYY-MM-DD"
    
    // 3. Validation
    if (name.isEmpty() || ageText.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "⚠️ Please fill in Name and Age!", 
            "Input Required", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        int age = Integer.parseInt(ageText);
        if (age < 18 || age > 65) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Donor must be between 18 and 65 years old.", 
                "Invalid Age", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 4. Database insert
        java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
        String sql = "INSERT INTO donors (name, age, blood_group, phone, address, donation_date) VALUES (?, ?, ?, ?, ?, ?)";
        java.sql.PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, name);
        pst.setInt(2, age);
        pst.setString(3, bloodGroup);
        pst.setString(4, phone);
        pst.setString(5, address);
        pst.setDate(6, sqlDate); // ← Use sqlDate directly!
        
        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, 
                "✅ Donor registered successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            updateInventory(bloodGroup);
            clearForm();
        }
        
        pst.close();
        con.close();
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "⚠️ Age must be a valid number!", 
            "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "❌ Error: " + e.getMessage(), 
            "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSaveActionPerformed

    
    
    
    
    private void updateInventory(String bloodGroup) {
    try {
        java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
        // Update inventory: +1 unit sa corresponding blood group
        String sql = "UPDATE blood_inventory SET available_units = available_units + 1 WHERE blood_group = ?";
        java.sql.PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, bloodGroup);
        pst.executeUpdate();
        pst.close();
        con.close();
    } catch (Exception e) {
        // Silent fail para hindi ma-crash ang app, print lang sa console
        System.out.println("Inventory update failed: " + e.getMessage());
    }
}
    
    /**
 * Helper: Clear all form fields
 */
private void clearForm() {
    txtName.setText("");
    txtAge.setText("");
    txtPhone.setText("");
    txtAddress.setText("");
    dateChooser.setDate(null);  
    cmbBlood.setSelectedIndex(0);
    txtName.requestFocus(); // Focus back to first field
}
    
   
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    txtName.setText("");
    txtAge.setText("");
    txtPhone.setText("");
    txtAddress.setText("");
   
    cmbBlood.setSelectedIndex(0);
    txtName.requestFocus();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        new DashboardForm().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customcontrols.ModernLabel MainPanel;
    private customcontrols.GlassPanel RegistrationPanel;
    private CustomComponents.GlassPanel SidePanel;
    private CustomComponents.SidebarButton btnBack;
    private CustomComponents.RoundedButton btnClear;
    private CustomComponents.SidebarButton btnDashboard;
    private CustomComponents.SidebarButton btnInventory;
    private CustomComponents.SidebarButton btnLogout;
    private CustomComponents.SidebarButton btnRegister;
    private CustomComponents.SidebarButton btnReports;
    private CustomComponents.RoundedButton btnSave;
    private CustomComponents.SidebarButton btnSettings;
    private CustomComponents.SidebarButton btnViewDonors;
    private javax.swing.JComboBox<String> cmbBlood;
    private com.toedter.calendar.JDateChooser dateChooser;
    private customcontrols.GlassPanel glassPanel1;
    private customcontrols.GlassPanel glassPanel2;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAge1;
    private javax.swing.JLabel lblBlood1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateTime;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private CustomComponents.RoleBadge lblRoleBadge;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcome;
    private customcontrols.LogoLabel logoLabel1;
    private customcontrols.LogoLabel logoLabel10;
    private customcontrols.LogoLabel logoLabel11;
    private customcontrols.LogoLabel logoLabel2;
    private customcontrols.LogoLabel logoLabel3;
    private customcontrols.LogoLabel logoLabel4;
    private customcontrols.LogoLabel logoLabel5;
    private customcontrols.LogoLabel logoLabel6;
    private customcontrols.LogoLabel logoLabel7;
    private customcontrols.LogoLabel logoLabel8;
    private customcontrols.LogoLabel logoLabel9;
    private javax.swing.JPanel pantakipPanel;
    private CustomComponents.ModernTextField txtAddress;
    private CustomComponents.ModernTextField txtAge;
    private CustomComponents.ModernTextField txtName;
    private CustomComponents.ModernTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
