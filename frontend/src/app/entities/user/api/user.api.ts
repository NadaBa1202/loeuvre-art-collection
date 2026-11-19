// api/user.api.ts
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from '@entities/session';
import { ManagedUser } from '../model/managed-user';

@Injectable({ providedIn: 'root' })
export class UserApi {
  private readonly http = inject(HttpClient);
  private readonly url = '/api/users';

  list(): Observable<ManagedUser[]> { return this.http.get<ManagedUser[]>(this.url); }
  updateRoles(id: number, roles: Role[]): Observable<ManagedUser> {
    return this.http.put<ManagedUser>(`${this.url}/${id}/roles`, { roles });
  }
  remove(id: number): Observable<void> { return this.http.delete<void>(`${this.url}/${id}`); }
}