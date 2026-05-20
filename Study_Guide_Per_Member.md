# 📚 BLOODLINK - STUDY GUIDE PER MEMBER
## Para Pag-aralan ng Bawat Miyembro

---

# 👤 TOLENTINO — LoginForm.java

## Ang Iyong Part: Login System

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano nagla-log in ang mga user sa BloodLink.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Ano ang JFrame?**
> Ang `JFrame` ay ang "bintana" (window) ng programa. Lahat ng aming forms ay `extends JFrame` kaya mayroon silang window.

**2. Paano nakukuha ang text na tinype ng user?**
```java
String username = txtUsername.getText().trim();
// .getText() = kunin ang text
// .trim()    = alisin ang spaces sa simula at dulo
```

**3. Paano nakukuha ang password?**
```java
String password = new String(txtPassword.getPassword());
// .getPassword() = ligtas na paraan ng pagkuha ng password
// Ibinibigay ito bilang char[] kaya kino-convert sa String
```

**4. Paano nagkokonekta sa database?**
```java
java.sql.Connection con = com.bloodlink.db.DBConnection.connect();
// Tinatawag ang connect() method ng DBConnection class
// Kung null ang bumalik = hindi makakakonekta
```

**5. Ang SQL para sa Login:**
```sql
SELECT * FROM users WHERE username = ? AND password = ?
```
> Ang `?` ay placeholder para sa input ng user. Ginagamit ito para sa SQL Injection protection.

**6. Paano nalalaman kung tama ang login?**
```java
if (rs.next()) {
    // rs.next() = TRUE kung may nahanap na matching record
    // ibig sabihin tama ang username at password!
}
```

**7. Ano ang nangyayari pagkatapos ng successful login?**
```java
UserSession.currentUser = fullName;  // Ise-save ang pangalan
UserSession.currentRole = role;       // Ise-save kung Admin o Staff
UserSession.isLoggedIn = true;        // Markahan na naka-login

DashboardForm dashboard = new DashboardForm();
dashboard.setVisible(true);  // Ipakita ang Dashboard
this.dispose();               // Isara ang Login window
```

**8. Paano gumagana ang "Show Password" checkbox?**
```java
if (jCheckBox1.isSelected()) {
    txtPassword.setEchoChar((char) 0); // Nakikita ang password (walang masking)
} else {
    txtPassword.setEchoChar('•');      // Nakatago ulit (bullet points)
}
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Paano gumagana ang login?**
> "Kinukuha po namin ang username at password na tinype ng user, pagkatapos iche-check ito sa database gamit ang SQL SELECT statement. Kung may nahanap na matching record, ise-save namin ang user info sa UserSession at ililipat sa Dashboard."

**Q: Ano ang PreparedStatement at bakit hindi kayo nag-concatenate?**
> "Ang PreparedStatement po ay ginagamit namin para maiwasan ang SQL Injection attack. Imbes na direktang ilagay ang input ng user sa SQL string, gumagamit kami ng '?' placeholder para ligtas."

**Q: Ano ang UserSession?**
> "Ang UserSession po ay isang global class na nagtatago ng impormasyon ng naka-login na user — ang pangalan, role, at status ng login. Ginagamit ito ng lahat ng ibang forms para malaman kung sino ang kasalukuyang naka-login."

---
---

# 👤 VILLANUEVA — DashboardForm.java

## Ang Iyong Part: Dashboard Overview

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano gumagana ang Dashboard — ang home page ng sistema.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Paano nakukuha ang Total Donors?**
```sql
SELECT COUNT(*) as total FROM donors
-- COUNT(*) = bilangan ang lahat ng rows sa donors table
```
```java
lblValueDonors.setText(String.valueOf(rs.getInt("total")));
// String.valueOf() = i-convert ang number sa text para mailagay sa label
```

**2. Paano nakukuha ang Total Blood Units?**
```sql
SELECT SUM(available_units) as total FROM blood_inventory
-- SUM() = isama-samahin ang lahat ng values ng isang column
```

**3. Paano nalalaman kung CRITICAL na ang isang blood type?**
```sql
SELECT blood_group, available_units FROM blood_inventory 
WHERE available_units < 5
-- WHERE available_units < 5 = i-filter ang mga stock na kulang
```
```java
if (criticalCount > 0) {
    lblValueCritical.setForeground(new Color(220, 20, 60)); // PULA
} else {
    lblValueCritical.setForeground(new Color(59, 130, 246)); // ASUL (ok)
}
```

**4. Paano nakukuha ang Recent Donations?**
```sql
SELECT COUNT(*) as recent FROM donors 
WHERE donation_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
-- DATE_SUB(CURDATE(), INTERVAL 7 DAY) = 7 araw na nakalipas hanggang ngayon
```

**5. Ang Role-Based UI - paano nililimitahan ang access ng Staff?**
```java
if (UserSession.currentRole.equalsIgnoreCase("Admin")) {
    pantakipPanel.setVisible(false); // Admin nakikita ang lahat
} else {
    pantakipPanel.setVisible(true);  // Staff ay may hidden buttons
}
```

**6. Paano gumagana ang live clock?**
```java
clockTimer = new javax.swing.Timer(1000, e -> {
    // Tuwing 1000ms (1 segundo), mag-u-update ang clock
    LocalDateTime now = LocalDateTime.now();
    lblDateTime.setText(now.format(fmt));
});
clockTimer.start();
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Ano ang nakikita sa Dashboard at paano kinukuha ang data?**
> "Ang Dashboard po ay nagpapakita ng 4 na statistics: Total Donors gamit ang COUNT(*), Total Blood Units gamit ang SUM(), Critical Blood Types na may stock na mas mababa sa 5, at Recent Donations sa nakaraang 7 araw. Lahat ng datos ay galing sa MySQL database."

