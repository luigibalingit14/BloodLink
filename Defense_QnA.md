# 🩸 BloodLink - Possible Defense Questions & Answers
### Para sa COMPROG2 / INFOMAN Final Project Defense

---

## 📌 PART 1: GENERAL QUESTIONS TUNGKOL SA PROJECT

---

**Q1: Ano ang BloodLink at bakit nyo ito ginawa?**

> **Sagot:** Ang BloodLink ay isang Blood Bank Management System na ginawa para tulungan ang mga blood bank na mag-manage ng mga donor records at blood inventory. Ginawa namin ito dahil mahalaga ang dugo sa buhay ng tao, at maraming blood bank ang gumagamit pa rin ng manu-manong proseso. Ang aming system ay nagbibigay ng mas mabilis at mas organisadong paraan ng pag-track ng donor records at blood stock.

---

**Q2: Ano ang mga pangunahing features ng inyong system?**

> **Sagot:** Ang aming system ay may 6 na pangunahing features:
> 1. **Dashboard** – Nagpapakita ng real-time summary ng total donors, blood units, at critical alerts
> 2. **Donor Registration** – Para mag-add ng bagong donor (CREATE)
> 3. **View Donors** – Para makita, mag-search, mag-edit, at mag-delete ng donor records (READ, UPDATE, DELETE)
> 4. **Blood Inventory** – Para makita ang stock ng bawat blood type na may color-coding
> 5. **Reports & Analytics** – Para makita ang monthly donation trends, kasama ang CSV export
> 6. **System Settings** – Para sa user management at password change (Admin only)

---

**Q3: Sino-sino ang mga user ng inyong system?**

> **Sagot:** May dalawang uri ng user ang aming system:
> - **Admin** – May buong access sa lahat ng features, kasama ang Settings at user management
> - **Staff** – Pwede lang mag-register ng donor, mag-view, at mag-manage ng inventory, pero hindi sila pwedeng mag-access ng Settings

---

## 📌 PART 2: TUNGKOL SA DATABASE

---

**Q4: Anong database ang ginamit ninyo? Paano kayo nagkokonekta dito?**

> **Sagot:** Gumamit kami ng **MySQL** bilang aming database at **XAMPP** para patakbuhin ito locally. Para kumonekta mula sa Java papunta sa MySQL, gumamit kami ng **JDBC (Java Database Connectivity)** kasama ang `mysql-connector-j.jar`. Ang koneksyon ay pinamamahalaan ng aming `DBConnection.java` file. Ang laman ng connection string namin ay:
> ```
> jdbc:mysql://localhost:3306/bloodbank
> ```

---

**Q5: Ano ang mga tables sa inyong database?**

> **Sagot:** Ang aming database ay may tatlong pangunahing tables:
> 1. **`donors`** – Naglalaman ng impormasyon ng mga nagdonate (pangalan, blood type, contact, etc.)
> 2. **`users`** – Naglalaman ng mga account sa system (username, password, role)
> 3. **`blood_inventory`** – Naglalaman ng current stock ng bawat blood type (A+, A-, B+, B-, O+, O-, AB+, AB-)

---

**Q6: Ipakita ang SQL statement para sa pag-login. Paano ito gumagana?**

> **Sagot:** Ito ang SQL statement para sa login:
> ```sql
> SELECT id, password, role, full_name FROM users WHERE username = ?
> ```
> Gumagana ito sa ganito: kapag nag-type ang user ng username, hinahanap namin ito sa `users` table. Kukunin namin ang password mula sa database at ikon-compare namin sa tinype ng user. Kung magkapareho, ang role (Admin/Staff) ay ise-save sa `UserSession` para gamitin ng buong system.

---

**Q7: Ano ang PreparedStatement? Bakit nyo ito ginamit?**

> **Sagot:** Ang **PreparedStatement** ay isang secure na paraan ng pagpapadala ng SQL query sa database. Ginamit namin ito para maiwasan ang **SQL Injection Attack** – isang uri ng cyber attack kung saan magtatype ang masamang tao ng malisyosong code sa login form para mag-bypass ng security. Sa `PreparedStatement`, ang `?` placeholder ay nag-iingat na ang input ng user ay hindi maaaring "mapagsamantalahan" bilang code.
>
> *Halimbawa ng SQL Injection na hindi gagana sa aming system:*
> `Username: admin' OR '1'='1` ← Hindi ito gagana dahil protected na tayo.

---

**Q8: Ipakita ang SQL statements para sa CRUD operations (Create, Read, Update, Delete).**

