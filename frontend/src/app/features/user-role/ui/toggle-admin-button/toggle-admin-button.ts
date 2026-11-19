import { Component, computed, inject, input, output, signal } from '@angular/core';
import { Role } from '@entities/session';
import { ManagedUser, UserApi } from '@entities/user';
import { extractErrors } from '@shared/api';

@Component({
  selector: 'app-toggle-admin-button',
  templateUrl: './toggle-admin-button.html',
  styleUrl: './toggle-admin-button.css',
})
export class ToggleAdminButton {
  private readonly api = inject(UserApi);

  user = input.required<ManagedUser>();
  changed = output<ManagedUser>();

  readonly confirming = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);
  readonly isAdmin = computed(() => this.user().roles.includes('ADMIN'));

  confirm() {
    const next: Role[] = this.isAdmin() ? ['COLLABORATEUR'] : ['ADMIN'];
    this.busy.set(true);
    this.error.set(null);
    this.api.updateRoles(this.user().id, next).subscribe({
      next: u => {
        this.busy.set(false);
        this.confirming.set(false);
        this.changed.emit(u);
      },
      error: err => {
        this.error.set(extractErrors(err)[0]);
        this.busy.set(false);
        this.confirming.set(false);
      },
    });
  }
}