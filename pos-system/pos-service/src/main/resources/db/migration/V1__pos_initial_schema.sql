-- V1__pos_initial_schema.sql

CREATE TABLE IF NOT EXISTS products (
    id             BIGSERIAL PRIMARY KEY,
    code           VARCHAR(50)  NOT NULL UNIQUE,
    barcode        VARCHAR(20)  UNIQUE,
    name           VARCHAR(200) NOT NULL,
    description    VARCHAR(500),
    ncm            VARCHAR(10),
    cfop           VARCHAR(10),
    unit           VARCHAR(10)  DEFAULT 'UN',
    price          NUMERIC(15,2) NOT NULL,
    tax_rate       NUMERIC(5,2)  DEFAULT 0.00,
    stock_quantity INTEGER       DEFAULT 0,
    status         VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    category       VARCHAR(100),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sales (
    id               BIGSERIAL PRIMARY KEY,
    sale_code        VARCHAR(20)   NOT NULL UNIQUE,
    status           VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    subtotal         NUMERIC(15,2) NOT NULL,
    discount         NUMERIC(15,2) NOT NULL DEFAULT 0,
    total            NUMERIC(15,2) NOT NULL,
    payment_method   VARCHAR(30)   NOT NULL,
    customer_email   VARCHAR(100),
    customer_document VARCHAR(20),
    customer_name    VARCHAR(100),
    terminal_id      VARCHAR(50),
    operator_id      VARCHAR(50),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sale_items (
    id             BIGSERIAL PRIMARY KEY,
    sale_id        BIGINT NOT NULL REFERENCES sales(id),
    product_id     BIGINT,
    product_code   VARCHAR(50),
    product_name   VARCHAR(200),
    ncm            VARCHAR(10),
    cfop           VARCHAR(10),
    unit           VARCHAR(10),
    quantity       INTEGER       NOT NULL,
    unit_price     NUMERIC(15,2) NOT NULL,
    discount       NUMERIC(15,2) DEFAULT 0,
    subtotal       NUMERIC(15,2) NOT NULL,
    tax_rate       NUMERIC(5,2)  DEFAULT 0,
    tax_amount     NUMERIC(15,2) DEFAULT 0
);

CREATE INDEX idx_sales_code   ON sales(sale_code);
CREATE INDEX idx_sales_status ON sales(status);
CREATE INDEX idx_sale_items_sale ON sale_items(sale_id);
CREATE INDEX idx_products_barcode ON products(barcode);
