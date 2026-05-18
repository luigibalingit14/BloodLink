package com.bloodlink.utils;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Universal header manager for all forms
 * Handles: Welcome message, Role badge, Live clock
 */
public class HeaderManager {
    
    private Timer clockTimer;
    private JLabel lblWelcome;
    private JLabel lblRoleBadge;
    private JLabel lblDateTime;
    
    public HeaderManager(JLabel welcome, JLabel roleBadge, JLabel dateTime) {
        this.lblWelcome = welcome;
        this.lblRoleBadge = roleBadge;
        this.lblDateTime = dateTime;
    }
    
    /**
     * Setup header with user info
     */
    public void setupHeader(String userName, String userRole) {
        // Set welcome message
        if (lblWelcome != null) {
            lblWelcome.setText(" Welcome, " + userName + "!");
        }
        
        // Set role badge
        if (lblRoleBadge != null) {
            lblRoleBadge.setText(userRole.toUpperCase());
            
            // Color code by role
            if (userRole.equalsIgnoreCase("Admin")) {
                lblRoleBadge.setForeground(new java.awt.Color(220, 20, 60)); // Crimson
                lblRoleBadge.setBackground(new java.awt.Color(255, 240, 240));
            } else {
                lblRoleBadge.setForeground(new java.awt.Color(59, 130, 246)); // Blue
                lblRoleBadge.setBackground(new java.awt.Color(239, 246, 255));
            }
        }
        
        // Start live clock
        startClock();
    }
    
    /**
     * Start live clock timer
     */
    private void startClock() {
        if (clockTimer != null) {
            clockTimer.stop(); // Stop existing timer
        }
        
        clockTimer = new Timer(1000, e -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE, MMM dd • hh:mm:ss a");
                if (lblDateTime != null) {
                    lblDateTime.setText(now.format(fmt));
                }
            } catch (Exception ex) {
                System.err.println("Clock error: " + ex.getMessage());
            }
        });
        
        clockTimer.start();
    }
    
    /**
     * Stop timer (call when form closes)
     */
    public void stopTimer() {
        if (clockTimer != null) {
            clockTimer.stop();
        }
    }
}