import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Oeuvre, OeuvreInput } from '../model/oeuvre';

@Injectable({ providedIn: 'root' })
export class OeuvreApi {
  private readonly http = inject(HttpClient);
  private readonly url = '/api/oeuvres';

  list(artisteId?: number): Observable<Oeuvre[]> {
    const params = artisteId != null ? new HttpParams().set('artisteId', artisteId) : undefined;
    return this.http.get<Oeuvre[]>(this.url, { params });
  }
  get(id: number): Observable<Oeuvre> { return this.http.get<Oeuvre>(`${this.url}/${id}`); }
  create(input: OeuvreInput): Observable<Oeuvre> { return this.http.post<Oeuvre>(this.url, input); }
  update(id: number, input: OeuvreInput): Observable<Oeuvre> { return this.http.put<Oeuvre>(`${this.url}/${id}`, input); }
  remove(id: number): Observable<void> { return this.http.delete<void>(`${this.url}/${id}`); }
}