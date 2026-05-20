/*
 * =============================================================================
 * FILE: UserSession.java
 * PARA KAY: JAPITANA
 * LAYUNIN: Ang "ID Card" ng naka-login na user. 
 *          Nagtatago ng impormasyon ng user habang gumagamit ng sistema.
 * =============================================================================
 *
 * SIMPLENG PALIWANAG:
 * Isipin mo ang UserSession bilang isang ID card na ibinibigay sa iyo
 * kapag nag-login ka sa sistema.
 * Kahit saan ka pumunta sa loob ng programa (Dashboard, Inventory, Reports),
 * kinu-check ng sistema ang iyong ID card para malaman kung sino ka
 * at kung anong mga bagay ang pinapayagan mong gawin.
 *
 * BAKIT KAILANGAN ITO?
 * Sa Java, ang bawat Form (window) ay magkaibang "object" o bagay.
 * Kapag lumipat ka mula sa LoginForm papunta sa DashboardForm,
 * ang DashboardForm ay hindi automatic na alam kung sino ang nag-login.
 * Ang UserSession ang nagtatago ng impormasyong iyon para sa lahat ng forms.
 *
 * IBIG SABIHIN NG "STATIC":
 * Ang "static" ay nangangahulugan na IISA lang ang kopya ng variable na ito
 * para sa buong programa. Kahit saang form ka pumunta, iisa lang ang UserSession.
 * Parang shared noticeboard na nakikita ng lahat.
 * =============================================================================
 */
package com.bloodlink.utils;

/**
 * Central session manager para sa naka-login na user.
 * Accessible mula sa kahit anong form nang hindi na kailangang magpasa ng parameters.
 * 
 * PAANO GAMITIN:
 *   Mag-access:  UserSession.currentUser   (para malaman ang pangalan)
 *                UserSession.currentRole   (para malaman kung Admin o Staff)
 *   Mag-save:    UserSession.currentUser = "Juan";
 *   Mag-clear:   UserSession.clearSession();
 */
public class UserSession {
    
    // =========================================================================
    // MGA GLOBAL VARIABLES (Static Variables)
    // Ang "static" ay nangangahulugang iisa lang ang halaga nito sa buong programa
    // Kapag binago mo ito sa isang form, babago rin ito sa lahat ng ibang forms
    // =========================================================================
    
    // Ang ID number ng naka-login na user sa database (para sa SQL queries)
    public static int currentUserId = 0;
    
    // Ang pangalan ng naka-login na user (hal: "Juan Dela Cruz")
    // Ito ang ipinapakita sa "Welcome, [name]!" sa header ng bawat form
    public static String currentUser = "";
    
    // Ang tungkulin (role) ng naka-login na user
    // Possible values: "Admin" o "Staff"
    // Ginagamit ito para malaman kung anong features ang pwedeng gamitin
    public static String currentRole = "";
    
    // Boolean flag: TRUE = naka-login, FALSE = hindi pa naka-login
    // Ginagamit ito para suriin kung may aktibong session bago mag-load ng form
    public static boolean isLoggedIn = false;
    
    // =========================================================================
    // METHODS (Mga Aksyon)
    // =========================================================================
    
    /**
     * clearSession() — Tinatawag kapag nag-logout ang user
     * 
     * PALIWANAG:
     * Ito ang "nagbabalik" sa lahat ng variables sa kanilang default na halaga.
     * Para itong sinisibak ang ID card ng user — wala na silang access sa sistema.
     * 
     * KAILAN TINATAWAG:
     * - Kapag pinindot ang "Logout" button sa kahit anong form
     * - Kapag tinatawag: UserSession.clearSession();
     * 
     * PROSESO:
     * 1. I-reset ang currentUserId sa 0 (walang user)
     * 2. I-clear ang currentUser (walang pangalan)
     * 3. I-clear ang currentRole (walang role)
     * 4. Itakda ang isLoggedIn sa false (hindi na naka-login)
     */
    public static void clearSession() {
        // [DEFENSE] Kapag nag log out ang user, buburahin ang nakasave na impormasyon
        // nya para hindi magamit ng susunod na gagamit ng computer
        currentUserId = 0;  // Ibalik sa 0 (walang user ID)
        currentUser = "";   // Burahin ang pangalan
        currentRole = "";   // Burahin ang role
        isLoggedIn = false; // Markahan na hindi na naka-login
    }
    
    /**
     * hasActiveSession() — Nagche-check kung may aktibong login
     * 
     * PALIWANAG:
     * Nagbabalik ng TRUE kung parehong:
     *   1. Ang isLoggedIn ay TRUE  (nakatakda bilang logged in)
     *   2. Ang currentUser ay hindi walang laman (may pangalan)
     * 
     * Ginagamit ito para sa double-checking ng session validity.
     * 
     * HALIMBAWA:
     *   if (UserSession.hasActiveSession()) {
     *       // May naka-login na, pwede nang pumunta sa Dashboard
     *   } else {
     *       // Walang naka-login, ibalik sa Login form
     *   }
     * 
     * @return true kung may aktibong user session, false kung wala
     */
    public static boolean hasActiveSession() {
        // Ang && ay "AND" operator — PAREHONG kondisyon ay dapat TRUE
        return isLoggedIn && !currentUser.isEmpty();
        //     ^^^^^^^^^         ^^^^^^^^^^^^^^^^^
        //     naka-login ba?    may pangalan ba? (! means "NOT empty")
    }
}