> **Sagot:**
>
> **CREATE (Mag-add ng Donor):**
> ```sql
> INSERT INTO donors (full_name, age, gender, blood_group, contact_number, email, address) 
> VALUES (?, ?, ?, ?, ?, ?, ?)
> ```
>
> **READ (Makita ang Donors):**
> ```sql
> SELECT * FROM donors ORDER BY donation_date DESC
> ```
>
> **UPDATE (I-edit ang Donor):**
> ```sql
> UPDATE donors SET full_name=?, age=?, gender=?, blood_group=?, contact_number=?, email=?, address=? 
> WHERE id=?
> ```
>
> **DELETE (Burahin ang Donor):**
> ```sql
> DELETE FROM donors WHERE id = ?
> ```

---

**Q9: Paano gumagana ang Search function sa View Donors?**

> **Sagot:** Gumagamit kami ng `LIKE` keyword sa SQL para sa search. Kapag nag-type ang user sa search box, hahanapin namin lahat ng donors na may katulad na pangalan o blood type:
> ```sql
> SELECT * FROM donors WHERE full_name LIKE ? OR blood_group LIKE ?
> ```
> Ang `%` sa magkabilang dulo ng keyword ay nangangahulugang "kahit nasa kahit saang posisyon ng salita".

---

**Q10: Paano ninyong alam kung "Critical" na ang isang blood type sa Inventory?**

> **Sagot:** Sa aming `InventoryForm.java`, may kondisyon kami:
> ```sql
> SELECT blood_group, available_units FROM blood_inventory WHERE available_units < 5
> ```
> Kapag ang `available_units` ng isang blood type ay **mas mababa sa 5**, ita-tag namin ito bilang "CRITICAL" at kukukulayan ng **PULA** ang text. Kung hindi, "NORMAL" at **PUTI** ang kulay.

---

## 📌 PART 3: TUNGKOL SA CODE / JAVA

---

**Q11: Ano ang `UserSession.java`? Bakit kailangan ninyo ito?**

> **Sagot:** Ang `UserSession` ay parang "ID card" na hawak ng system habang naka-login ang user. Ino-store nito ang:
> - `currentUser` – Username ng naka-login
> - `currentRole` – Kung "Admin" o "Staff"
> - `isLoggedIn` – Boolean kung naka-login ba
>
> Kailangan namin ito para sa **Role-Based Access Control**. Halimbawa, kapag sinubukan ng Staff na pumasok sa Settings, iche-check ng system ang `UserSession.currentRole`. Kung hindi "Admin", hindi papayagan.

---

**Q12: Ano ang ibig sabihin ng `Role-Based Access Control (RBAC)` sa inyong system?**

> **Sagot:** Ibig sabihin nito, ang access ng bawat user ay depende sa kanyang "role" o tungkulin. Sa aming system:
> - **Admin** – Pwedeng gumamit ng lahat, kasama ang Settings at User Management
> - **Staff** – Hindi pwedeng mag-access ng Settings, hindi pwedeng mag-manage ng accounts
>
> Ito ay isang mahalagang security feature para masiguro na hindi makakapaglaro ng sensitive na data ang mga taong hindi dapat makita ito.

---

**Q13: Ano ang `JFrame` at bakit lahat ng forms ninyo ay nag-eextend dito?**

> **Sagot:** Ang `JFrame` ay isang built-in na class ng Java Swing na nagbibigay ng `window` o `frame` (yung naglo-load na bintana ng program). Lahat ng aming forms ay **extends JFrame** dahil kailangan namin ng graphical window para ipakita ang aming UI. Ang lahat ng buttons, text boxes, at tables ay naka-lagay sa loob ng `JFrame`.

---

**Q14: Ano ang ibig sabihin ng `ResultSet` at `PreparedStatement`?**

> **Sagot:**
> - **`PreparedStatement`** – Ito ang ginagamit para magpadala ng SQL query sa database. Para itong "template ng utos" na may `?` placeholder para sa secure na pagpapadala ng data.
> - **`ResultSet`** – Ito ang "resulta" na natatanggap mula sa database pagkatapos i-execute ang query. Para itong isang talahanayan/table ng mga sagot mula sa database na pinagsasalhan namin gamit ang `while (rs.next())`.

---

**Q15: Bakit may `try-catch` sa inyong mga functions?**

> **Sagot:** Ang `try-catch` ay isang error handling mechanism sa Java. Inilalagay namin ito para kung may problema sa connection sa database (halimbawa, naka-off ang XAMPP, o may typo sa SQL), hindi mag-crash ang buong program. Sa halip, ipapakita lang namin ang isang friendly na error message sa user tulad ng "❌ Error loading donors."

---

## 📌 PART 4: TECHNICAL / DEEP DIVE QUESTIONS

---

**Q16: Paano ninyong naiwasan ang SQL Injection?**

> **Sagot:** Sa pamamagitan ng `PreparedStatement` at `parameterized queries`. Sa halip na direktang i-concatenate ang input ng user sa SQL string (na mapanganib), gumagamit kami ng `?` placeholder at pagkatapos ay ginagamit ang `pst.setString()` para ilagay ang value. Ang MySQL driver na mismo ang mag-hahandle para masiguro na ang input ay hindi maisalin bilang SQL code.

