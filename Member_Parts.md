# 👥 BloodLink - Member Presentation Parts (UPDATED)
## Group 1 | COMPROG2 & INFOMAN Final Project

---

## 🎯 SINO MAGPEPRESENT NG ANO?

| # | Pangalan | Part / File | Slide | Subject |
|---|----------|------------|-------|---------|
| 1 | **Tolentino** | Login System — LoginForm.java | Login Slide | COMPROG2 + INFOMAN |
| 2 | **Villanueva** | Dashboard Overview — DashboardForm.java | Dashboard Slide | COMPROG2 + INFOMAN |
| 3 | **De Leon** | Introduction + System Features (Slide 3) | Slide 3 | COMPROG2 + INFOMAN |
| 4 | **Japitana** | Database Schema + SQL Statements + ERD | DB Slides | **INFOMAN ONLY** |
| 5 | **Avila** | Register Donor — RegisterDonorForm.java (CREATE) | Register Slide | COMPROG2 + INFOMAN |
| 6 | **Duyanen** | View Donors – Display & Search — READ/SELECT | View Donors Slide | COMPROG2 + INFOMAN |
| 7 | **Sorcoso** | View Donors – Update & Delete — UPDATE/DELETE | CRUD Slide | COMPROG2 + INFOMAN |
| 8 | **Gonzales** | Blood Inventory — InventoryForm.java | Inventory Slide | COMPROG2 + INFOMAN |
| 9 | **Balingit** | Reports & Analytics + CSV Export — ReportsForm.java | Reports Slide | COMPROG2 + INFOMAN |
| 10 | **Nasol** | System Settings (Admin Only) — SettingsForm.java ⚡ | Settings Slide | COMPROG2 + INFOMAN |
| 11 | **Macahilig** | Project Title Slide + Screenshots (Slide 2 & 4–10) | Slides 2, 4-10 | COMPROG2 + INFOMAN |

---

> **📌 NOTE KAY JAPITANA:**
> Ikaw lang ang nag-ta-take ng INFOMAN, kaya ang iyong part ay ang **Database Integration slides** —
> yung mga SQL statements, Database Schema (tables), at ERD (Entity Relationship Diagram).
> Hindi ka kailangang magpaliwanag ng Java code — ang SQL at database design lang ang iyong part.

---

## 📋 DETALYENG PART NG BAWAT ISA

---

### 👤 TOLENTINO — LoginForm.java
**Magpapaliwanag:** Paano gumagana ang Login system

**Sasabihin:**
> "Ako po si Tolentino, at ako ang magpapaliwanag ng Login system ng aming BloodLink.
> Ang LoginForm.java ay ang unang bintana na makikita ng user kapag binuksan ang program.
> Dito nag-iinput ang user ng kanyang Username at Password.
> Ang sistema ay kumokonekta sa database at chini-check kung tama ang information.
> Kapag tama, ide-detect niya kung Admin ba o Staff ang user, at ililipat sa Dashboard."

**Key Points:**
- `txtUsername.getText()` — kunin ang text sa Username box
- SQL: `SELECT * FROM users WHERE username = ? AND password = ?`
- `UserSession.currentRole = role;` — ise-save kung Admin o Staff
- `jCheckBox1` — Show/Hide password

---

### 👤 VILLANUEVA — DashboardForm.java
**Magpapaliwanag:** Paano gumagana ang Dashboard

**Sasabihin:**
> "Ako po si Villanueva, at ipagpapaliwanag ko ang aming Dashboard.
> Ang Dashboard ang unang page na makikita pagkatapos mag-login.
> Ipinapakita nito ang real-time statistics — total donors, blood units, at critical blood types.
> Lahat ng numbers dito ay galing sa database at nag-a-update agad."

**Key Points:**
- `COUNT(*)` — para bilangan ang donors
- `SUM(available_units)` — para sumahin ang blood units
- `WHERE available_units < 5` — para malaman kung critical na
- Live clock gamit ang `javax.swing.Timer`

---

### 👤 DE LEON — Introduction + System Features (Slide 3)
**Magpapaliwanag:** Ano ang BloodLink at ano ang features nito

