import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Artiste, ArtisteInput } from '../model/artiste';

@Injectable({ providedIn: 'root' })
export class ArtisteApi {
  private readonly http = inject(HttpClient);
  private readonly url = '/api/artistes';

  list(): Observable<Artiste[]> { return this.http.get<Artiste[]>(this.url); }
  get(id: number): Observable<Artiste> { return this.http.get<Artiste>(`${this.url}/${id}`); }
  create(input: ArtisteInput): Observable<Artiste> { return this.http.post<Artiste>(this.url, input); }
  update(id: number, input: ArtisteInput): Observable<Artiste> { return this.http.put<Artiste>(`${this.url}/${id}`, input); }
  remove(id: number): Observable<void> { return this.http.delete<void>(`${this.url}/${id}`); }
}