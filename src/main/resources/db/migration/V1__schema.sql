-- === USERS & ROLES ===
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100),
                       enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_roles (
                            user_id INTEGER NOT NULL,
                            role_id INTEGER NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- === COMPANIES ===
CREATE TABLE companies (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           industry VARCHAR(100),
                           address TEXT,
                           website VARCHAR(255),
                           description TEXT
);

-- === CONTACTS ===
CREATE TABLE contacts (
                          id SERIAL PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100),
                          phone VARCHAR(50),
                          position VARCHAR(100),
                          company_id INTEGER,
                          FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
);

-- === LEADS ===
CREATE TABLE leads (
                       id SERIAL PRIMARY KEY,
                       source VARCHAR(50), -- e.g. Email, Website, Phone
                       status VARCHAR(50), -- e.g. New, Contacted, Qualified
                       contact_id INTEGER,
                       company_id INTEGER,
                       FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE SET NULL,
                       FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
);

-- === OPPORTUNITIES ===
CREATE TABLE opportunities (
                               id SERIAL PRIMARY KEY,
                               name VARCHAR(100) NOT NULL,
                               probability INTEGER CHECK (probability BETWEEN 0 AND 100),
                               expected_value DECIMAL(12, 2),
                               stage VARCHAR(50), -- e.g. Negotiation, Proposal, Closed-Won, Closed-Lost
                               lead_id INTEGER,
                               FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE SET NULL
);

-- === ACTIVITIES ===
CREATE TABLE activities (
                            id SERIAL PRIMARY KEY,
                            type VARCHAR(50), -- e.g. Call, Meeting, Note
                            date_time TIMESTAMP NOT NULL,
                            description TEXT,
                            contact_id INTEGER,
                            opportunity_id INTEGER,
                            FOREIGN KEY (contact_id) REFERENCES contacts(id) ON DELETE SET NULL,
                            FOREIGN KEY (opportunity_id) REFERENCES opportunities(id) ON DELETE CASCADE
);
