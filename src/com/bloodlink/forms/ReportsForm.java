package com.bloodlink.forms;

// ==================== IMPORTS ====================
import com.bloodlink.utils.UserSession;
import com.bloodlink.db.DBConnection;
import CustomComponents.SidebarButton;
import customcontrols.GlassPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;

/**
 * 📊 BLOODLINK - Reports & Analytics Form
 * 
 * Features:
 * ✅ Table-based donation trends & blood distribution
 * ✅ CSV Export functionality
 * ✅ Live digital clock
 * ✅ Role-based access (Admin/Staff)
 * ✅ Glassmorphism UI styling
 * ✅ Proper error handling & resource cleanup
 *
 */
public class ReportsForm extends javax.swing.JFrame {
    
    // ==================== INSTANCE VARIABLES ====================
    private javax.swing.Timer clockTimer;
    private static final Logger logger = Logger.getLogger(ReportsForm.class.getName());
    
    // ==================== CONSTRUCTOR ====================
    /**
     * Creates new form ReportsForm
     */
    public ReportsForm() {
        initComponents();
        
            // Transparency for tblTrends
    jScrollPane4.setOpaque(false);
    jScrollPane4.getViewport().setOpaque(false);
    jScrollPane4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    tblTrends.setOpaque(false);
    tblTrends.setShowGrid(false);
    tblTrends.setForeground(new Color(255, 255, 255));
    
    // Transparency for tblDistribution
    jScrollPane3.setOpaque(false);
    jScrollPane3.getViewport().setOpaque(false);
    jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    tblDistribution.setOpaque(false);
    tblDistribution.setShowGrid(false);
    tblDistribution.setForeground(new Color(255, 255, 255));
        
        
        initCustomComponents();
        setupReportsSidebar();
        setupHeader();
        loadDonationTrends();
        loadBloodDistribution();
    }
    
    /**
     * Initialize custom components & styling
     */
    private void initCustomComponents() {
        // Style datetime label
        styleDateTimeLabel();
        
        // Role-based UI: Hide Reports/Settings for Staff (optional)
        if (UserSession.currentRole != null && 
            UserSession.currentRole.equalsIgnoreCase("Staff")) {
            // Staff can still view reports, so we keep buttons visible
            // But you can hide if needed:
            // btnSettings.setVisible(false);
        }
        
        // Pantakip Panel: Hide for Admin, Show for Staff
        if (UserSession.currentRole != null && 
            UserSession.currentRole.equalsIgnoreCase("Admin")) {
            pantakipPanel.setVisible(false);
        } else {
            pantakipPanel.setVisible(true);
            
            
        }
    }
    
    // ==================== HEADER & CLOCK ====================
    /**
     * Setup header UI with current user session + live clock
     */
    private void setupHeader() {
        // Set welcome message
        lblWelcome.setText(" Welcome, " + UserSession.currentUser + "!");
        
        // Set role badge
        lblRoleBadge.setText(UserSession.currentRole.toUpperCase());
        
        // Color code by role
        if (UserSession.currentRole.equalsIgnoreCase("Admin")) {
            lblRoleBadge.setForeground(new Color(220, 20, 60)); // Crimson
            lblRoleBadge.setBackground(new Color(255, 240, 240));
        } else {
            lblRoleBadge.setForeground(new Color(59, 130, 246)); // Blue
            lblRoleBadge.setBackground(new Color(239, 246, 255));
        }
        
        // Start the live clock
        startClock();
    }
    
    /**
     * Style the datetime label for better visibility
     */
    private void styleDateTimeLabel() {
        if (lblDateTime != null) {
            lblDateTime.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDateTime.setForeground(new Color(200, 200, 220));
            lblDateTime.setHorizontalAlignment(SwingConstants.RIGHT);
            lblDateTime.setOpaque(false);
        }
    }
    
