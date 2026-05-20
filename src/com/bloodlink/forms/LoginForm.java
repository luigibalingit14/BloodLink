/*
 * =============================================================================
 * FILE: LoginForm.java
 * PARA KAY: TOLENTINO
 * LAYUNIN: Ang "pintuan" ng BloodLink. Dito nagla-log in ang mga gumagamit.
 * =============================================================================
 *
 * SIMPLENG PALIWANAG:
 * Isipin mo ang LoginForm bilang entrance ng isang building.
 * May guard (ang database) na nagche-check kung ikaw ba ay lisensyadong pumasok.
 * Kapag naipasa mo ang username at password mo, papayagan ka at bibigyan ng
 * "ID card" (ang UserSession) para malaman ng buong building kung sino ka.
 *
 * MGA MAHAHALAGANG KONSEPTO DITO:
 * - JFrame: Ang "bintana" (window) ng programa
 * - JTextField / JPasswordField: Mga kahon kung saan nag-iinput ang user
 * - PreparedStatement: Secure na paraan ng pagpapasend ng SQL sa database
 * - UserSession: Ang "ID card" na nagtatago ng impormasyon ng naka-login
 * =============================================================================
 */
package com.bloodlink.forms;
import javax.swing.JOptionPane;
import com.bloodlink.utils.UserSession;  // Ginagamit natin ang UserSession para
                                          // i-save ang info ng naka-login na user

public class LoginForm extends javax.swing.JFrame {
    // Ang Logger ay ginagamit para mag-record ng mga error sa console (para sa debugging)
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginForm.class.getName());

    /**
     * CONSTRUCTOR: Ito ang unang tinatawag kapag ginawa ang LoginForm
     * Para itong "setup" ng form bago pa man ito ipakita sa screen
     */
