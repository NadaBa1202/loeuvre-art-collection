import { Injectable, computed, signal } from '@angular/core';
import { AuthResponse, Role, User } from './types';

@Injectable({ providedIn: 'root' })
export class SessionStore {
  private token: string | null = null;

  readonly user = signal<User | null>(null);
  readonly isAuthenticated = computed(() => this.user() !== null);
  readonly isAdmin = computed(() => this.user()?.roles.includes('ADMIN') ?? false);

  get accessToken(): string | null {
    return this.token;
  }

  set(auth: AuthResponse): void {
    this.token = auth.accessToken;
    this.user.set(auth.user);
  }

  clear(): void {
    this.token = null;
    this.user.set(null);
  }

  hasAnyRole(roles: Role[]): boolean {
    const mine = this.user()?.roles ?? [];
    return roles.some(r => mine.includes(r));
  }
}