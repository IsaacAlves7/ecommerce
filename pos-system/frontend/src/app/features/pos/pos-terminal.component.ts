// src/app/features/pos/pos-terminal.component.ts
import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { SaleService } from '../../core/services/sale.service';
import { ProductService } from '../../core/services/product.service';
import { CreateSaleRequest, SaleItem, SaleResponse, Product } from '../../core/models/sale.model';

@Component({
  selector: 'app-pos-terminal',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  template: `
<div class="pos-layout">

  <!-- SIDEBAR: Cart -->
  <aside class="cart-panel">
    <div class="cart-header">
      <h2>🛒 Carrinho</h2>
      <span class="badge" *ngIf="cartItems.length">{{ cartItems.length }} item(s)</span>
    </div>

    <div class="cart-items" *ngIf="cartItems.length > 0; else emptyCart">
      <div class="cart-item" *ngFor="let item of cartItems; let i = index">
        <div class="item-info">
          <div class="item-name">{{ item.productName }}</div>
          <div class="item-code">{{ item.productCode }}</div>
        </div>
        <div class="item-controls">
          <button class="qty-btn" (click)="decreaseQty(i)">−</button>
          <span class="qty">{{ item.quantity }}</span>
          <button class="qty-btn" (click)="increaseQty(i)">+</button>
        </div>
        <div class="item-price">
          <div>R$ {{ (item.unitPrice * item.quantity) | number:'1.2-2' }}</div>
          <button class="remove-btn" (click)="removeItem(i)">✕</button>
        </div>
      </div>
    </div>

    <ng-template #emptyCart>
      <div class="empty-cart">
        <span>🛍️</span>
        <p>Carrinho vazio</p>
        <small>Busque produtos ou escaneie o código de barras</small>
      </div>
    </ng-template>

    <!-- Customer Info -->
    <div class="customer-section">
      <h3>👤 Dados do Cliente (opcional)</h3>
      <input [(ngModel)]="customerName" placeholder="Nome" class="input"/>
      <input [(ngModel)]="customerEmail" placeholder="E-mail (para envio NFC-e)" class="input"/>
      <input [(ngModel)]="customerDocument" placeholder="CPF (11 dígitos)" class="input" maxlength="11"/>
    </div>

    <!-- Totals -->
    <div class="totals">
      <div class="total-row"><span>Subtotal</span><span>R$ {{ subtotal | number:'1.2-2' }}</span></div>
      <div class="total-row total-final"><span>TOTAL</span><span>R$ {{ subtotal | number:'1.2-2' }}</span></div>
    </div>

    <!-- Payment -->
    <div class="payment-section">
      <h3>💳 Forma de Pagamento</h3>
      <div class="payment-methods">
        <button *ngFor="let pm of paymentMethods"
                [class.active]="selectedPayment === pm.value"
                (click)="selectedPayment = pm.value"
                class="pm-btn">
          {{ pm.icon }} {{ pm.label }}
        </button>
      </div>
    </div>

    <!-- Actions -->
    <div class="cart-actions">
      <button class="btn-cancel" (click)="clearCart()">🗑 Limpar</button>
      <button class="btn-confirm" [disabled]="cartItems.length === 0 || !selectedPayment || loading"
              (click)="finalizeSale()">
        {{ loading ? '⏳ Processando...' : '✅ Finalizar Venda' }}
      </button>
    </div>
  </aside>

  <!-- MAIN: Products -->
  <main class="products-panel">
    <div class="search-bar">
      <input #barcodeInput
             [(ngModel)]="searchQuery"
             (keyup.enter)="searchProducts()"
             (input)="onSearchInput()"
             placeholder="🔍 Buscar produto por nome, código ou código de barras..."
             class="search-input"/>
      <button class="btn-search" (click)="searchProducts()">Buscar</button>
    </div>

    <div class="categories">
      <button *ngFor="let cat of categories"
              [class.active]="selectedCategory === cat"
              (click)="filterByCategory(cat)"
              class="cat-btn">
        {{ cat }}
      </button>
    </div>

    <div class="products-grid">
      <div class="product-card" *ngFor="let product of filteredProducts"
           (click)="addToCart(product)">
        <div class="product-icon">{{ getCategoryIcon(product.category) }}</div>
        <div class="product-name">{{ product.name }}</div>
        <div class="product-code">{{ product.code }}</div>
        <div class="product-price">R$ {{ product.price | number:'1.2-2' }}</div>
        <div class="product-stock" [class.low]="product.stockQuantity < 10">
          Estoque: {{ product.stockQuantity }}
        </div>
      </div>
    </div>
  </main>

  <!-- SUCCESS MODAL -->
  <div class="modal-overlay" *ngIf="showSuccessModal" (click)="closeModal()">
    <div class="modal" (click)="$event.stopPropagation()">
      <div class="modal-header success">
        <h2>✅ Venda Finalizada!</h2>
      </div>
      <div class="modal-body" *ngIf="lastSale">
        <div class="sale-info">
          <div class="sale-field"><label>Código da Venda</label><strong>{{ lastSale.saleCode }}</strong></div>
          <div class="sale-field"><label>Total</label><strong>R$ {{ lastSale.total | number:'1.2-2' }}</strong></div>
          <div class="sale-field"><label>Pagamento</label><strong>{{ translatePayment(lastSale.paymentMethod) }}</strong></div>
          <div class="sale-field" *ngIf="lastSale.customerEmail">
            <label>NFC-e enviada para</label><strong>{{ lastSale.customerEmail }}</strong>
          </div>
        </div>
        <div class="nfce-notice" *ngIf="lastSale.customerEmail">
          <span>📧</span>
          <p>A Nota Fiscal Eletrônica está sendo gerada e será enviada por email em instantes!</p>
        </div>
        <div class="nfce-notice info" *ngIf="!lastSale.customerEmail">
          <span>ℹ️</span>
          <p>Nenhum e-mail informado. A NFC-e foi gerada internamente.</p>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn-confirm" (click)="closeModal()">Nova Venda</button>
      </div>
    </div>
  </div>
</div>
  `,
  styles: [`
    .pos-layout { display: flex; height: 100vh; background: #f0f2f5; }

    /* Cart Panel */
    .cart-panel { width: 380px; min-width: 380px; background: #fff; display: flex; flex-direction: column; box-shadow: 2px 0 12px rgba(0,0,0,0.08); overflow-y: auto; }
    .cart-header { padding: 16px 20px; background: #1a73e8; color: #fff; display: flex; justify-content: space-between; align-items: center; }
    .cart-header h2 { font-size: 18px; margin: 0; }
    .badge { background: #ff5252; color: #fff; border-radius: 12px; padding: 2px 10px; font-size: 12px; }
    .cart-items { flex: 1; padding: 12px; overflow-y: auto; }
    .cart-item { display: flex; align-items: center; gap: 8px; padding: 10px; margin-bottom: 8px; background: #f8f9fa; border-radius: 8px; }
    .item-info { flex: 1; }
    .item-name { font-size: 13px; font-weight: 600; }
    .item-code { font-size: 11px; color: #888; }
    .item-controls { display: flex; align-items: center; gap: 6px; }
    .qty-btn { width: 26px; height: 26px; border: 1px solid #ddd; background: #fff; border-radius: 50%; cursor: pointer; font-size: 16px; display: flex; align-items: center; justify-content: center; }
    .qty-btn:hover { background: #e8f0fe; }
    .qty { font-weight: 700; min-width: 24px; text-align: center; }
    .item-price { display: flex; flex-direction: column; align-items: flex-end; gap: 4px; font-size: 13px; font-weight: 600; }
    .remove-btn { background: none; border: none; color: #e53935; cursor: pointer; font-size: 13px; }
    .empty-cart { text-align: center; padding: 40px 20px; color: #aaa; }
    .empty-cart span { font-size: 40px; }
    .empty-cart p { margin: 10px 0 4px; font-size: 15px; }
    .empty-cart small { font-size: 12px; }

    .customer-section { padding: 12px 16px; border-top: 1px solid #eee; }
    .customer-section h3 { font-size: 13px; color: #666; margin-bottom: 8px; }
    .input { width: 100%; padding: 8px 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 13px; margin-bottom: 6px; }

    .totals { padding: 12px 16px; border-top: 1px solid #eee; }
    .total-row { display: flex; justify-content: space-between; padding: 4px 0; font-size: 14px; }
    .total-final { font-size: 18px; font-weight: 700; color: #1a73e8; padding-top: 8px; border-top: 2px solid #e8f0fe; }

    .payment-section { padding: 12px 16px; border-top: 1px solid #eee; }
    .payment-section h3 { font-size: 13px; color: #666; margin-bottom: 8px; }
    .payment-methods { display: grid; grid-template-columns: 1fr 1fr; gap: 6px; }
    .pm-btn { padding: 8px; border: 2px solid #ddd; background: #f8f9fa; border-radius: 8px; cursor: pointer; font-size: 12px; }
    .pm-btn.active { border-color: #1a73e8; background: #e8f0fe; color: #1a73e8; font-weight: 700; }

    .cart-actions { padding: 16px; display: flex; gap: 8px; border-top: 1px solid #eee; }
    .btn-cancel { flex: 1; padding: 12px; border: 1px solid #e53935; background: #fff; color: #e53935; border-radius: 8px; cursor: pointer; font-weight: 600; }
    .btn-confirm { flex: 2; padding: 12px; background: #1a73e8; color: #fff; border: none; border-radius: 8px; cursor: pointer; font-weight: 700; font-size: 14px; }
    .btn-confirm:disabled { background: #ccc; cursor: not-allowed; }

    /* Products Panel */
    .products-panel { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
    .search-bar { padding: 16px 20px; background: #fff; display: flex; gap: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    .search-input { flex: 1; padding: 10px 16px; border: 2px solid #e8f0fe; border-radius: 8px; font-size: 14px; }
    .search-input:focus { outline: none; border-color: #1a73e8; }
    .btn-search { padding: 10px 20px; background: #1a73e8; color: #fff; border: none; border-radius: 8px; cursor: pointer; }
    .categories { padding: 10px 20px; background: #fff; display: flex; gap: 8px; overflow-x: auto; border-bottom: 1px solid #eee; }
    .cat-btn { padding: 6px 14px; border: 1px solid #ddd; background: #f8f9fa; border-radius: 20px; cursor: pointer; font-size: 13px; white-space: nowrap; }
    .cat-btn.active { background: #1a73e8; color: #fff; border-color: #1a73e8; }
    .products-grid { flex: 1; overflow-y: auto; padding: 20px; display: grid; grid-template-columns: repeat(auto-fill, minmax(170px, 1fr)); gap: 16px; }
    .product-card { background: #fff; border-radius: 12px; padding: 16px; cursor: pointer; transition: all 0.2s; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
    .product-card:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(26,115,232,0.15); border: 1px solid #1a73e8; }
    .product-icon { font-size: 28px; margin-bottom: 8px; }
    .product-name { font-size: 13px; font-weight: 600; margin-bottom: 4px; }
    .product-code { font-size: 11px; color: #888; margin-bottom: 4px; }
    .product-price { font-size: 16px; font-weight: 700; color: #1a73e8; }
    .product-stock { font-size: 11px; color: #4caf50; margin-top: 4px; }
    .product-stock.low { color: #ff9800; }

    /* Modal */
    .modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
    .modal { background: #fff; border-radius: 16px; width: 460px; overflow: hidden; }
    .modal-header { padding: 24px; text-align: center; }
    .modal-header.success { background: linear-gradient(135deg, #4caf50, #2e7d32); color: #fff; }
    .modal-header h2 { margin: 0; font-size: 22px; }
    .modal-body { padding: 24px; }
    .sale-info { display: flex; flex-direction: column; gap: 10px; margin-bottom: 16px; }
    .sale-field { display: flex; justify-content: space-between; padding: 8px; background: #f8f9fa; border-radius: 6px; font-size: 14px; }
    .sale-field label { color: #666; }
    .nfce-notice { display: flex; gap: 12px; align-items: flex-start; background: #e8f5e9; border-radius: 8px; padding: 12px; }
    .nfce-notice.info { background: #e3f2fd; }
    .nfce-notice span { font-size: 24px; }
    .nfce-notice p { font-size: 13px; color: #333; }
    .modal-footer { padding: 16px 24px; border-top: 1px solid #eee; }
    .modal-footer .btn-confirm { width: 100%; }
  `]
})
export class PosTerminalComponent implements OnInit {

