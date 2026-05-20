-- 🗑️ DROP ALL TABLES FIRST
DROP TABLE IF EXISTS donors;
DROP TABLE IF EXISTS blood_inventory;
DROP TABLE IF EXISTS blood_stock;
DROP TABLE IF EXISTS users;

-- ✅ RECREATE WITH CORRECT STRUCTURE

-- 1. USERS TABLE
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100),
    role ENUM('Admin', 'Staff') DEFAULT 'Staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. DONORS TABLE
CREATE TABLE donors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    phone VARCHAR(15),
    address VARCHAR(200),
    donation_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from donors;

-- Load All Donors (View Donors)
SELECT id, name, age, blood_group, phone, address, donation_date 
FROM donors 
ORDER BY id DESC;

SELECT COUNT(*) as total 
FROM donors;

-- Get Recent Donations (Last 7 Days)
SELECT COUNT(*) as recent 
FROM donors 
WHERE donation_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);

-- 3. BLOOD_INVENTORY TABLE
CREATE TABLE blood_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    blood_group ENUM('A+','A-','B+','B-','O+','O-','AB+','AB-') UNIQUE NOT NULL,
    available_units INT DEFAULT 0
);
-- Count Critical Blood Types (Low Stock Alert)
SELECT COUNT(*) as critical 
FROM blood_inventory 
WHERE available_units < 5;

-- Load All Blood Inventory
SELECT blood_group, available_units 
FROM blood_inventory 
ORDER BY blood_group ASC;

SELECT SUM(available_units) as total FROM blood_inventory;


-- 📦 INSERT DEFAULT DATA

-- Admin account
INSERT INTO users (username, password, full_name, role) 
VALUES ('Luigi', 'bloodlink123', 'Luigi Balingit', 'Staff');


-- Admin account
INSERT INTO users (username, password, full_name, role) 
VALUES ('admin', 'bloodlink123', 'System Administrator', 'Admin');

-- Blood inventory initial stock
INSERT INTO blood_inventory (blood_group, available_units) VALUES
('A+', 10), ('A-', 5), ('B+', 8), ('B-', 3),
('O+', 15), ('O-', 2), ('AB+', 6), ('AB-', 1);


-- Check all tables exist
SHOW TABLES;

-- Check users table structure
DESCRIBE users;

-- Check donors table structure
DESCRIBE donors;

-- Check blood_inventory
SELECT * FROM blood_inventory;

SELECT * FROM users