**Q: Paano gumagana ang real-time updates?**
> "Kapag binuksan ang Dashboard, awtomatikong tinatawag ang loadDashboardStats() method na nagre-run ng mga SQL queries sa database at ina-update ang mga labels na ipinapakita sa screen."

---
---

# 👤 DE LEON — Introduction + System Features

## Ang Iyong Part: Slide 3 - Introduction at Features

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung ano ang BloodLink at ano ang mga features nito.

---

### 🔑 DAPAT MONG MALAMAN:

**Ang Buong System:**
```
BloodLink
├── LoginForm.java        → Ang pintuan (authentication)
├── DashboardForm.java    → Home page (statistics)
├── RegisterDonorForm.java → Mag-add ng donor (CREATE)
├── ViewDonorsForm.java   → Listahan ng donors (READ, UPDATE, DELETE)
├── InventoryForm.java    → Blood stock (color-coded)
├── ReportsForm.java      → Analytics + CSV export
├── SettingsForm.java     → Admin-only user management
├── DBConnection.java     → Koneksyon sa MySQL database
└── UserSession.java      → Global login state
```

**Ang dalawang uri ng user:**
- **Admin** → May access sa lahat, kasama ang Settings
- **Staff** → Basic operations lang, walang access sa Settings

**Ang CRUD Operations:**
- **C**reate → RegisterDonorForm (INSERT INTO donors)
- **R**ead → ViewDonorsForm (SELECT FROM donors)
- **U**pdate → ViewDonorsForm (UPDATE donors SET...)
- **D**elete → ViewDonorsForm (DELETE FROM donors)

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Ipaliwanag ang inyong sistema.**
> "Ang BloodLink po ay isang Blood Bank Management System na ginawa sa Java Swing. Ang layunin nito ay tulungan ang mga blood bank na mag-track ng donor records at blood inventory. May dalawang uri ng user: Admin na may buong access, at Staff na may limitadong access. Gumagamit kami ng MySQL database para permanent na ma-store ang lahat ng impormasyon."

**Q: Ano ang CRUD?**
> "Ang CRUD po ay Create, Read, Update, at Delete — ito ang apat na pangunahing operasyon sa isang database-driven system. Sa aming programa: Create ay ang pag-register ng bagong donor, Read ay ang pagpapakita ng listahan, Update ay ang pag-edit ng impormasyon, at Delete ay ang pagbura ng record."

