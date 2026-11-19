import { Component, computed, inject, signal } from '@angular/core';
import { SessionStore } from '@entities/session';
import { ManagedUser, UserApi } from '@entities/user';
import { ToggleAdminButton } from '@features/user-role';
import { DeleteUserButton } from '@features/user-delete';
import { extractErrors } from '@shared/api';

type Filter = 'ALL' | 'ADMIN' | 'COLLABORATEUR';

@Component({
  selector: 'app-users-page',
  imports: [ToggleAdminButton, DeleteUserButton],
  templateUrl: './users.html',
  styleUrl: './users.css',
})
export class UsersPage {
  private readonly api = inject(UserApi);
  protected readonly session = inject(SessionStore);

  readonly users = signal<ManagedUser[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly filter = signal<Filter>('ALL');

  readonly adminCount = computed(() => this.users().filter(u => u.roles.includes('ADMIN')).length);
  readonly visible = computed(() => {
    const f = this.filter();
    return f === 'ALL' ? this.users() : this.users().filter(u => u.roles.includes(f));
  });

  constructor() {
    this.api.list().subscribe({
      next: list => { this.users.set(list); this.loading.set(false); },
      error: err => { this.error.set(extractErrors(err)[0]); this.loading.set(false); },
    });
  }

  isMe(u: ManagedUser) { return this.session.user()?.id === u.id; }
  initials(u: ManagedUser) { return ((u.prenom?.[0] ?? '') + (u.nom?.[0] ?? '')).toUpperCase(); }
  label(role: string) { return role === 'ADMIN' ? 'Administrateur' : 'Collaborateur'; }

  onChanged(updated: ManagedUser) {
    this.users.update(list => list.map(u => (u.id === updated.id ? updated : u)));
  }
  onDeleted(id: number) {
    this.users.update(list => list.filter(u => u.id !== id));
  }
}