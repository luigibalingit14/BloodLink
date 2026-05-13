package com.bloodlink.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database Connection Handler for BloodLink
 * Handles MySQL connection setup and testing
 */
public class DBConnection {
    
    // Database configuration
    // ⚠️ Update these if your MySQL setup is different
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bloodbank ";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // Empty for default XAMPP
    
    /**
     * Establishes connection to bloodlink database
     * @return Connection object if successful, null if failed
     */
    public static Connection connect() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish and return connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            
        } catch (ClassNotFoundException e) {
            // Driver not found
            System.err.println("❌ MySQL Driver not found!");
            System.err.println("   Solution: Add mysql-connector-j.jar to project Libraries");
            e.printStackTrace();
            return null;
            
        } catch (Exception e) {
            // Connection failed
            System.err.println("❌ Connection failed: " + e.getMessage());
            System.err.println("   URL: " + DB_URL);
            System.err.println("   User: " + DB_USER);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Test method - Run this class to verify database connection
     * Right-click DBConnection.java → Run File
     */
    public static void main(String[] args) {
        System.out.println("🧪 Testing BloodLink database connection...");
        System.out.println("   URL: " + DB_URL);
        
        Connection con = connect();
        
        if (con != null) {
            System.out.println("✅ SUCCESS! Connected to bloodlink database.");
            System.out.println("   Connection: " + con);
            
            // Close connection
            try {
                con.close();
                System.out.println("✅ Connection closed properly.");
            } catch (Exception e) {
                System.err.println("⚠️ Error closing connection: " + e.getMessage());
            }
        } else {
            System.out.println("❌ FAILED! Check error messages above.");
            System.out.println("\n🔍 Common fixes:");
            System.out.println("   1. Is MySQL running in XAMPP?");
            System.out.println("   2. Is database 'bloodlink' created?");
            System.out.println("   3. Is mysql-connector-j.jar added to Libraries?");
            System.out.println("   4. Are DB_USER/DB_PASS correct in this file?");
        }
    }
}