---
---

# 👤 JAPITANA — DBConnection.java + UserSession.java + Database Schema

## Ang Iyong Part: Database Architecture (PINAKA-TECHNICAL)

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag ng pinaka-importante at pinaka-technical na bahagi — paano nagkokonekta ang Java sa MySQL, at ang istruktura ng database.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Ano ang JDBC?**
> JDBC = Java Database Connectivity. Ito ang built-in na paraan ng Java para makausap ang database. Para itong universal na translator sa pagitan ng Java at MySQL.

**2. Paano gumagana ang DBConnection.java?**
```java
// HAKBANG 1: I-load ang MySQL driver (translator)
Class.forName("com.mysql.cj.jdbc.Driver");

// HAKBANG 2: Kumonekta gamit ang URL, username, at password
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/bloodbank",  // URL ng database
    "root",   // username (default sa XAMPP)
    ""        // password (blangko sa XAMPP)
);
```

**3. Ang tatlong Tables ng Database:**

| Table | Layunin | Pangunahing Columns |
|-------|---------|---------------------|
| `donors` | Donor records | id, name, age, blood_group, phone, address, donation_date |
| `users` | System accounts | id, username, password, full_name, role |
| `blood_inventory` | Blood stock | id, blood_group, available_units |

**4. Paano nagkokonekta ang tables (Relationships)?**
```
donors.blood_group → blood_inventory.blood_group
(Many donors → One blood type entry)

users.role → Controls access to forms
(One user → One role: Admin or Staff)
```

**5. Ano ang PreparedStatement at bakit hindi nag-concatenate?**
```java
// ❌ HINDI DAPAT (SQL Injection vulnerable):
String sql = "SELECT * FROM users WHERE username = '" + username + "'";

// ✅ DAPAT GAMITIN (PreparedStatement - Protected):
String sql = "SELECT * FROM users WHERE username = ?";
pst.setString(1, username); // Ligtas na inaayos ng driver
```

**6. Paano gumagana ang UserSession?**
```java
// Kapag nag-login:
UserSession.currentUser = "Juan";   // Ise-save ang pangalan
UserSession.currentRole = "Admin";  // Ise-save ang role
UserSession.isLoggedIn = true;      // Flag na naka-login

// Sa lahat ng ibang forms:
if (UserSession.currentRole.equals("Admin")) {
    // Ipakita ang admin buttons
}

// Kapag nag-logout:
UserSession.clearSession(); // I-clear ang lahat
```

**7. Bakit "static" ang mga variables sa UserSession?**
> Ang "static" ay nangangahulugan na IISA lang ang kopya ng variable para sa buong programa. Kahit saang form ka pumunta, isa lang ang UserSession — lahat ng forms ay nagbabasa at nagsusulat sa iisang lugar.

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Paano nagkokonekta ang inyong Java program sa MySQL?**
> "Gumagamit po kami ng JDBC — Java Database Connectivity. Sa aming DBConnection.java, unang ino-load namin ang MySQL JDBC driver gamit ang Class.forName(), pagkatapos kumokonekta kami sa database gamit ang DriverManager.getConnection() na binibigyan ng URL, username, at password."

**Q: Ipaliwanag ang database structure ninyo.**
> "Mayroon po kaming tatlong tables: donors para sa mga donor records, users para sa mga system accounts, at blood_inventory para sa blood stock. Ang donors at blood_inventory ay related sa pamamagitan ng blood_group column."

**Q: Ano ang SQL Injection at paano ninyo ito iniiwasan?**
> "Ang SQL Injection po ay isang uri ng cyber attack kung saan naglalagay ang masamang tao ng malisyosong SQL code sa input fields. Iniiwasan namin ito sa pamamagitan ng PreparedStatement at '?' placeholders. Sa ganitong paraan, ang input ng user ay hindi kailanman maisasama bilang SQL code."

---
---

# 👤 AVILA — RegisterDonorForm.java