public LoginForm() {
    initComponents(); // Tinatawag nito ang auto-generated code para i-setup ang lahat ng buttons, textboxes, atbp.
    
    // SPECIAL CASE: Kung may naka-login na (Hindi dapat mangyari normally)
    // Kapag mayroon nang aktibong session, diretso na sa Dashboard
    if (UserSession.isLoggedIn && !UserSession.currentUser.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "ℹ️ You are already logged in!", 
            "Session Active", JOptionPane.INFORMATION_MESSAGE);
        
        // Gumawa ng bagong Dashboard window at ipakita ito
        DashboardForm dashboard = new DashboardForm();
        dashboard.loadUserData(UserSession.currentUser, UserSession.currentRole);
        dashboard.setVisible(true);
        this.dispose(); // Isara ang Login window (para hindi dalawa ang bukas)
        return; // Tigilan na ang pag-execute ng code sa ibaba
    }
}

 
    
    
    
    
    /**
     * initComponents() - AW-GENERATED CODE
     * HUWAG BAGUHIN ITO! Ito ay awtomatikong ginawa ng NetBeans IDE.
     * Naglalaman ito ng lahat ng setup para sa visual na bahagi (buttons, labels, etc.)
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        glassPanel1 = new customcontrols.GlassPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnLogin = new CustomComponents.RoundedButton();
        logoLabel3 = new customcontrols.LogoLabel();
        txtPassword = new CustomComponents.PasswordFieldWithIcon();
        logoLabel2 = new customcontrols.LogoLabel();
        logoLabel1 = new customcontrols.LogoLabel();
        txtUsername = new CustomComponents.TextboxWithIcon();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        logoLabel4 = new customcontrols.LogoLabel();
        modernLabel1 = new customcontrols.ModernLabel();

        setTitle("BloodLink - Login");
        setPreferredSize(new java.awt.Dimension(1000, 700));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        glassPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Username");
        glassPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 60, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Password");
        glassPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 80, -1));

        btnLogin.setText("Login ");
        btnLogin.addActionListener(this::btnLoginActionPerformed);
        glassPanel1.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 260, 40));

        logoLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lock.png"))); // NOI18N
        logoLabel3.setText("logoLabel3");
        glassPanel1.add(logoLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 306, 40, 50));

        txtPassword.setBackground(new java.awt.Color(255, 255, 255));
        txtPassword.addActionListener(this::txtPasswordActionPerformed);
        glassPanel1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 310, 260, -1));

        logoLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profile.png"))); // NOI18N
        logoLabel2.setText("logoLabel2");
        glassPanel1.add(logoLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 40, 70));

        logoLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bloodlink_logo.png"))); // NOI18N
        logoLabel1.setText("logoLabel1");
        glassPanel1.add(logoLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 260, 240));

        txtUsername.setBackground(new java.awt.Color(255, 255, 255));
        txtUsername.setText("Enter your ID");
        txtUsername.addActionListener(this::txtUsernameActionPerformed);
        glassPanel1.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 260, -1));

        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Show Password");
        jCheckBox1.addActionListener(this::jCheckBox1ActionPerformed);
        glassPanel1.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, -1, -1));

        jLabel3.setText("Password");
        glassPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 80, -1));

        getContentPane().add(glassPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 430, 550));

        logoLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backdropicon.png"))); // NOI18N
        logoLabel4.setText("logoLabel4");
        getContentPane().add(logoLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-570, 100, 1250, 800));

        modernLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Screenshot 2026-05-06 232929.png"))); // NOI18N
        getContentPane().add(modernLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        pack();
        setLocationRelativeTo(null); // Ilalagay ang window sa gitna ng screen
    }// </editor-fold>//GEN-END:initComponents

    // Kapag pinindot ang Enter sa Username field — wala pang ginagawa
    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    // Kapag pinindot ang Enter sa Password field — wala pang ginagawa
    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    /**
     * btnLoginActionPerformed - PINAKAMAHALAGANG METHOD SA LOGINFORM
     * Tinatawag ito kapag pinindot ng user ang "Login" button.
     *
     * PROSESO (Step by Step):
     * 1. Kunin ang username at password na tinype ng user
     * 2. I-validate (siguruhin na may laman)
     * 3. Kumonekta sa database
     * 4. Mag-execute ng SQL para hanapin ang user
     * 5. Kung nahanap → mag-login, kung hindi → error message
     */
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        
        // =====================================================================
        // HAKBANG 1: KUNIN ANG INPUT NG USER
        // =====================================================================
        // .getText() ay kumuha ng text mula sa username field
        // .trim() ay nag-aalis ng mga puwang sa simula at dulo (hal: "  admin  " → "admin")
        String username = txtUsername.getText().trim();
        
        // .getPassword() ay ibinibigay ang password bilang char array (mas secure kaysa String)
        // new String(...) ay kino-convert ang char array pabalik sa String para magamit sa SQL
        String password = new String(txtPassword.getPassword());
    
        // =====================================================================
        // HAKBANG 2: BASIC VALIDATION
        // Pag-ingat: Tinitingnan kung may laman ang dalawang field
        // =====================================================================
        if (username.isEmpty() || password.isEmpty()) {
            // .isEmpty() ay bumabalik ng TRUE kung walang laman ang text
            // Kung alinman sa dalawa ay walang laman, ipapakita ang babala at titigil
            JOptionPane.showMessageDialog(this, 
                "⚠️ Please enter both username and password!", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return; // Itigil ang pagpapatuloy ng code (hindi na susunod ang database check)
        }
    
        // =====================================================================
        // HAKBANG 3: DATABASE VALIDATION (Ang pinaka-importanteng bahagi!)
        // =====================================================================
        try {
            // Kumonekta sa MySQL database gamit ang aming DBConnection class
            // Kapag null ang bumalik, hindi makakakonekta sa database
            java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "❌ Cannot connect to database!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // =====================================================================
            // DATABASE INTEGRATION - LOGIN SQL STATEMENT
            // Ang "?" ay placeholder para sa username at password
            // BAKIT HINDI NATIN DIREKTANG ISULAT ANG USERNAME?
            // → Para sa SQL Injection Protection! Ang PreparedStatement ang mag-hahandle.
            // =====================================================================
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            
            // Ilalagay ang actual values sa mga "?" placeholder
            pst.setString(1, username); // Ang 1 ay para sa unang "?" (username)
            pst.setString(2, password); // Ang 2 ay para sa pangalawang "?" (password)
            
            // .executeQuery() ay nagpapatakbo ng SELECT query at nagbabalik ng ResultSet
            // Ang ResultSet ay parang virtual na talahanayan ng mga resulta mula sa database
            java.sql.ResultSet rs = pst.executeQuery();
            
            // =====================================================================
            // HAKBANG 4: SURIIN ANG RESULTA
            // rs.next() ay nagbabalik ng TRUE kung may nahanap na matching record
            // =====================================================================
        if (rs.next()) {
            // MAY NAHANAP! Ang login ay tama.
            
            // Kunin ang full name at role ng nahanap na user
            String fullName = rs.getString("full_name"); // Ang "full_name" ay column name sa database
            String role = rs.getString("role");           // Ang role ay "Admin" o "Staff"
            
            // =====================================================================
            // SAVE SA UserSession — ISIPIN ITO BILANG "ID CARD" NG USER
            // Ang UserSession ay isang global class na pwedeng ma-access sa lahat ng forms
            // Dito sine-save ang impormasyon ng naka-login para malaman ng system
            // =====================================================================
            UserSession.currentUser = fullName;   // Ise-save ang pangalan ng user
            UserSession.currentRole = role;        // Ise-save kung Admin o Staff
            UserSession.isLoggedIn = true;         // Markahan na naka-login na
            
            JOptionPane.showMessageDialog(this, 
                "✅ Login Successful!\nWelcome, " + fullName + " (" + role + ")", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Pumunta na sa Dashboard — ang pangunahing window ng sistema
            DashboardForm dashboard = new DashboardForm();
            dashboard.loadUserData(fullName, role); // Ipasa ang user info sa Dashboard
            dashboard.setVisible(true);             // Ipakita ang Dashboard
            this.dispose();                         // Isara ang Login window
            
        } else {
            // WALANG NAHANAP! — Maling username o password
            JOptionPane.showMessageDialog(this, 
                "🚫 Invalid username or password!", 
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            
            // I-clear ang password field para sa susunod na pagsubok (security practice)
            txtPassword.setText("");
            txtUsername.requestFocus(); // Ibalik ang cursor sa username field
        }
        
            // =====================================================================
            // HAKBANG 5: LINISIN ANG RESOURCES (Mahalaga para maiwasan ang memory leak)
            // =====================================================================
            rs.close();  // Isara ang ResultSet
            pst.close(); // Isara ang PreparedStatement
            con.close(); // Isara ang database connection
        
        } catch (Exception e) {
            // Kapag may unexpected error (hal: database naka-off, wrong SQL syntax)
            JOptionPane.showMessageDialog(this, 
                "❌ System Error: " + e.getMessage(), 
                "Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // I-print ang error details sa console para sa debugging
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * jCheckBox1ActionPerformed — "Show Password" checkbox logic
     * Kapag may tsek ang checkbox, ipapakita ang password bilang plain text.
     * Kapag walang tsek, itatago ito bilang mga bullet points (•••)
     */
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            // isSelected() ay TRUE kapag may tsek ang checkbox
            // setEchoChar((char) 0) ay nagtatanggal ng masking — nakikita na ang password
            txtPassword.setEchoChar((char) 0); // Ipakita ang password
        } else {
            // Ibalik ang masking — mapapakita ulit bilang "•••"
            txtPassword.setEchoChar('•');      // Itago ulit
        }                            
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * main() — Entry Point ng buong programa
     * Ito ang UNANG TUMATAKBO kapag pinapatakbo ang programa.
     * Ine-set up ang visual look ng programa (Nimbus Look and Feel)
     * at pagkatapos ay ipinapakita ang LoginForm.
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel — para magmukhang mas maganda ang UI */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Ipakita ang LoginForm sa screen
        // invokeLater() ay nagbe-bukod ng UI loading sa isang special thread (EDT)
        // para hindi mag-freeze ang programa habang nag-lo-load
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // Lahat ng variables dito ay ang mga visual components ng form
    // Ginawa ng NetBeans IDE ang section na ito — HUWAG BAGUHIN
    private CustomComponents.RoundedButton btnLogin;    // Ang "Login" button
    private customcontrols.GlassPanel glassPanel1;      // Transparent panel sa background
    private javax.swing.JCheckBox jCheckBox1;            // "Show Password" checkbox
    private javax.swing.JLabel jLabel1;                  // Label na "Username"
    private javax.swing.JLabel jLabel2;                  // Label na "Password"
    private javax.swing.JLabel jLabel3;
    private customcontrols.LogoLabel logoLabel1;         // BloodLink logo
    private customcontrols.LogoLabel logoLabel2;         // Profile icon
    private customcontrols.LogoLabel logoLabel3;         // Lock icon
    private customcontrols.LogoLabel logoLabel4;         // Background design
    private customcontrols.ModernLabel modernLabel1;     // Background image
    private CustomComponents.PasswordFieldWithIcon txtPassword;  // Password input field
    private CustomComponents.TextboxWithIcon txtUsername;        // Username input field
    // End of variables declaration//GEN-END:variables
}
