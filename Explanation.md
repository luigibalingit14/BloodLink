# BloodLink - Simpleng Paliwanag (Para sa Hindi Marunong Mag-Code)

Ito ang simpleng paliwanag kung paano gumagana ang inyong system. Gamitin itong gabay para sa inyong defense.

## Ano ang BloodLink?
Ang BloodLink ay isang system na ginawa para tulungan ang mga Blood Bank na i-record at i-manage ang mga nagdodonate ng dugo at kung ilan pa ang natitirang stock ng dugo.

## Paano Gumagana ang mga Bahagi (Forms) nito?

### 1. LoginForm.java (Ang Pintuan)
- **Ano ito:** Dito nag-la-log in ang mga user. May dalawang uri ng user: Admin at Staff.
- **Paano gumagana:** Kapag nag-type ng username at password, iche-check nito sa database kung tama. Kung tama, papapasukin ka nya sa system.

### 2. DashboardForm.java (Ang Buod)
- **Ano ito:** Ito ang unang makikita pagkatapos mag-login.
- **Paano gumagana:** Binibilang nito lahat ng nasa database (ilang donors, ilang dugo). Ipinapakita rin nito kung may paubos na dugong kailangan pansinin (Critical).

### 3. RegisterDonorForm.java (Para Mag-Add ng Donor)
- **Ano ito:** Dito pinapasok ang impormasyon ng bagong magdodonate.
- **Paano gumagana:** Kapag pinindot ang "Save", kukunin nya lahat ng tinype mo (Pangalan, Blood Type, etc.) at isesave sa table na `donors` sa database. (Ito yung **CREATE** sa CRUD).

### 4. ViewDonorsForm.java (Para Makita at Ma-Edit ang Donors)
- **Ano ito:** Listahan ng lahat ng nag-donate.
- **Paano gumagana:** 
  - Ipinapakita ang lahat ng records (Ito yung **READ**).
  - Pwede kang mag-search gamit ang text box.
  - Pwede mong i-edit ang impormasyon kapag nagkamali at isave ulit (Ito yung **UPDATE**).
  - Pwede mong burahin ang record kung kailangan (Ito yung **DELETE**).

### 5. InventoryForm.java (Ang Stock ng Dugo)
- **Ano ito:** Ipinapakita kung gaano karami ang bawat klase ng dugo (A+, O-, etc.).
- **Paano gumagana:** Kukunin nya ang data sa `blood_inventory` table. Kapag ang stock ay bumaba sa 5, kukulayan nya ng PULA ang text at lalagyan ng "CRITICAL" para mabilis mapansin.

### 6. ReportsForm.java (Ang Analytics)
- **Ano ito:** Ipinapakita ang history at buod ng mga donation.
- **Paano gumagana:** Nagcocompute ito galing sa database para ipakita kung ilang nagdonate ngayong buwan. Pwede rin itong i-save bilang Excel/CSV file (Export) para mai-print.

### 7. SettingsForm.java (Para sa Admin)
- **Ano ito:** Dito pwedeng gumawa ng bagong account (User) at magpalit ng password.
- **Paano gumagana:** Ang "Admin" lang ang pwedeng pumasok dito. Ang "Staff" ay hindi pwedeng mag-add ng bagong account. Dito natin ginagamit ang mga nakatagong restrictions.

## Mga Kagamitan sa Likod ng System:
- **DBConnection.java:** Ito yung "Tulay". Sya ang nagkokonekta sa Java program natin papunta sa MySQL Database. Kapag nasira ang tulay, walang data na papasok o lalabas.
- **UserSession.java:** Ito yung "ID Lace". Kinakabisado nito kung sino ang kasalukuyang nakalog-in para alam ng system kung "Admin" ba sya o "Staff".

## Paano Hanapin ang mga Code Explanations?
Binuksan ko lahat ng `.java` files ninyo at nilagyan ng komento na nagsisimula sa salitang `// [DEFENSE]`. Hanapin nyo lang ang salitang ito sa inyong code editor (gamitin ang Ctrl+F) kapag tinanong kayo ng panelist kung paano ginawa ang isang partikular na part. Nakasulat sa Tagalog/English ang paliwanag bawat linya.
