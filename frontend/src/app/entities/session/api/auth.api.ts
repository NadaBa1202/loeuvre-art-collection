import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, finalize, shareReplay, tap } from 'rxjs';
import { SessionStore } from '../model/session.store';
import { AuthResponse, LoginPayload, RegisterPayload, User } from '../model/types';

const AJAX = { 'X-Requested-With': 'XMLHttpRequest' };   // required by /refresh and /logout

@Injectable({ providedIn: 'root' })
export class AuthApi {
  private readonly http = inject(HttpClient);
  private readonly session = inject(SessionStore);
  private refreshing$: Observable<AuthResponse> | null = null;

  register(payload: RegisterPayload): Observable<User> {
    return this.http.post<User>('/api/auth/register', payload);
  }

  login(payload: LoginPayload): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/api/auth/login', payload).pipe(
      tap(res => this.session.set(res)),
    );
  }

  refresh(): Observable<AuthResponse> {
    if (!this.refreshing$) {
      this.refreshing$ = this.http.post<AuthResponse>('/api/auth/refresh', {}, { headers: AJAX }).pipe(
        tap(res => this.session.set(res)),
        finalize(() => (this.refreshing$ = null)),
        shareReplay(1),
      );
    }
    return this.refreshing$;
  }

  logout(): Observable<void> {
    return this.http.post<void>('/api/auth/logout', {}, { headers: AJAX }).pipe(
      finalize(() => this.session.clear()),
    );
  }
}