    /**
     * 🕐 Start live digital clock in header
     * Format: "Tue, May 19 • 02:30:45 PM"
     */
    private void startClock() {
        // Stop existing timer first (prevent memory leaks)
        if (clockTimer != null) {
            clockTimer.stop();
        }
        
        clockTimer = new javax.swing.Timer(1000, e -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE, MMM dd • hh:mm:ss a");
                lblDateTime.setText(now.format(fmt));
            } catch (Exception ex) {
                lblDateTime.setText("⏰ --:--:--");
                logger.warning("Clock update error: " + ex.getMessage());
            }
        });
        clockTimer.start();
    }
    
    // ==================== RESOURCE CLEANUP ====================
    /**
     * Clean up resources when form is closed
     * Prevents memory leaks from running timers
     */
    @Override
    public void dispose() {
        if (clockTimer != null) {
            clockTimer.stop();
            clockTimer = null;
        }
        super.dispose();
    }
    
    // ==================== SIDEBAR NAVIGATION ====================
    /**
     * Setup sidebar button actions & active state
     */
    private void setupReportsSidebar() {
        // Set Reports as active
        setSidebarActive(btnReports);
        
        // Navigation actions
        btnDashboard.addActionListener(e -> navigateTo(new DashboardForm()));
        btnRegister.addActionListener(e -> navigateTo(new RegisterDonorForm()));
        btnViewDonors.addActionListener(e -> navigateTo(new ViewDonorsForm()));
        btnInventory.addActionListener(e -> navigateTo(new InventoryForm()));
        btnReports.addActionListener(e -> {}); // Stay on current form
        btnSettings.addActionListener(e -> {
            if ("Admin".equalsIgnoreCase(UserSession.currentRole)) {
                navigateTo(new SettingsForm());
            } else {
                JOptionPane.showMessageDialog(this, 
                    "⛔ Access Denied!\nSettings are for Admins only.", 
                    "Unauthorized", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnLogout.addActionListener(e -> handleLogout());
    }
    
    /**
     * Highlight active sidebar button
     */
    private void setSidebarActive(SidebarButton activeBtn) {
        SidebarButton[] buttons = {btnDashboard, btnRegister, btnViewDonors, 
                                   btnInventory, btnReports, btnSettings};
        for (SidebarButton btn : buttons) {
            if (btn != null) btn.setActive(btn == activeBtn);
        }
    }
    
    /**
     * Helper: Navigate to another form
     */
    private void navigateTo(JFrame nextForm) {
        this.dispose();
        nextForm.setVisible(true);
    }
    
    /**
     * Helper: Handle logout with confirmation
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Logout from BloodLink?", "Confirm Logout", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            UserSession.clearSession();
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }
    


        
        // Style header

    
    // ==================== REPORT 1: DONATION TRENDS ====================
    /**
     * Load monthly donation trends from database
     */
    private void loadDonationTrends() {
        DefaultTableModel model = (DefaultTableModel) tblTrends.getModel();
        model.setRowCount(0); // Clear existing data
        
        try (Connection con = DBConnection.connect()) {
            if (con == null) throw new SQLException("Database connection failed");
            
            String sql = "SELECT MONTHNAME(donation_date) as month, " +
                        "YEAR(donation_date) as year, " +
                        "COUNT(*) as total_donations, " +
                        "GROUP_CONCAT(DISTINCT blood_group ORDER BY blood_group) as blood_types " +
                        "FROM donors " +
                        "GROUP BY YEAR(donation_date), MONTH(donation_date) " +
                        "ORDER BY YEAR(donation_date) DESC, MONTH(donation_date) DESC";
            
            try (PreparedStatement pst = con.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("month") + " " + rs.getString("year"),
                        rs.getInt("total_donations"),
                        formatBloodTypes(rs.getString("blood_types"))
                    };
                    model.addRow(row);
                }
            }
            
            // Add summary row
            try (PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) as total FROM donors");
                 ResultSet rs = pst.executeQuery()) {
                
                if (rs.next()) {
                    model.addRow(new Object[]{
                        "📊 GRAND TOTAL",
                        rs.getInt("total"),
                        "All donations recorded"
                    });
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error loading donation trends: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "❌ Failed to load donation trends:\n" + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            
            // Fallback: Show empty state
            model.addRow(new Object[]{"⚠️", "No data", "Check database connection"});
        }
    }
    
    /**
     * Format blood types string for display
     */
    private String formatBloodTypes(String bloodTypes) {
        if (bloodTypes == null || bloodTypes.isEmpty()) return "-";
        return bloodTypes.replaceAll(",", ", ").toUpperCase();
    }
    
    // ==================== REPORT 2: BLOOD DISTRIBUTION ====================
    /**
     * Load blood group distribution with inventory status
     */
    private void loadBloodDistribution() {
    DefaultTableModel model = (DefaultTableModel) tblDistribution.getModel(); // ← DAPAT tblDistribution
    model.setRowCount(0); // Clear existing data
        
        try (Connection con = DBConnection.connect()) {
            if (con == null) throw new SQLException("Database connection failed");
            
            String sql = "SELECT i.blood_group, " +
                        "COALESCE(COUNT(d.id), 0) as total_donors, " +
                        "i.available_units as current_stock, " +
                        "CASE WHEN i.available_units < 5 THEN '🔴 CRITICAL' " +
                        "WHEN i.available_units < 10 THEN '🟡 LOW' " +
                        "ELSE '🟢 ADEQUATE' END as status " +
                        "FROM blood_inventory i " +
                        "LEFT JOIN donors d ON i.blood_group = d.blood_group " +
                        "GROUP BY i.blood_group, i.available_units " +
                        "ORDER BY i.available_units ASC";
            
            try (PreparedStatement pst = con.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("blood_group"),
                        rs.getInt("total_donors"),
                        rs.getInt("current_stock"),
                        rs.getString("status")
                    };
                    model.addRow(row);
                }
            }
            
            // Add total row
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT SUM(available_units) as total FROM blood_inventory");
                 ResultSet rs = pst.executeQuery()) {
                
                if (rs.next()) {
                    model.addRow(new Object[]{
                        "📦 TOTAL UNITS",
                        "-",
                        rs.getInt("total"),
                        "All blood types"
                    });
                }
            }
            
        } catch (SQLException e) {
            logger.severe("Error loading blood distribution: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "❌ Failed to load distribution:\n" + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            
            model.addRow(new Object[]{"⚠️", "No data", "Check connection", "-"});
        }
    }
    
    // ==================== CSV EXPORT FUNCTIONALITY ====================
    /**
     * Export JTable data to CSV file with proper formatting
     * @param table The JTable to export
     * @param reportName Name for the file and dialog
     */
    private void exportTableToCSV(JTable table, String reportName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export " + reportName + " to CSV");
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        // Auto-generate filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String safeName = reportName.toLowerCase().replaceAll("[^a-z0-9]", "_");
        fileChooser.setSelectedFile(new java.io.File(safeName + "_" + timestamp + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            // Ensure .csv extension
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (FileWriter writer = new FileWriter(fileToSave)) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                
                // Write headers
                for (int col = 0; col < model.getColumnCount(); col++) {
                    writer.write(escapeCSV(model.getColumnName(col)));
                    if (col < model.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
                
                // Write data rows
                for (int row = 0; row < table.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = table.getValueAt(row, col);
                        String cellValue = (value != null) ? value.toString() : "";
                        writer.write(escapeCSV(cellValue));
                        if (col < model.getColumnCount() - 1) writer.write(",");
                    }
                    writer.write("\n");
                }
                
                // Success feedback
                JOptionPane.showMessageDialog(this, 
                    "✅ Export successful!\n\n📁 " + fileToSave.getAbsolutePath(), 
                    "CSV Export Complete", JOptionPane.INFORMATION_MESSAGE);
                
                // Optional: Open containing folder
                int openFolder = JOptionPane.showConfirmDialog(this, 
                    "Open folder containing file?", "Open Folder", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                if (openFolder == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(fileToSave.getParentFile());
                    } catch (IOException ex) {
                        logger.warning("Could not open folder: " + ex.getMessage());
                    }
                }
                
            } catch (IOException e) {
                logger.severe("CSV export failed: " + e.getMessage());
                JOptionPane.showMessageDialog(this, 
                    "❌ Export failed:\n" + e.getMessage(), 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Escape CSV values properly (handle commas, quotes, newlines)
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        
        // If contains special chars, wrap in quotes and escape internal quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    // ==================== BUTTON ACTION HANDLERS ====================
    

    

    
   
    
  
    
 
    
    /**
     * Show subtle refresh notification
     */
    private void showRefreshNotification(String message) {
        // Optional: Use a toast-style notification instead of JOptionPane
        // For now, using lightweight dialog
        JOptionPane.showMessageDialog(this, message, "Refreshed", 
            JOptionPane.INFORMATION_MESSAGE);
    }
  



   



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoLabel10 = new customcontrols.LogoLabel();
        btnBack = new CustomComponents.SidebarButton();
        glassPanel3 = new customcontrols.GlassPanel();
        btnRefreshDist = new CustomComponents.RoundedButton();
        btnRefreshTrends = new CustomComponents.RoundedButton();
        btnExportDist = new CustomComponents.RoundedButton();
        btnExportTrends = new CustomComponents.RoundedButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDistribution = new customcontrols.GlassTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTrends = new customcontrols.GlassTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        logoLabel11 = new customcontrols.LogoLabel();
        logoLabel12 = new customcontrols.LogoLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BloodLink - Reports & Analytics ");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iconback.png"))); // NOI18N
        logoLabel10.setText("logoLabel10");
        getContentPane().add(logoLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 650, 30, 40));

        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("       Back to Dashboard");
        btnBack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnBack.addActionListener(this::btnBackActionPerformed);
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 650, 180, 40));

        glassPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRefreshDist.setText("Refresh");
        btnRefreshDist.addActionListener(this::btnRefreshDistActionPerformed);
        glassPanel3.add(btnRefreshDist, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, -1, -1));

        btnRefreshTrends.setText("Refresh");
        btnRefreshTrends.addActionListener(this::btnRefreshTrendsActionPerformed);
        glassPanel3.add(btnRefreshTrends, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 100, -1, -1));

        btnExportDist.setBackground(new java.awt.Color(0, 153, 51));
        btnExportDist.setText("Export CSV");
        btnExportDist.addActionListener(this::btnExportDistActionPerformed);
        glassPanel3.add(btnExportDist, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 100, -1, -1));

        btnExportTrends.setBackground(new java.awt.Color(0, 153, 51));
        btnExportTrends.setText("Export CSV");
        btnExportTrends.addActionListener(this::btnExportTrendsActionPerformed);
        glassPanel3.add(btnExportTrends, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 100, -1, -1));

        tblDistribution.setForeground(new java.awt.Color(0, 0, 0));
        tblDistribution.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Blood Group", "Registered Donors", "Available Units", "Stock Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDistribution.setSelectionBackground(new java.awt.Color(102, 102, 102));
        tblDistribution.setSelectionForeground(new java.awt.Color(153, 153, 153));
        jScrollPane3.setViewportView(tblDistribution);

        glassPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 670, 380));

        tblTrends.setForeground(new java.awt.Color(0, 0, 0));
        tblTrends.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Month/Year", "Total Donations", "Blood Types"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTrends.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane4.setViewportView(tblTrends);

        glassPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 140, 430, 380));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Donation Summary");
        glassPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 350, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Inventory Status");
        glassPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 350, 50));

        logoLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/duguan inventory.png"))); // NOI18N
        logoLabel11.setText("logoLabel10");
        glassPanel3.add(logoLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 130, 70));

        logoLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bloodplus.png"))); // NOI18N
        logoLabel12.setText("logoLabel10");
        glassPanel3.add(logoLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 30, 100, 50));

        logoLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel13.setText("logoLabel4");
        glassPanel3.add(logoLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, 1040, 600));

        getContentPane().add(glassPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 1150, 530));

        glassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome, ");
        glassPanel1.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 710, 40));
        glassPanel1.add(lblRoleBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 40, 60, 25));

        lblDateTime.setForeground(new java.awt.Color(255, 255, 255));
        lblDateTime.setText("TIME");
        glassPanel1.add(lblDateTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, 170, 25));

        getContentPane().add(glassPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 1150, 100));

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
        getContentPane().add(MainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1420, 700));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    // ==================== BUTTON ACTION HANDLERS ====================
    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        new DashboardForm().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshTrendsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshTrendsActionPerformed
    loadDonationTrends();
    JOptionPane.showMessageDialog(this, "📊 Trends table refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnRefreshTrendsActionPerformed

    private void btnRefreshDistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDistActionPerformed
    loadBloodDistribution();
    JOptionPane.showMessageDialog(this, "🩸 Distribution table refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnRefreshDistActionPerformed

    private void btnExportTrendsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportTrendsActionPerformed
        exportTableToCSV(tblDistribution, "Donation_Trends_Report");
    }//GEN-LAST:event_btnExportTrendsActionPerformed

    private void btnExportDistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportDistActionPerformed
        exportTableToCSV(tblDistribution, "Blood_Distribution_Report");
    }//GEN-LAST:event_btnExportDistActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customcontrols.ModernLabel MainPanel;
    private CustomComponents.GlassPanel SidePanel;
    private CustomComponents.SidebarButton btnBack;
    private CustomComponents.SidebarButton btnDashboard;
    private CustomComponents.RoundedButton btnExportDist;
    private CustomComponents.RoundedButton btnExportTrends;
    private CustomComponents.SidebarButton btnInventory;
    private CustomComponents.SidebarButton btnLogout;
    private CustomComponents.RoundedButton btnRefreshDist;
    private CustomComponents.RoundedButton btnRefreshTrends;
    private CustomComponents.SidebarButton btnRegister;
    private CustomComponents.SidebarButton btnReports;
    private CustomComponents.SidebarButton btnSettings;
    private CustomComponents.SidebarButton btnViewDonors;
    private customcontrols.GlassPanel glassPanel1;
    private customcontrols.GlassPanel glassPanel2;
    private customcontrols.GlassPanel glassPanel3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDateTime;
    private CustomComponents.RoleBadge lblRoleBadge;
    private javax.swing.JLabel lblWelcome;
    private customcontrols.LogoLabel logoLabel1;
    private customcontrols.LogoLabel logoLabel10;
    private customcontrols.LogoLabel logoLabel11;
    private customcontrols.LogoLabel logoLabel12;
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
    private customcontrols.GlassTable tblDistribution;
    private customcontrols.GlassTable tblTrends;
    // End of variables declaration//GEN-END:variables
}
