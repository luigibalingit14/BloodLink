package com.bloodlink.forms;

import com.bloodlink.utils.UserSession;
import com.bloodlink.db.DBConnection;
import CustomComponents.SidebarButton;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SettingsForm extends javax.swing.JFrame {
    private javax.swing.Timer clockTimer;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SettingsForm.class.getName());

    /**
     * Creates new form SettingsForm
     */
public SettingsForm() {
    initComponents();
    
    // ✅ ✅ ✅ ADD THIS: Glassmorphism Transparency for tblUsers & txtSysInfo
    setupGlassStyling();
    
    // 🔐 ADMIN ONLY CHECK
    if (!"Admin".equalsIgnoreCase(UserSession.currentRole)) {
        JOptionPane.showMessageDialog(this, 
            "⛔ Access Denied!\nSystem Settings are for Admins only.", 
            "Unauthorized", JOptionPane.ERROR_MESSAGE);
        this.dispose();
        new DashboardForm().setVisible(true);
        return;
    }
    
    // Hide overlay for Admin
    if (pantakipPanel != null) pantakipPanel.setVisible(false);
    
    setupSettingsSidebar();
    setupHeader();
    loadUsersTable();
    loadSystemInfo();
    
    // Make system info read-only
    if (txtSysInfo != null) txtSysInfo.setEditable(false);
}

/**
 * ✅ NEW: Apply glassmorphism styling to tables & text areas
 */