## Ang Iyong Part: Register New Donor (CREATE)

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano nagre-register ng bagong donor at paano naitatago ang impormasyon sa database.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Paano kinukuha ang input ng user mula sa form?**
```java
String name = txtName.getText().trim();       // Pangalan
String ageText = txtAge.getText().trim();      // Edad (bilang text pa)
String bloodGroup = cmbBlood.getSelectedItem().toString(); // Blood type mula sa dropdown
String phone = txtPhone.getText().trim();      // Telepono
String address = txtAddress.getText().trim();  // Address
java.util.Date selectedDate = dateChooser.getDate(); // Petsa ng donation
```

**2. Mga Validation (Mga panuntunan bago masave):**
```java
// Panuntunan 1: Bawal blangko ang Name at Age
if (name.isEmpty() || ageText.isEmpty()) {
    JOptionPane.showMessageDialog(this, "⚠️ Please fill in Name and Age!");
    return; // Itigil - huwag ituloy sa database
}

// Panuntunan 2: Dapat 18-65 taong gulang
int age = Integer.parseInt(ageText); // i-convert ang text sa number
if (age < 18 || age > 65) {
    JOptionPane.showMessageDialog(this, "⚠️ Donor must be between 18-65!");
    return;
}
```

**3. Ang SQL para sa pag-save ng bagong donor (CREATE/INSERT):**
```sql
INSERT INTO donors (name, age, blood_group, phone, address, donation_date) 
VALUES (?, ?, ?, ?, ?, ?)
```
```java
java.sql.PreparedStatement pst = con.prepareStatement(sql);
pst.setString(1, name);      // Pangalan
pst.setInt(2, age);          // Edad
pst.setString(3, bloodGroup); // Blood type
pst.setString(4, phone);     // Telepono
pst.setString(5, address);   // Address
pst.setDate(6, sqlDate);     // Petsa ng donation

int rowsAffected = pst.executeUpdate(); // Patakbuhin ang INSERT
// rowsAffected > 0 = matagumpay na naisave
```

**4. Automatic na pag-update ng Inventory pagkatapos mag-register:**
```java
// Pagkatapos masave ang donor, awtomatikong +1 sa blood_inventory
private void updateInventory(String bloodGroup) {
    String sql = "UPDATE blood_inventory SET available_units = available_units + 1 
                  WHERE blood_group = ?";
    // Ibig sabihin: Ang current na units + 1 = bagong halaga
}
```

