-- === Roles ===
INSERT INTO roles (name) VALUES
                             ('ROLE_ADMIN'),
                             ('ROLE_USER');

-- === Users ===
INSERT INTO users (email, password, full_name, enabled)
VALUES
    ('admin@acrm.dev', 'admin', 'Admin User', true),
    ('user@acrm.dev', 'user', 'Regular User', true);

-- === User-Role Mapping ===
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE (u.email = 'admin@acrm.dev' AND r.name = 'ROLE_ADMIN')
   OR (u.email = 'user@acrm.dev' AND r.name = 'ROLE_USER');

-- === Companies ===
INSERT INTO companies (name, industry, address, website, description)
VALUES
    ('Acme Inc.', 'Technology', '123 Main St', 'https://acme.com', 'Leading tech company'),
    ('Globex Corp.', 'Finance', '456 Wall St', 'https://globex.com', 'Financial services');

-- === Contacts ===
INSERT INTO contacts (first_name, last_name, email, phone, position, company_id)
VALUES
    ('Ivan', 'Ivanov', 'ivan@example.com', '+380991234567', 'Sales Manager', 1),
    ('Oksana', 'Petrenko', 'oksana@example.com', '+380987654321', 'CTO', 2);

-- === Leads ===
INSERT INTO leads (source, status, contact_id, company_id)
VALUES
    ('Website', 'New', 1, NULL),
    ('Phone', 'Contacted', 2, 2);

-- === Opportunities ===
INSERT INTO opportunities (name, probability, expected_value, stage, lead_id)
VALUES
    ('Enterprise deal', 80, 10000.00, 'Proposal', 1),
    ('Consulting package', 60, 3000.00, 'Negotiation', 2);

-- === Activities ===
INSERT INTO activities (type, date_time, description, contact_id, opportunity_id)
VALUES
    ('Call', NOW(), 'Follow-up call with Ivan', 1, 1),
    ('Meeting', NOW(), 'Strategy session with Oksana', 2, 2);
