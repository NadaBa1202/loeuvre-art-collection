import { Component, computed, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthApi, SessionStore } from '@entities/session';
import { Icon } from '@shared/ui/icon';

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, Icon],
  templateUrl: './app-shell.html',
  styleUrl: './app-shell.css',
})
export class AppShell {
  protected readonly session = inject(SessionStore);
  private readonly auth = inject(AuthApi);
  private readonly router = inject(Router);

  readonly fullName = computed(() => {
    const u = this.session.user();
    return u ? `${u.prenom} ${u.nom}` : '';
  });
  readonly initials = computed(() => {
    const u = this.session.user();
    return u ? `${u.prenom[0] ?? ''}${u.nom[0] ?? ''}`.toUpperCase() : '';
  });
  readonly roleLabel = computed(() => (this.session.isAdmin() ? 'Administrateur' : 'Collaborateur'));

  logout() {
    const done = () => this.router.navigateByUrl('/');
    this.auth.logout().subscribe({ next: done, error: done });
  }
}