**5. Paano nililinis ang form pagkatapos mag-save?**
```java
private void clearForm() {
    txtName.setText("");       // Burahin ang Name field
    txtAge.setText("");        // Burahin ang Age field
    txtPhone.setText("");      // Burahin ang Phone field
    txtAddress.setText("");    // Burahin ang Address field
    cmbBlood.setSelectedIndex(0); // I-reset ang Blood Type dropdown
    txtName.requestFocus();    // Ilagay ang cursor sa Name field (ready for next)
}
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Ipaliwanag ang Register Donor feature.**
> "Ang RegisterDonorForm po ay para sa pag-add ng bagong blood donors. Pinapasok ng user ang pangalan, edad, blood type, phone, address, at petsa ng donation. Bago namin ito ise-save, nino-validate muna namin ang impormasyon — dapat 18-65 ang edad at hindi blangko ang mga kinakailangang fields. Pagkatapos masave, ginagamit namin ang SQL INSERT statement para ilagay sa donors table, at awtomatikong nag-a-update din ang blood_inventory (+1 unit)."

**Q: Bakit may validation ng edad?**
> "Para po sa safety at accuracy ng data. Ang blood donation ay may age requirements — dapat 18 years old at pataas at hindi hihigit sa 65. Iniimplementa namin ito sa code gamit ang if-else statement."

---
---

# 👤 DUYANEN — ViewDonorsForm.java (Display + Search)

## Ang Iyong Part: Pagsasalita ng Listahan ng Donors at Search

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano ipinapakita ang lahat ng donors sa JTable at paano gumagana ang Search function.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Ano ang JTable?**
> Ang JTable ay isang visual table component sa Java Swing. Para itong isang Excel sheet na ipinapakita sa loob ng programa. Ang DefaultTableModel ang nagtatago ng data ng JTable.

**2. Paano naglo-load ang lahat ng donors (READ/SELECT):**
```java
private void loadDonorsToTable() {
    // HAKBANG 1: Handa ang table (linisin muna ang luma)
    DefaultTableModel model = (DefaultTableModel) tblDonors.getModel();
    model.setRowCount(0); // Burahin ang lahat ng luma
    
    // HAKBANG 2: SQL - Kunin lahat ng donors, pinaka-bago muna
    String sql = "SELECT id, name, age, blood_group, phone, address, donation_date 
                  FROM donors ORDER BY id DESC";
    
    // HAKBANG 3: I-loop ang bawat resulta at ilagay sa table
    while (rs.next()) {
        Object[] row = {
            rs.getInt("id"),          // Column 0: ID
            rs.getString("name"),      // Column 1: Pangalan
            rs.getInt("age"),          // Column 2: Edad
            rs.getString("blood_group"), // Column 3: Blood type
            rs.getString("phone"),     // Column 4: Telepono
            rs.getString("address"),   // Column 5: Address
            rs.getString("donation_date") // Column 6: Petsa
        };
        model.addRow(row); // Idagdag ang row sa table
    }
}
```

**3. Paano gumagana ang Search (LIKE keyword):**
```sql
SELECT * FROM donors WHERE name LIKE ? OR blood_group LIKE ?
-- LIKE = hinahanap ang similar na text
-- %keyword% = kahit saang posisyon ng salita
```
```java
pst.setString(1, "%" + keyword + "%"); // %Juan% = hanapin lahat ng may "Juan"
pst.setString(2, "%" + keyword + "%"); // %O+% = hanapin lahat ng O+ blood type
```

**4. Paano ina-activate ang Search?**
```java
// Kapag may naka-type sa search box, awtomatikong naghahanap
// Kapag blangko ang search box, ibabalik ang lahat
if (txtSearch.getText().trim().isEmpty()) {
    loadDonorsToTable();  // Ipakita lahat
} else {
    searchDonors();        // Mag-search
}
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Paano ipinapakita ang listahan ng donors?**
> "Gumagamit po kami ng JTable na pinupunan ng DefaultTableModel. Kapag binuksan ang ViewDonorsForm, tinatawag namin ang loadDonorsToTable() method na nagre-run ng SQL SELECT statement para kunin ang lahat ng records mula sa donors table. Gamit ang while loop, idinagdag namin ang bawat record bilang isang row sa JTable."

**Q: Paano gumagana ang search?**
> "Ang search po ay gumagamit ng SQL LIKE keyword kasama ang '%' wildcard character. Ang '%' ay nangangahulugang kahit anong character bago at pagkatapos ng keyword. Kaya halimbawa kapag nag-type ng 'Juan', hahanapin namin ang lahat ng donors na may 'Juan' sa kanilang pangalan o blood type."

---
---

# 👤 SORCOSO — ViewDonorsForm.java (Update + Delete)

## Ang Iyong Part: I-edit at Burahin ang Donor Records

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano ina-update at dine-delete ang donor records.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Paano nalalaman kung aling donor ang pinili?**
```java
int selectedRow = tblDonors.getSelectedRow();
// Nagbabalik ng -1 kung walang pinili
// Nagbabalik ng 0, 1, 2... kung may pinili (index ng row)

if (selectedRow == -1) {
    JOptionPane.showMessageDialog(this, "⚠️ Please select a donor first!");
    return; // Huwag ituloy kung walang pinili
}

// Kukunin ang ID ng napiling donor mula sa table (Column 0)
int id = Integer.parseInt(tblDonors.getValueAt(selectedRow, 0).toString());
String name = tblDonors.getValueAt(selectedRow, 1).toString();
```