---

**Q17: Paano gumagana ang Reports export to CSV?**

> **Sagot:** Sa `ReportsForm.java`, mayroon kaming `exportTableToCSV()` method. Ang proseso ay:
> 1. Magbubukas ng `JFileChooser` para piliin ang location ng file
> 2. Gagamitin ang `FileWriter` para gumawa ng `.csv` file
> 3. Isusulat muna ang mga column headers (Blood Group, Available Units, etc.)
> 4. Pagkatapos, iloloop namin ang bawat row ng table at isusulat ito sa file na may comma separator
> 5. Kapag tapos, may `JOptionPane` na magpapakita na matagumpay na na-save ang file

---

**Q18: Bakit gumagamit kayo ng `COUNT(*)` at `SUM()` sa Dashboard?**

> **Sagot:**
> - **`COUNT(*)`** – Para mabilang ang lahat ng rows/records. Halimbawa, `SELECT COUNT(*) FROM donors` ay nagbibigay ng total number ng donors.
> - **`SUM(available_units)`** – Para isama-samahin ang lahat ng blood units sa inventory. Nagbibigay ito ng kabuuang stock ng dugo sa lahat ng blood types.
>
> Ginagamit namin ito para sa real-time statistics sa Dashboard.

---

**Q19: Bakit may `dispose()` sa inyong forms?**

> **Sagot:** Ang `dispose()` ay ginagamit para **isara at i-release ang memory** ng isang JFrame. Kapag pupunta ang user mula sa isang form papunta sa isa pa (halimbawa, mula Dashboard papunta sa Register Donor), kina-`dispose()` namin ang lumang form bago ipakita ang bago. Kung hindi namin ito gagawin, magtitipon ng dami-daming bintana sa background na hindi na ginagamit, at magco-consume ito ng maraming memory.

---

**Q20: Paano gumagana ang Live Clock sa header ng bawat form?**

> **Sagot:** Gumagamit kami ng `javax.swing.Timer` na nagtatrigger tuwing **1 segundo (1000 milliseconds)**. Sa loob ng timer, kinukuha namin ang kasalukuyang oras gamit ang `LocalDateTime.now()` at kina-format ito gamit ang `DateTimeFormatter`. Pagkatapos, ino-update ang text ng `lblDateTime` label. Para walang memory leak, isti-stop namin ang timer kapag na-`dispose()` ang form.

---

## 📌 PART 5: KUNG TINANONG KAYO NANG PERSONAL (Gamitin ang inyong sariling salita)

---

**Q21: Anong bahagi ng code ang pinaka-mahirap na gawin?**

> *Possible na sagot:*
> "Para sa akin, ang pinaka-mahirap ay ang pag-implement ng Role-Based Access Control. Kailangan naming tiyakin na ang bawat form ay nagche-check ng `UserSession.currentRole` para sa tamang access restrictions. Nagkaroon din kami ng challenge sa color-coding ng table para sa critical blood types."

---

**Q22: Kung pwede nyo pang pagbutihin ang system, ano ang idadagdag ninyo?**

> *Possible na sagot:*
> "Gusto naming idagdag ang:
> 1. **Password Hashing** – Para mas secure ang pag-store ng passwords (gamit ang BCrypt o SHA-256 imbes na plain text)
> 2. **Email Notifications** – Para awtomatikong mag-alert kapag critical na ang blood type
> 3. **Print Feature** – Para pwedeng mag-print ng donor ID or report directly from the system
> 4. **Graph/Chart** – Para mas visual ang Reports section (gamit ang Chart library)"

---

**Q23: Bakit Java ang pinili ninyong programming language?**

> *Possible na sagot:*
> "Java ang itinuro sa amin sa COMPROG2, at gusto namin ang Java Swing dahil madaling gumawa ng GUI (Graphical User Interface) gamit ito. Java rin ay isang platform-independent language, ibig sabihin, ang program na gawa sa Java ay pwedeng patakbuhin sa kahit anong OS na may installed na Java."

---

**Q24: Paano ninyong tine-test kung gumagana ang inyong system?**

> *Possible na sagot:*
> "Nag-manual test kami. Pinagsamantalahan namin ang lahat ng fields para makita kung may validation. Sinubukan naming:
> - Mag-login gamit ang maling password (dapat hindi pumayag)
> - Mag-add ng donor na may kulang na fields (dapat magpakita ng error)
> - Mag-delete ng record at tingnan kung nawala talaga sa database
> - Mag-access ng Settings gamit ang Staff account (dapat i-deny)
> - Icheck kung tama ang counting sa Dashboard pagkatapos mag-add ng donor"

---

*💡 Tip: Huwag memorize word-for-word. Intindihin lang ang konsepto para mas natural ang sagot mo kapag tinanong ng panelists!*