**Sasabihin:**
> "Ako po si De Leon, at ipagpapaliwanag ko kung ano ang aming sistema.
> Ang BloodLink ay isang Blood Bank Management System na tumutulong sa mga
> blood bank na i-manage ang donor records at blood inventory.
> May role-based access — meaning, may mga features na para lang sa Admin,
> at may mga para sa lahat."

**Key Features:**
1. Dashboard — Real-time statistics
2. Donor Registration — CREATE (INSERT SQL)
3. View Donors — READ, UPDATE, DELETE (CRUD)
4. Blood Inventory — Color-coded stock alerts
5. Reports & Analytics — Monthly trends + CSV export
6. System Settings — Admin-only user management

---

### 👤 JAPITANA — Database Schema + SQL + ERD *(INFOMAN ONLY)*
**Magpapaliwanag:** Ang database structure, SQL statements, at ERD

**Sasabihin:**
> "Ako po si Japitana, at ipagpapaliwanag ko ang database ng aming BloodLink.
> Gumagamit kami ng MySQL database na may tatlong tables:
> ang donors table para sa mga nagdonate, ang users table para sa mga may access sa sistema,
> at ang blood_inventory para sa stock ng dugo.
> Ang lahat ng datos ay permanent na naka-store dito at accessible gamit ang SQL."

**Key Points:**
- **Table 1 - donors:** id, name, age, blood_group, phone, address, donation_date
- **Table 2 - users:** id, username, password, full_name, role (Admin/Staff)
- **Table 3 - blood_inventory:** id, blood_group, available_units
- ERD: donors → blood_inventory via `blood_group`; users → access control via `role`
- Lahat ng SQL statements (LOGIN, INSERT, UPDATE, DELETE, SELECT, SEARCH)

---

### 👤 AVILA — RegisterDonorForm.java (CREATE)
**Magpapaliwanag:** Paano mag-add ng bagong donor

**Sasabihin:**
> "Ako po si Avila, at ipagpapaliwanag ko ang Register Donor form.
> Dito po ina-add ang bagong blood donors. Pinapasok ng user ang pangalan, edad,
> blood type, phone, address, at petsa ng donation.
> Kapag pinindot ang Save, ang information ay sasave sa donors table
> gamit ang INSERT SQL statement at awtomatiko ring ma-a-update ang inventory (+1 unit)."

**Key Points:**
- Validation: edad 18–65, bawal blank fields
- SQL: `INSERT INTO donors (name, age, blood_group, phone, address, donation_date) VALUES (?, ?, ?, ?, ?, ?)`
- `updateInventory(bloodGroup)` — +1 sa blood_inventory

---

### 👤 DUYANEN — ViewDonorsForm.java (READ + Search)
**Magpapaliwanag:** Paano makita ang listahan at mag-search ng donors

**Sasabihin:**
> "Ako po si Duyanen, at ipagpapaliwanag ko kung paano ipinapakita ang listahan ng donors
> at paano gumagana ang search function.
> Kapag binuksan ang View Donors, awtomatikong kukuha ng lahat ng records mula sa database
> at ipapakita sa JTable. Ang Search ay gumagamit ng LIKE SQL keyword."

**Key Points:**
- `SELECT * FROM donors ORDER BY id DESC` — Kunin lahat, pinaka-bago muna
- `WHERE name LIKE ? OR blood_group LIKE ?` — Para sa search
- `%keyword%` — Ang % ay wildcard (kahit saang posisyon)
- `model.setRowCount(0)` → `model.addRow(row)` — Para sa JTable

---

### 👤 SORCOSO — ViewDonorsForm.java (UPDATE + DELETE)
**Magpapaliwanag:** Paano mag-edit at mag-delete ng donor records

**Sasabihin:**
> "Ako po si Sorcoso, at ipagpapaliwanag ko ang Update at Delete functions.
> Kapag pipiliin ng user ang isang donor sa table at pinindot ang Update,
> ipapakita ang information para ma-edit. Para sa Delete, hihingi muna ng confirmation
> bago burahin ang record."

**Key Points:**
- `tblDonors.getSelectedRow()` — Alamin kung aling row ang pinili
- SQL Update: `UPDATE donors SET name=?, age=?, blood_group=?, phone=?, address=? WHERE id=?`
- SQL Delete: `DELETE FROM donors WHERE id = ?`
- `JOptionPane.showConfirmDialog()` — "Are you sure?" bago mag-delete
- `loadDonorsToTable()` — I-refresh ang table pagkatapos