**2. Ang SQL para sa pag-update ng donor (UPDATE):**
```sql
UPDATE donors 
SET name=?, age=?, blood_group=?, phone=?, address=? 
WHERE id=?
-- WHERE id=? = i-update lang ang donor na may ganitong ID
-- (para hindi maapektuhan ang ibang records!)
```
```java
pst.setString(1, newName);       // Bagong pangalan
pst.setInt(2, newAge);           // Bagong edad
pst.setString(3, newBloodGroup); // Bagong blood type
pst.setString(4, newPhone);      // Bagong telepono
pst.setString(5, newAddress);    // Bagong address
pst.setInt(6, id);               // ID ng donor na ia-update
```

**3. Ang SQL para sa pagbura ng donor (DELETE):**
```sql
DELETE FROM donors WHERE id = ?
-- Permanenteng inalis ang record na may ganitong ID
```

**4. Ang Confirmation Dialog (Pag-ingat bago mag-delete):**
```java
int confirm = JOptionPane.showConfirmDialog(this, 
    "Delete donor: " + name + "?",  // Tanong
    "Confirm Delete",                // Title
    JOptionPane.YES_NO_OPTION);      // May YES at NO button

if (confirm == JOptionPane.YES_OPTION) {
    // Tanging kapag pinindot ang YES, ipapatakbo ang DELETE
    // SQL DELETE statement here...
    loadDonorsToTable(); // I-refresh ang table pagkatapos
}
```

**5. Bakit kailangan ng i-refresh ang table pagkatapos mag-update o delete?**
> Dahil ang JTable ay hindi awtomatikong nag-a-update kapag nabago ang database. Kailangan nating manu-manong tawagan ang `loadDonorsToTable()` para makuha ulit ang pinakabagong data mula sa database.

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Paano gumagana ang Update feature?**
> "Una pong pipiliin ng user ang isang donor sa table. Kukunin namin ang ID ng napiling row gamit ang getSelectedRow() at getValueAt(). Pagkatapos, ipapakita ang mga input dialogs para sa bagong impormasyon. Kapag natanggap ang bagong data, nagpapatakbo kami ng SQL UPDATE statement na nagbabago ng impormasyon ng donor na may matching ID."

**Q: Bakit may confirmation dialog bago mag-delete?**
> "Para po sa safety. Kapag nag-delete ka ng record, permanente na iyon. Ayaw namin na may mabura ng hindi sinasadya, kaya naglalagay kami ng Yes/No confirmation dialog bago ipatupad ang DELETE SQL statement."

---
---

# 👤 GONZALES — InventoryForm.java

## Ang Iyong Part: Blood Inventory System

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano gumagana ang blood inventory tracking at color-coding system.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Ang SQL para sa pag-load ng inventory:**
```sql
SELECT blood_group, available_units 
FROM blood_inventory 
ORDER BY blood_group ASC
-- ORDER BY blood_group ASC = alphabetical order (A+, A-, AB+, AB-...)
```

**2. Paano nagde-decide kung CRITICAL o NORMAL?**
```java
// Ito ay tinatawag na Ternary Operator (shortcut ng if-else)
String status = (units < 5) ? "CRITICAL" : "NORMAL";
//               ^condition   ^kung true   ^kung false
// Kung bababa sa 5 ang units: status = "CRITICAL"
// Kung hindi: status = "NORMAL"
```

**3. Ang Color-Coding (Paano nagbabago ang kulay ng text):**
```java
tblInventory.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(...) {
        // Tingnan ang status column (Column Index 2)
        Object statusObj = table.getValueAt(row, 2);
        
        if (statusObj.toString().equals("CRITICAL")) {
            c.setForeground(Color.RED);                // PULA ang text
            c.setFont(new Font("Segoe UI", BOLD, 12)); // BOLD pa
        } else {
            c.setForeground(Color.WHITE); // PUTI ang text (normal)
        }
    }
});
// DefaultTableCellRenderer = nagde-decide kung paano mag-display ng bawat cell
```

**4. Paano gumagana ang Search sa Inventory?**
```sql
SELECT blood_group, available_units 
FROM blood_inventory 
WHERE blood_group LIKE ?
ORDER BY blood_group ASC
-- Hahanapin ang blood type na katulad ng tinype ng user
```

