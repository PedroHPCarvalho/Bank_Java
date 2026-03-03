CREATE TABLE IF NOT EXISTS account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number INT NOT NULL,
    account_agency INT NOT NULL,
    account_holder VARCHAR(255) NOT NULL,
    account_balance DOUBLE NOT NULL,
    account_especial_limit DOUBLE NOT NULL,
    account_especial_limit_default DOUBLE NOT NULL
);