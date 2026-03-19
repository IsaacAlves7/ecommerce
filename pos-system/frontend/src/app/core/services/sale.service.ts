// src/app/core/services/sale.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateSaleRequest, SaleResponse } from '../models/sale.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SaleService {
  private base = `${environment.apiUrl}/api/v1/sales`;

  constructor(private http: HttpClient) {}

  create(req: CreateSaleRequest): Observable<SaleResponse> {
    return this.http.post<SaleResponse>(this.base, req);
  }

  findById(id: number): Observable<SaleResponse> {
    return this.http.get<SaleResponse>(`${this.base}/${id}`);
  }

  findAll(page = 0, size = 20): Observable<SaleResponse[]> {
    return this.http.get<SaleResponse[]>(`${this.base}?page=${page}&size=${size}`);
  }

  confirm(id: number): Observable<SaleResponse> {
    return this.http.post<SaleResponse>(`${this.base}/${id}/confirm`, {});
  }

  cancel(id: number): Observable<SaleResponse> {
    return this.http.post<SaleResponse>(`${this.base}/${id}/cancel`, {});
  }
}
