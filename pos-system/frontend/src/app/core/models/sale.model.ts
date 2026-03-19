// src/app/core/models/sale.model.ts
export interface SaleItem {
  productId?: number;
  productCode: string;
  productName: string;
  ncm?: string;
  cfop?: string;
  unit: string;
  quantity: number;
  unitPrice: number;
  taxRate: number;
  subtotal?: number;
  taxAmount?: number;
}

export interface CreateSaleRequest {
  items: SaleItem[];
  paymentMethod: string;
  customerEmail?: string;
  customerDocument?: string;
  customerName?: string;
  terminalId: string;
  operatorId: string;
}

export interface SaleResponse {
  id: number;
  saleCode: string;
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'REFUNDED';
  items: SaleItem[];
  subtotal: number;
  discount: number;
  total: number;
  paymentMethod: string;
  customerEmail?: string;
  customerName?: string;
  terminalId: string;
  createdAt: string;
}

export interface Product {
  id: number;
  code: string;
  barcode?: string;
  name: string;
  description?: string;
  ncm?: string;
  cfop?: string;
  unit: string;
  price: number;
  taxRate: number;
  stockQuantity: number;
  status: 'ACTIVE' | 'INACTIVE';
  category?: string;
}

export interface NfceResponse {
  id: number;
  accessKey: string;
  nfceNumber: string;
  series: string;
  status: 'PROCESSING' | 'AUTHORIZED' | 'REJECTED' | 'CANCELLED';
  saleCode: string;
  customerName?: string;
  customerDocument?: string;
  customerEmail?: string;
  total: number;
  taxTotal: number;
  paymentMethod: string;
  qrCodeUrl?: string;
  qrCodeBase64?: string;
  protocol?: string;
  items: { productName: string; quantity: number; unitPrice: number; subtotal: number }[];
  issuedAt: string;
}