private void setupGlassStyling() {
    // --- TRANSPARENCY FOR tblUsers (User Management Tab) ---
    // ⚠️ Palitan ang 'jScrollPane1' kung iba ang variable name sa iyong Design View
    if (jScrollPane1 != null) {
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }
    
    if (tblUsers != null) {
        tblUsers.setOpaque(false);
        tblUsers.setShowGrid(false);
        tblUsers.setForeground(new Color(255, 255, 255)); // White text
        tblUsers.setSelectionBackground(new Color(255, 255, 255, 30)); // Subtle highlight
        tblUsers.setSelectionForeground(new Color(255, 255, 255));
        tblUsers.setRowHeight(35); // Comfortable row height
        
        // Make cell renderer transparent
        javax.swing.table.DefaultTableCellRenderer renderer = 
            (javax.swing.table.DefaultTableCellRenderer) tblUsers.getDefaultRenderer(Object.class);
        renderer.setOpaque(false);
        renderer.setForeground(new Color(255, 255, 255));
        
        // Header styling
        if (tblUsers.getTableHeader() != null) {
            tblUsers.getTableHeader().setOpaque(false);
            tblUsers.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            tblUsers.getTableHeader().setForeground(new Color(255, 255, 255));
            tblUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
    }
    
 
    
}

    // ==================== SIDEBAR & HEADER ====================
    private void setSidebarActive(SidebarButton activeBtn) {
        SidebarButton[] btns = {btnDashboard, btnRegister, btnViewDonors, btnInventory, btnReports, btnSettings};
        for (SidebarButton b : btns) if (b != null) b.setActive(b == activeBtn);
    }

    private void setupSettingsSidebar() {
        setSidebarActive(btnSettings);
        btnDashboard.addActionListener(e -> navigateTo(new DashboardForm()));
        btnRegister.addActionListener(e -> navigateTo(new RegisterDonorForm()));
        btnViewDonors.addActionListener(e -> navigateTo(new ViewDonorsForm()));
        btnInventory.addActionListener(e -> navigateTo(new InventoryForm()));
        btnReports.addActionListener(e -> navigateTo(new ReportsForm()));
        btnSettings.addActionListener(e -> {}); // Stay
        
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Logout from BloodLink?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                UserSession.clearSession();
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    private void setupHeader() {
        lblWelcome.setText(" Welcome, " + UserSession.currentUser + "!");
        lblRoleBadge.setText(UserSession.currentRole.toUpperCase());
        lblRoleBadge.setForeground(new Color(220, 20, 60));
        lblRoleBadge.setBackground(new Color(255, 240, 240));
        startClock();
    }

    private void startClock() {
        if (clockTimer != null) clockTimer.stop();
        clockTimer = new javax.swing.Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            lblDateTime.setText(now.format(java.time.format.DateTimeFormatter.ofPattern("EEE, MMM dd • hh:mm:ss a")));
        });
        clockTimer.start();
    }

    private void navigateTo(JFrame next) {
        this.dispose();
        next.setVisible(true);
    }

    @Override
    public void dispose() {
        if (clockTimer != null) clockTimer.stop();
        super.dispose();
    }

    // ==================== USER MANAGEMENT ====================
    private void loadUsersTable() {
        DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
        model.setRowCount(0);
        try (Connection con = DBConnection.connect();
             PreparedStatement pst = con.prepareStatement("SELECT id, username, full_name, role FROM users ORDER BY id");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("username"), rs.getString("full_name"), rs.getString("role")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading users: " + e.getMessage());
        }
    }

    private void addUser() {
        String user = JOptionPane.showInputDialog(this, "Username:");
        String pass = JOptionPane.showInputDialog(this, "Password:");
        String name = JOptionPane.showInputDialog(this, "Full Name:");
        String role = (String) JOptionPane.showInputDialog(this, "Role:", "Select", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Admin", "Staff"}, "Staff");
        
        if (user != null && pass != null && name != null && role != null && !user.trim().isEmpty()) {
            try (Connection con = DBConnection.connect();
                 PreparedStatement pst = con.prepareStatement("INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)")) {
                pst.setString(1, user.trim());
                pst.setString(2, pass.trim());
                pst.setString(3, name.trim());
                pst.setString(4, role);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ User added successfully!");
                loadUsersTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Failed: " + e.getMessage());
            }
        }
    }

    private void deleteUser() {
        int row = tblUsers.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "⚠️ Select a user first!"); return; }
        int id = (int) tblUsers.getValueAt(row, 0);
        String uname = tblUsers.getValueAt(row, 1).toString();
        
        if ("admin".equalsIgnoreCase(uname)) {
            JOptionPane.showMessageDialog(this, "⛔ Cannot delete main admin account!"); return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Delete user '" + uname + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection con = DBConnection.connect();
                 PreparedStatement pst = con.prepareStatement("DELETE FROM users WHERE id = ?")) {
                pst.setInt(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "🗑️ User deleted!");
                loadUsersTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Failed: " + e.getMessage());
            }
        }
    }

    // ==================== CHANGE PASSWORD ====================
    private void updatePassword() {
        String oldP = new String(txtOldPass.getPassword()).trim();
        String newP = new String(txtNewPass.getPassword()).trim();
        String confP = new String(txtConfirmPass.getPassword()).trim();
        
        if (oldP.isEmpty() || newP.isEmpty() || confP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Fill all fields!"); return;
        }
        if (!newP.equals(confP)) {
            JOptionPane.showMessageDialog(this, "⚠️ New passwords do not match!"); return;
        }
        if (newP.length() < 6) {
            JOptionPane.showMessageDialog(this, "⚠️ Password must be at least 6 characters!"); return;
        }
        
        try (Connection con = DBConnection.connect();
             PreparedStatement verify = con.prepareStatement("SELECT password FROM users WHERE id = ? AND password = ?")) {
            verify.setInt(1, getCurrentUserId());
            verify.setString(2, oldP);
            
            if (!verify.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "❌ Incorrect current password!"); return;
            }
            
            try (PreparedStatement update = con.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
                update.setString(1, newP);
                update.setInt(2, getCurrentUserId());
                update.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Password updated successfully!");
                txtOldPass.setText(""); txtNewPass.setText(""); txtConfirmPass.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }
    
    // Helper: Get current user ID from DB
    private int getCurrentUserId() {
        try (Connection con = DBConnection.connect();
             PreparedStatement pst = con.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            pst.setString(1, UserSession.currentUser);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) { e.printStackTrace(); }
        return 1; // Fallback to admin ID if not found
    }

    // ==================== SYSTEM INFO ====================
    private void loadSystemInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("️ SYSTEM INFORMATION\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("📦 Version: BloodLink v1.0\n");
        sb.append("👤 Current User: ").append(UserSession.currentUser).append("\n");
        sb.append("🔐 Role: ").append(UserSession.currentRole).append("\n");
        sb.append("📅 Session Started: ").append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy • hh:mm a"))).append("\n\n");
        
        sb.append("🗄️ DATABASE STATUS\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        try (Connection con = DBConnection.connect()) {
            sb.append("✅ Status: Connected\n");
            PreparedStatement pst1 = con.prepareStatement("SELECT COUNT(*) FROM donors");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) sb.append("👥 Total Donors: ").append(rs1.getInt(1)).append("\n");
            
            PreparedStatement pst2 = con.prepareStatement("SELECT SUM(available_units) FROM blood_inventory");
            ResultSet rs2 = pst2.executeQuery();
            if (rs2.next()) sb.append("🩸 Total Blood Units: ").append(rs2.getInt(1)).append("\n");
            
            sb.append("\n🔒 Security: PreparedStatement (SQL Injection Protected)\n");
            sb.append("🛡️ Access Control: Role-Based (Admin/Staff)\n");
        } catch (SQLException e) {
            sb.append("❌ Status: Disconnected (").append(e.getMessage()).append(")\n");
        }
        
        if (txtSysInfo != null) txtSysInfo.setText(sb.toString());
    }
 

  
   

    // ... NetBeans auto-generated initComponents() stays here ...

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new CustomComponents.SidebarButton();
        logoLabel11 = new customcontrols.LogoLabel();
        tabSettings = new javax.swing.JTabbedPane();
        glassPanel5 = new customcontrols.GlassPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = new customcontrols.GlassTable();
        jLabel5 = new javax.swing.JLabel();
        logoLabel14 = new customcontrols.LogoLabel();
        btnAddUser = new CustomComponents.RoundedButton();
        btnDeleteUser = new CustomComponents.RoundedButton();
        btnRefreshUsers = new CustomComponents.RoundedButton();
        logoLabel12 = new customcontrols.LogoLabel();
        glassPanel6 = new customcontrols.GlassPanel();
        logoLabel16 = new customcontrols.LogoLabel();
        logoLabel18 = new customcontrols.LogoLabel();
        logoLabel17 = new customcontrols.LogoLabel();
        txtOldPass = new CustomComponents.PasswordFieldWithIcon();
        txtNewPass = new CustomComponents.PasswordFieldWithIcon();
        txtConfirmPass = new CustomComponents.PasswordFieldWithIcon();
        btnUpdatePass = new CustomComponents.RoundedButton();
        logoLabel10 = new customcontrols.LogoLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        logoLabel15 = new customcontrols.LogoLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        glassPanel7 = new customcontrols.GlassPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSysInfo = new javax.swing.JTextArea();
        logoLabel13 = new customcontrols.LogoLabel();
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
        setTitle("BloodLink - Settings ");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("       Back to Dashboard");
        btnBack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnBack.addActionListener(this::btnBackActionPerformed);
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 650, 180, 40));

        logoLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iconback.png"))); // NOI18N
        logoLabel11.setText("logoLabel10");
        getContentPane().add(logoLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 650, 30, 40));

        glassPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Username", "Full Name", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblUsers);

        glassPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 700, 360));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Manage System Users");
        glassPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        logoLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel14.setText("logoLabel4");
        glassPanel5.add(logoLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 1010, 600));

        btnAddUser.setBackground(new java.awt.Color(0, 102, 0));
        btnAddUser.setText("Add User");
        btnAddUser.addActionListener(this::btnAddUserActionPerformed);
        glassPanel5.add(btnAddUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, -1, -1));

        btnDeleteUser.setText("Delete User");
        btnDeleteUser.addActionListener(this::btnDeleteUserActionPerformed);
        glassPanel5.add(btnDeleteUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 460, -1, -1));

        btnRefreshUsers.setBackground(new java.awt.Color(255, 102, 0));
        btnRefreshUsers.setText("Refresh");
        btnRefreshUsers.addActionListener(this::btnRefreshUsersActionPerformed);
        glassPanel5.add(btnRefreshUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 460, -1, -1));

        logoLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/users icon.png"))); // NOI18N
        logoLabel12.setText("logoLabel12");
        glassPanel5.add(logoLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -20, 160, 140));

        tabSettings.addTab("User Management", glassPanel5);

        glassPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/susi.png"))); // NOI18N
        logoLabel16.setText("logoLabel3");
        glassPanel6.add(logoLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 30, 20));

        logoLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/confirm.png"))); // NOI18N
        logoLabel18.setText("logoLabel3");
        glassPanel6.add(logoLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 40, 60));

        logoLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/may bago na.png"))); // NOI18N
        logoLabel17.setText("logoLabel3");
        glassPanel6.add(logoLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 40, 40));

        txtOldPass.setForeground(new java.awt.Color(0, 0, 0));
        glassPanel6.add(txtOldPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 350, -1));

        txtNewPass.setForeground(new java.awt.Color(0, 0, 0));
        txtNewPass.addActionListener(this::txtNewPassActionPerformed);
        glassPanel6.add(txtNewPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 350, -1));

        txtConfirmPass.setForeground(new java.awt.Color(0, 0, 0));
        glassPanel6.add(txtConfirmPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, 350, -1));

        btnUpdatePass.setText("Update Password");
        btnUpdatePass.addActionListener(this::btnUpdatePassActionPerformed);
        glassPanel6.add(btnUpdatePass, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 390, 300, 46));

        logoLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/passchange icon.png"))); // NOI18N
        logoLabel10.setText("logoLabel10");
        glassPanel6.add(logoLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, -10, 170, 170));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Confirm New Password");
        glassPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 130, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Account Security");
        glassPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, -1));

        logoLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel15.setText("logoLabel4");
        glassPanel6.add(logoLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 1010, 600));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Current Password");
        glassPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 130, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("New Password");
        glassPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 130, -1));

        tabSettings.addTab("Account", glassPanel6);

        glassPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSysInfo.setColumns(20);
        txtSysInfo.setRows(5);
        jScrollPane2.setViewportView(txtSysInfo);

        glassPanel7.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 685, 482));

        logoLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel13.setText("logoLabel4");
        glassPanel7.add(logoLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 1010, 600));

        tabSettings.addTab("System Info", glassPanel7);

        getContentPane().add(tabSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 720, 550));

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
        btnSettings.addActionListener(this::btnSettingsActionPerformed);
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

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettingsActionPerformed
    // ==================== BUTTON HANDLERS ====================
    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        addUser();
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void btnDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteUserActionPerformed
        deleteUser();
    }//GEN-LAST:event_btnDeleteUserActionPerformed

    private void btnRefreshUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshUsersActionPerformed
        loadUsersTable();
    }//GEN-LAST:event_btnRefreshUsersActionPerformed

    private void btnUpdatePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePassActionPerformed
        updatePassword();
    }//GEN-LAST:event_btnUpdatePassActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        new DashboardForm().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void txtNewPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNewPassActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customcontrols.ModernLabel MainPanel;
    private CustomComponents.GlassPanel SidePanel;
    private CustomComponents.RoundedButton btnAddUser;
    private CustomComponents.SidebarButton btnBack;
    private CustomComponents.SidebarButton btnDashboard;
    private CustomComponents.RoundedButton btnDeleteUser;
    private CustomComponents.SidebarButton btnInventory;
    private CustomComponents.SidebarButton btnLogout;
    private CustomComponents.RoundedButton btnRefreshUsers;
    private CustomComponents.SidebarButton btnRegister;
    private CustomComponents.SidebarButton btnReports;
    private CustomComponents.SidebarButton btnSettings;
    private CustomComponents.RoundedButton btnUpdatePass;
    private CustomComponents.SidebarButton btnViewDonors;
    private customcontrols.GlassPanel glassPanel1;
    private customcontrols.GlassPanel glassPanel2;
    private customcontrols.GlassPanel glassPanel5;
    private customcontrols.GlassPanel glassPanel6;
    private customcontrols.GlassPanel glassPanel7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDateTime;
    private CustomComponents.RoleBadge lblRoleBadge;
    private javax.swing.JLabel lblWelcome;
    private customcontrols.LogoLabel logoLabel1;
    private customcontrols.LogoLabel logoLabel10;
    private customcontrols.LogoLabel logoLabel11;
    private customcontrols.LogoLabel logoLabel12;
    private customcontrols.LogoLabel logoLabel13;
    private customcontrols.LogoLabel logoLabel14;
    private customcontrols.LogoLabel logoLabel15;
    private customcontrols.LogoLabel logoLabel16;
    private customcontrols.LogoLabel logoLabel17;
    private customcontrols.LogoLabel logoLabel18;
    private customcontrols.LogoLabel logoLabel2;
    private customcontrols.LogoLabel logoLabel3;
    private customcontrols.LogoLabel logoLabel4;
    private customcontrols.LogoLabel logoLabel5;
    private customcontrols.LogoLabel logoLabel6;
    private customcontrols.LogoLabel logoLabel7;
    private customcontrols.LogoLabel logoLabel8;
    private customcontrols.LogoLabel logoLabel9;
    private javax.swing.JPanel pantakipPanel;
    private javax.swing.JTabbedPane tabSettings;
    private customcontrols.GlassTable tblUsers;
    private CustomComponents.PasswordFieldWithIcon txtConfirmPass;
    private CustomComponents.PasswordFieldWithIcon txtNewPass;
    private CustomComponents.PasswordFieldWithIcon txtOldPass;
    private javax.swing.JTextArea txtSysInfo;
    // End of variables declaration//GEN-END:variables
}
