package com.bloodlink.utils;

/**
 * Central session manager for logged-in user info
 * Accessible from any form without passing parameters
 */
public class UserSession {
    // User info
    public static int currentUserId = 0;
    public static String currentUser = "";
    public static String currentRole = "";
    
    // Login status flag
    public static boolean isLoggedIn = false;
    
    /**
     * Clear all session data (call on logout)
     */
    public static void clearSession() {
        currentUserId = 0;
        currentUser = "";
        currentRole = "";
        isLoggedIn = false;
    }
    
    /**
     * Check if user is logged in (helper method)
     */
    public static boolean hasActiveSession() {
        return isLoggedIn && !currentUser.isEmpty();
    }
}