**5. Paano napapabago ang inventory kapag nag-register ng donor?**
> Sa RegisterDonorForm.java, pagkatapos masave ang bagong donor, awtomatikong tinatawag ang `updateInventory()` method:
```sql
UPDATE blood_inventory 
SET available_units = available_units + 1 
WHERE blood_group = ?
-- Ang available_units ay nadaragdagan ng 1 awtomatiko!
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Paano gumagana ang CRITICAL alert sa inventory?**
> "Gumagamit po kami ng SQL WHERE clause na nagfi-filter ng blood types na ang stock ay bababa sa 5 units. Sa code, gumagamit kami ng ternary operator para itakda ang status — CRITICAL kung bababa sa 5, NORMAL kung hindi. Pagkatapos, gumagamit kami ng custom DefaultTableCellRenderer para kulayan ng PULA ang mga CRITICAL na rows sa JTable."

---
---

# 👤 BALINGIT — ReportsForm.java

## Ang Iyong Part: Reports & Analytics + CSV Export

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag kung paano gumagana ang analytics at paano na-e-export ang data.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Ang SQL para sa Donation Trends (GROUP BY):**
```sql
SELECT MONTHNAME(donation_date) as month,    -- Pangalan ng buwan
       YEAR(donation_date) as year,            -- Taon
       COUNT(*) as total_donations,            -- Bilang ng nagdonate
       GROUP_CONCAT(DISTINCT blood_group) as blood_types
FROM donors
GROUP BY YEAR(donation_date), MONTH(donation_date)
ORDER BY YEAR(donation_date) DESC, MONTH(donation_date) DESC
```
> `GROUP BY` = Pinagsasama-sama ang mga row na may parehong buwan at taon
> `COUNT(*)` = Binibilang kung ilan ang nasa bawat grupo

**2. Ang SQL para sa Blood Distribution (CASE WHEN + LEFT JOIN):**
```sql
SELECT i.blood_group,
       COUNT(d.id) as total_donors,
       i.available_units as current_stock,
       CASE WHEN i.available_units < 5 THEN '🔴 CRITICAL'
            WHEN i.available_units < 10 THEN '🟡 LOW'
            ELSE '🟢 ADEQUATE' END as status
FROM blood_inventory i
LEFT JOIN donors d ON i.blood_group = d.blood_group
GROUP BY i.blood_group, i.available_units
```
> `CASE WHEN` = parang if-elseif-else sa loob ng SQL
> `LEFT JOIN` = pag-combine ng blood_inventory at donors tables

**3. Paano gumagana ang CSV Export?**
```java
private void exportTableToCSV(JTable table, String reportName) {
    // HAKBANG 1: Magbukas ng File Save dialog
    JFileChooser fileChooser = new JFileChooser();
    
    // HAKBANG 2: Isulat ang column headers (Title ng bawat column)
    for (int col = 0; col < model.getColumnCount(); col++) {
        writer.write(model.getColumnName(col)); // "Blood Group", "Units", "Status"
        writer.write(","); // CSV = Comma Separated Values
    }
    writer.write("\n"); // Next line
    
    // HAKBANG 3: Isulat ang lahat ng data rows
    for (int row = 0; row < table.getRowCount(); row++) {
        for (int col = 0; col < model.getColumnCount(); col++) {
            writer.write(table.getValueAt(row, col).toString());
            writer.write(",");
        }
        writer.write("\n");
    }
}
// FileWriter = ginagamit para magsulat ng text sa isang file
// .csv = format na nabubuksan sa Excel o Google Sheets
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Paano gumagana ang Reports module?**
> "Ang ReportsForm po ay nagpapakita ng dalawang analytical tables. Una ang Donation Trends — gumagamit kami ng GROUP BY SQL clause para pagsama-samahin ang mga donation records per buwan at taon at COUNT() para bilangan kung ilan. Pangalawa ang Blood Distribution — gumagamit ng CASE WHEN SQL para awtomatikong mag-classify ng status (CRITICAL, LOW, ADEQUATE)."