  cartItems: SaleItem[] = [];
  products: Product[] = [];
  filteredProducts: Product[] = [];
  categories: string[] = ['Todos'];
  selectedCategory = 'Todos';
  searchQuery = '';
  selectedPayment = '';
  customerName = '';
  customerEmail = '';
  customerDocument = '';
  loading = false;
  showSuccessModal = false;
  lastSale: SaleResponse | null = null;

  paymentMethods = [
    { value: 'CASH', label: 'Dinheiro', icon: '💵' },
    { value: 'CREDIT_CARD', label: 'Crédito', icon: '💳' },
    { value: 'DEBIT_CARD', label: 'Débito', icon: '💳' },
    { value: 'PIX', label: 'PIX', icon: '⚡' },
  ];

  constructor(
    private saleService: SaleService,
    private productService: ProductService
  ) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.findAll().subscribe({
      next: (products) => {
        this.products = products;
        this.filteredProducts = products;
        const cats = ['Todos', ...new Set(products.map(p => p.category || 'Outros').filter(Boolean))];
        this.categories = cats;
      },
      error: () => {
        // Use demo products if API unavailable
        this.products = this.getDemoProducts();
        this.filteredProducts = this.products;
        this.categories = ['Todos', 'Bebidas', 'Padaria', 'Laticínios', 'Hortifruti'];
      }
    });
  }

  searchProducts() {
    if (!this.searchQuery.trim()) {
      this.filterByCategory(this.selectedCategory);
      return;
    }
    const q = this.searchQuery.toLowerCase();
    this.filteredProducts = this.products.filter(p =>
      p.name.toLowerCase().includes(q) ||
      p.code.toLowerCase().includes(q) ||
      (p.barcode || '').includes(q)
    );
  }

  onSearchInput() {
    if (this.searchQuery.length >= 3) this.searchProducts();
    else if (!this.searchQuery) this.filterByCategory(this.selectedCategory);
  }

  filterByCategory(cat: string) {
    this.selectedCategory = cat;
    this.searchQuery = '';
    this.filteredProducts = cat === 'Todos' ? this.products :
      this.products.filter(p => p.category === cat);
  }

  addToCart(product: Product) {
    const existing = this.cartItems.find(i => i.productCode === product.code);
    if (existing) {
      existing.quantity++;
    } else {
      this.cartItems.push({
        productId: product.id,
        productCode: product.code,
        productName: product.name,
        ncm: product.ncm,
        cfop: product.cfop || '5102',
        unit: product.unit,
        quantity: 1,
        unitPrice: product.price,
        taxRate: product.taxRate,
      });
    }
  }

  increaseQty(i: number) { this.cartItems[i].quantity++; }
  decreaseQty(i: number) {
    if (this.cartItems[i].quantity > 1) this.cartItems[i].quantity--;
    else this.removeItem(i);
  }
  removeItem(i: number) { this.cartItems.splice(i, 1); }

  clearCart() {
    this.cartItems = [];
    this.customerName = '';
    this.customerEmail = '';
    this.customerDocument = '';
    this.selectedPayment = '';
  }

  get subtotal(): number {
    return this.cartItems.reduce((sum, i) => sum + i.unitPrice * i.quantity, 0);
  }

  finalizeSale() {
    if (!this.cartItems.length || !this.selectedPayment) return;
    this.loading = true;

    const req: CreateSaleRequest = {
      items: this.cartItems,
      paymentMethod: this.selectedPayment,
      customerEmail: this.customerEmail || undefined,
      customerDocument: this.customerDocument || undefined,
      customerName: this.customerName || undefined,
      terminalId: 'TERMINAL-001',
      operatorId: 'OPERATOR-001',
    };

    this.saleService.create(req).subscribe({
      next: (sale) => {
        // Confirm immediately after creation
        this.saleService.confirm(sale.id).subscribe({
          next: (confirmed) => {
            this.lastSale = confirmed;
            this.showSuccessModal = true;
            this.loading = false;
          },
          error: () => {
            this.lastSale = sale;
            this.showSuccessModal = true;
            this.loading = false;
          }
        });
      },
      error: (e) => {
        alert('Erro ao finalizar venda: ' + (e.error?.message || e.message));
        this.loading = false;
      }
    });
  }

  closeModal() {
    this.showSuccessModal = false;
    this.clearCart();
  }

  translatePayment(method: string): string {
    const map: Record<string, string> = {
      CASH: 'Dinheiro', CREDIT_CARD: 'Cartão de Crédito',
      DEBIT_CARD: 'Cartão de Débito', PIX: 'PIX', VOUCHER: 'Voucher'
    };
    return map[method] || method;
  }

  getCategoryIcon(category?: string): string {
    const icons: Record<string, string> = {
      'Bebidas': '🥤', 'Padaria': '🍞', 'Laticínios': '🥛',
      'Hortifruti': '🥦', 'Carnes': '🥩', 'Limpeza': '🧹',
    };
    return icons[category || ''] || '📦';
  }

  private getDemoProducts(): Product[] {
    return [
      { id: 1, code: 'CAFE001', barcode: '7891234560001', name: 'Café Expresso 250g', unit: 'UN', price: 12.90, taxRate: 12, stockQuantity: 200, status: 'ACTIVE', category: 'Bebidas' },
      { id: 2, code: 'AGUA001', barcode: '7891234560002', name: 'Água Mineral 500ml', unit: 'UN', price: 2.50, taxRate: 7, stockQuantity: 500, status: 'ACTIVE', category: 'Bebidas' },
      { id: 3, code: 'PAO0001', barcode: '7891234560003', name: 'Pão de Forma Integral', unit: 'UN', price: 8.90, taxRate: 7, stockQuantity: 150, status: 'ACTIVE', category: 'Padaria' },
      { id: 4, code: 'LEITE01', barcode: '7891234560004', name: 'Leite Integral 1L', unit: 'UN', price: 4.80, taxRate: 7, stockQuantity: 300, status: 'ACTIVE', category: 'Laticínios' },
      { id: 5, code: 'OVOS001', barcode: '7891234560005', name: 'Ovos Brancos Dz', unit: 'DZ', price: 10.50, taxRate: 12, stockQuantity: 100, status: 'ACTIVE', category: 'Hortifruti' },
    ];
  }
}