---

### 👤 GONZALES — InventoryForm.java
**Magpapaliwanag:** Paano gumagana ang Blood Inventory system

**Sasabihin:**
> "Ako po si Gonzales, at ipagpapaliwanag ko ang Blood Inventory module.
> Ipinapakita nito ang kasalukuyang stock ng lahat ng 8 blood types.
> Kapag ang stock ay bumaba sa 5 units, awtomatikong magiging PULA ang text
> at matatandaan na CRITICAL na ang blood type na iyon."

**Key Points:**
- `SELECT blood_group, available_units FROM blood_inventory ORDER BY blood_group ASC`
- `String status = (units < 5) ? "CRITICAL" : "NORMAL"` — Ternary operator
- `DefaultTableCellRenderer` — Para sa color-coding ng table rows

---

### 👤 BALINGIT — ReportsForm.java (Reports + CSV Export)
**Magpapaliwanag:** Paano gumagana ang Reports at Analytics

**Sasabihin:**
> "Ako po si Balingit, at ipagpapaliwanag ko ang aming Reports module.
> Nagpapakita ito ng dalawang analytical tables:
> una, ang Monthly Donation Trends — kung ilang nagdonate bawat buwan;
> pangalawa, ang Blood Distribution — overview ng stock ng bawat blood type.
> May Export CSV feature din para ma-save ang report bilang Excel file."

**Key Points:**
- `GROUP BY YEAR(donation_date), MONTH(donation_date)` — per buwan
- `MONTHNAME(donation_date)` — pangalan ng buwan
- `COUNT(*)` — bilang ng donation per grupo
- `CASE WHEN available_units < 5 THEN 'CRITICAL'` — SQL conditional
- `FileWriter` + loop → CSV export; `JFileChooser` → piliin ang save location

---

### 👤 NASOL — SettingsForm.java (Admin Only) ⚡
**Magpapaliwanag:** Paano gumagana ang System Settings — Admin exclusive feature

**Sasabihin:**
> "Ako po si Nasol, at ipagpapaliwanag ko ang System Settings.
> Ito ang pinaka-sensitive na bahagi ng sistema kaya Admin lang ang pwedeng mag-access.
> May tatlong tabs: User Management, Account Security, at System Info.
> Sa User Management, pwedeng mag-add, mag-delete, at mag-view ng mga accounts.
> Sa Account Security, pwede mong baguhin ang iyong password."

**Key Points:**
- `if (!"Admin".equalsIgnoreCase(UserSession.currentRole))` — Admin check, kung hindi admin, isasara
- `SELECT id, username, full_name, role FROM users` — display users
- `INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)` — add user
- `DELETE FROM users WHERE id = ?` — delete user
- `if ("admin".equalsIgnoreCase(uname)) return;` — bawal burahin ang main admin
- `UPDATE users SET password = ? WHERE id = ?` — change password

---

### 👤 MACAHILIG — PPT Slides 2 + Screenshots (Slides 4–10)
**Magpapaliwanag:** Project Title at Screenshots ng bawat form

**Sasabihin:**
> "Ako po si Macahilig. Ipagpapaliwanag ko ang aming proyekto bilang BloodLink
> at ipapakita ang mga screenshots ng bawat form ng aming sistema —
> mula sa Login, Dashboard, Register Donor, hanggang sa Inventory at Reports."

**Key Points:**
- Slide 2: BloodLink title, logo, short description
- Slides 4–10: Screenshots ng bawat form na may maikling paliwanag
- I-demonstrate ang bawat screen habang nagpapaliwanag

---

## 💡 TIPS PARA SA LAHAT:
1. **Basahin ang Study_Guide_Per_Member.md** — May detalyeng paliwanag doon para sa inyong bawat part
2. **Tingnan ang code** — Sa bawat `.java` file, hanapin ang `// [DEFENSE]` comments para sa mga specific na paliwanag
3. **Huwag i-memorize** — Intindihin ang konsepto, natural na magiging sagot
4. **Kay Japitana** — Ang inyong part ay ang database slides lang — hindi kailangan ng Java code explanation
