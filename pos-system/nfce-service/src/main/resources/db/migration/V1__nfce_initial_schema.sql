-- V1__nfce_initial_schema.sql

CREATE TABLE IF NOT EXISTS nfces (
    id               BIGSERIAL PRIMARY KEY,
    access_key       VARCHAR(60)   NOT NULL UNIQUE,
    nfce_number      VARCHAR(20),
    series           VARCHAR(10),
    status           VARCHAR(20)   NOT NULL DEFAULT 'PROCESSING',
    sale_code        VARCHAR(30),
    sale_id          BIGINT,
    issuer_cnpj      VARCHAR(20),
    issuer_name      VARCHAR(200),
    customer_document VARCHAR(20),
    customer_name    VARCHAR(100),
    customer_email   VARCHAR(100),
    product_total    NUMERIC(15,2),
    discount_total   NUMERIC(15,2) DEFAULT 0,
    tax_total        NUMERIC(15,2) DEFAULT 0,
    total            NUMERIC(15,2),
    payment_method   VARCHAR(30),
    payment_amount   NUMERIC(15,2),
    qr_code_url      VARCHAR(500),
    qr_code_base64   TEXT,
    xml_content      TEXT,
    protocol         VARCHAR(50),
    danfe_url        TEXT,
    webhook_sent     BOOLEAN DEFAULT FALSE,
    webhook_sent_at  TIMESTAMP,
    issued_at        TIMESTAMP,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS nfce_items (
    id            BIGSERIAL PRIMARY KEY,
    nfce_id       BIGINT NOT NULL REFERENCES nfces(id),
    item_number   INTEGER,
    product_code  VARCHAR(50),
    product_name  VARCHAR(200),
    ncm           VARCHAR(10),
    cfop          VARCHAR(10),
    unit          VARCHAR(10),
    quantity      INTEGER NOT NULL,
    unit_price    NUMERIC(15,2),
    subtotal      NUMERIC(15,2),
    tax_rate      NUMERIC(5,2) DEFAULT 0,
    tax_amount    NUMERIC(15,2) DEFAULT 0
);

CREATE INDEX idx_nfce_access_key ON nfces(access_key);
CREATE INDEX idx_nfce_sale_code  ON nfces(sale_code);
CREATE INDEX idx_nfce_status     ON nfces(status);
CREATE INDEX idx_nfce_items_nfce ON nfce_items(nfce_id);
