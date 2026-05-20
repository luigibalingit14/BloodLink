# COMPROG2 FINAL PROJECT PRESENTATION
## BloodLink - Blood Bank Management System

---

### SLIDE 1
**Title:** FINAL PROJECT IN COMPUTER PROGRAMMING 2 (COMPROG2) & INFORMATION MANAGEMENT (INFOMAN)
**Leader & Group Members Name:**
- [Leader Name]
- [Member Name 1]
- [Member Name 2]
- [Member Name 3]

**Section:** [Your Section Here]
**Name of Professors:** [Professor's Name Here]

---

### SLIDE 2
**TITLE OF THE PROJECT:** BloodLink: Blood Bank Management System
**GROUP/BANK LOGO:** 
*(Insert your BloodLink Logo or Group Logo here)*

---

### SLIDE 3
**INTRODUCTION**
BloodLink is a comprehensive Blood Bank Management System designed to streamline the process of blood donation and inventory management. It provides a user-friendly graphical interface for both Administrators and Staff to manage donor records, track blood inventory levels, and generate insightful reports. The system ensures secure access through role-based authentication and maintains a real-time database to quickly address critical blood shortages.

---

### SLIDE 4
**System Features**
- **Dashboard Overview:** Displays real-time statistics like total donors, available blood units, and critical stock alerts.
- **Role-Based Access Control (RBAC):** Restricts system settings and advanced reports to Admin users only, while Staff can manage day-to-day operations.
- **Donor Registration:** Allows quick input of new donor information including their blood type and medical details.
- **Donor Management (View Donors):** Features search, update, and delete functionalities for existing donor records.
- **Real-Time Inventory Tracking:** Automatically updates blood stock levels upon new donations and highlights critically low blood types.
- **Reports & Analytics:** Generates monthly donation trends and blood distribution summaries, with the ability to export data to CSV.

---

### SLIDE 5
**SCREENSHOTS: Login Form**
*(Insert Screenshot of Login Form)*
**Short Description:** The secure entry point of BloodLink. It verifies user credentials against the database and assigns the correct role (Admin or Staff) for the session.

---

### SLIDE 6
**SCREENSHOTS: Dashboard Overview**
*(Insert Screenshot of Dashboard Form)*
**Short Description:** The central hub showing live updates of total donors, blood units, and critical alerts. It uses a clean, glassmorphism design for modern aesthetics.

---

### SLIDE 7
**SCREENSHOTS: Register Donor**
*(Insert Screenshot of Register Donor Form)*
**Short Description:** The data entry form where staff inputs new donor details. It validates the information before saving it securely to the database.

---

### SLIDE 8
**SCREENSHOTS: View Donors**
*(Insert Screenshot of View Donors Form)*
**Short Description:** Displays all registered donors in a tabular format. It includes a search bar for quick filtering and buttons for updating or deleting records.

---

### SLIDE 9
**SCREENSHOTS: Blood Inventory**
*(Insert Screenshot of Inventory Form)*
**Short Description:** A visual representation of current blood stocks. It uses color-coding (red for critical) to alert staff when specific blood types are running low.

---

### SLIDE 10
**SCREENSHOTS: Reports & Analytics**
*(Insert Screenshot of Reports Form)*
**Short Description:** Provides analytical tables showing monthly donation trends and overall blood distribution. Includes a feature to export these reports to CSV files.

---

### SLIDE 11
**DATABASE INTEGRATION - SQL STATEMENTS USED**

**1. LOGIN (Read)**
```sql
SELECT id, password, role, full_name FROM users WHERE username = ?
```
*Purpose:* Checks if the username exists and retrieves the hashed password and role for authentication.

**2. ADDING NEW CUSTOMER/DONOR (Create)**
```sql
INSERT INTO donors (full_name, age, gender, blood_group, contact_number, email, address) 
VALUES (?, ?, ?, ?, ?, ?, ?)
```
*Purpose:* Saves the newly registered donor's information into the database.

**3. SEARCHING CUSTOMER/DONOR (Read)**
```sql
SELECT * FROM donors WHERE full_name LIKE ? OR blood_group LIKE ?
```
*Purpose:* Filters the donor records based on the name or blood group entered in the search bar.

**4. UPDATING CUSTOMER/DONOR INFORMATION (Update)**
```sql
UPDATE donors SET full_name=?, age=?, gender=?, blood_group=?, contact_number=?, email=?, address=? 
WHERE id=?
```
*Purpose:* Modifies the details of an existing donor based on their unique ID.

**5. DELETING CUSTOMER/DONOR (Delete)**
```sql
DELETE FROM donors WHERE id = ?
```
*Purpose:* Permanently removes a donor record from the database.

**6. DISPLAYING ALL CUSTOMERS/DONORS (Read)**
```sql
SELECT * FROM donors ORDER BY donation_date DESC
```
*Purpose:* Retrieves all donor records to populate the table in the View Donors form.

---

### SLIDE 12
**THANK YOU!**
*Any Questions?*
