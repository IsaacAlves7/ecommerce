-- V2__pos_seed_data.sql

INSERT INTO products (code, barcode, name, description, ncm, cfop, unit, price, tax_rate, stock_quantity, status, category)
VALUES
  ('CAFE001', '7891234560001', 'Café Expresso 250g',   'Café torrado e moído', '09012100', '5102', 'UN', 12.90, 12.00, 200, 'ACTIVE', 'Bebidas'),
  ('AGUA001', '7891234560002', 'Água Mineral 500ml',   'Água mineral natural', '22011000', '5102', 'UN',  2.50,  7.00, 500, 'ACTIVE', 'Bebidas'),
  ('PAO0001', '7891234560003', 'Pão de Forma Integral','Pão integral 450g',   '19051000', '5102', 'UN',  8.90,  7.00, 150, 'ACTIVE', 'Padaria'),
  ('LEITE01', '7891234560004', 'Leite Integral 1L',    'Leite UHT integral',  '04011000', '5102', 'UN',  4.80,  7.00, 300, 'ACTIVE', 'Laticínios'),
  ('OVOS001', '7891234560005', 'Ovos Brancos Dz',      'Ovos brancos dúzia',  '04070011', '5102', 'DZ', 10.50, 12.00, 100, 'ACTIVE', 'Hortifruti');
