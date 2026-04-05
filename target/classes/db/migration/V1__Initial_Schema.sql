-- H2 Database Schema Migration Script
-- Bagamoyo District Council Management System
-- Migrates from MS Access to H2 Embedded Database

-- Manager Table (System Manager Credentials)
CREATE TABLE IF NOT EXISTS manager (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admin Table (Administrator Accounts)
CREATE TABLE IF NOT EXISTS admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Supervisor Table (Supervisor Accounts)
CREATE TABLE IF NOT EXISTS supervisor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Workers Table (Employee Records)
CREATE TABLE IF NOT EXISTS workers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    check_no VARCHAR(50),
    f_name VARCHAR(100) NOT NULL,
    m_name VARCHAR(100),
    l_name VARCHAR(100) NOT NULL,
    sex VARCHAR(10),
    cheo VARCHAR(150),
    n_mshahara VARCHAR(50),
    tsd VARCHAR(50),
    t_kuzaliwa DATE,
    t_ajira DATE,
    t_kuthibitishwa DATE,
    t_daraja DATE,
    k_elimu VARCHAR(100),
    c_alisoma VARCHAR(200),
    m_alimaliza INT,
    k_kazi_sasa VARCHAR(200),
    k_kazi_awali VARCHAR(200),
    dini VARCHAR(100),
    a_mahali VARCHAR(200),
    mobile VARCHAR(20),
    s_kwanza VARCHAR(100),
    s_pili VARCHAR(100),
    kata VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Department Table
CREATE TABLE IF NOT EXISTS department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- File Table (File Storage)
CREATE TABLE IF NOT EXISTS file_storage (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_content BLOB,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_workers_name ON workers(f_name, l_name);
CREATE INDEX IF NOT EXISTS idx_workers_sex ON workers(sex);
CREATE INDEX IF NOT EXISTS idx_workers_position ON workers(cheo);
CREATE INDEX IF NOT EXISTS idx_workers_education ON workers(k_elimu);
CREATE INDEX IF NOT EXISTS idx_supervisor_username ON supervisor(username);
CREATE INDEX IF NOT EXISTS idx_admin_username ON admin(username);

-- Insert default manager account (password: admin123 - should be changed on first login)
-- Note: In production, passwords should be BCrypt hashed
INSERT INTO manager (username, password) VALUES ('manager', 'admin123');

-- Insert default admin account (password: admin123 - should be changed on first login)
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');