**Q: Paano gumagana ang CSV Export?**
> "Gumagamit po kami ng Java FileWriter class para magsulat ng text file. Una, inilalagay namin ang column headers, pagkatapos ay iloloop ang lahat ng rows ng JTable at sinusulat ang bawat value na may comma na separator. Ang resulting file ay isang .csv na pwedeng buksan sa Microsoft Excel o Google Sheets."

---
---

# 👤 NASOL — SettingsForm.java

## Ang Iyong Part: System Settings (Admin Only)

### Ano ang iyong ipagpapaliwanag?
Ikaw ang magpapaliwanag ng Settings Form — ang pinaka-privileged na bahagi ng sistema.

---

### 🔑 MGA KEY CONCEPTS NA DAPAT MONG MALAMAN:

**1. Paano ino-block ang Staff sa Settings?**
```java
// Sa constructor ng SettingsForm:
if (!"Admin".equalsIgnoreCase(UserSession.currentRole)) {
    // Kung HINDI Admin ang role ng naka-login...
    JOptionPane.showMessageDialog(this, 
        "⛔ Access Denied!\nSystem Settings are for Admins only.");
    this.dispose();                   // Isara ang Settings window
    new DashboardForm().setVisible(true); // Ibalik sa Dashboard
    return; // Huwag ituloy ang pag-execute ng code
}
```

**2. Ang SQL para sa User Management:**
```java
// READ: Kunin lahat ng users
"SELECT id, username, full_name, role FROM users ORDER BY id"

// ADD USER (CREATE):
"INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)"

// DELETE USER:
"DELETE FROM users WHERE id = ?"

// PROTEKSYON: Hindi pwedeng burahin ang "admin"
if ("admin".equalsIgnoreCase(uname)) {
    JOptionPane.showMessageDialog(this, "⛔ Cannot delete main admin account!");
    return; // Huwag ituloy ang DELETE
}
```

**3. Ang SQL para sa Password Change:**
```java
// HAKBANG 1: I-verify muna ang lumang password
"SELECT password FROM users WHERE id = ? AND password = ?"
// Kung walang nahanap = mali ang lumang password

// HAKBANG 2: I-update ang password kapag tama
"UPDATE users SET password = ? WHERE id = ?"
```

**4. Mga Validation sa Password Change:**
```java
// Panuntunan 1: Lahat ng fields dapat may laman
if (oldP.isEmpty() || newP.isEmpty() || confP.isEmpty()) { return; }

// Panuntunan 2: Ang bagong password ay dapat magkatugma
if (!newP.equals(confP)) { // .equals() = compare dalawang String
    JOptionPane.showMessageDialog(this, "⚠️ New passwords do not match!");
    return;
}

// Panuntunan 3: Minimum 6 characters
if (newP.length() < 6) {
    JOptionPane.showMessageDialog(this, "⚠️ Password must be at least 6 characters!");
    return;
}
```

**5. Ang tatlong tabs ng Settings:**
```
tabSettings
├── Tab 1: "User Management"  → tblUsers, btnAddUser, btnDeleteUser
├── Tab 2: "Account"          → txtOldPass, txtNewPass, txtConfirmPass, btnUpdatePass
└── Tab 3: "System Info"      → txtSysInfo (read-only text area)
```

---

### 💬 POSIBLENG TANUNGIN SA IYO:

**Q: Bakit Admin lang ang pwedeng mag-access ng Settings?**
> "Para po sa security. Ang Settings ay naglalaman ng sensitive na operations tulad ng pag-add at pag-delete ng user accounts. Kung pwede ang lahat na mag-access nito, maaaring mag-abuse ng access ang mga hindi awtorisadong tao. Sa aming code, chini-check namin ang UserSession.currentRole sa simula ng SettingsForm — kung hindi 'Admin', awtomatikong isinasara at inibabalik sa Dashboard."

**Q: Paano ninyong iniiwasan na mabura ang pangunahing admin account?**
> "Nagllagay po kami ng special check bago mag-execute ng DELETE statement. Kung ang username ng gustong burahin ay 'admin', hihinto ang pagpapatakbo at magpapakita ng error message. Ito ay isang hardcoded na proteksyon para hindi mawala ang administrative access sa sistema."
