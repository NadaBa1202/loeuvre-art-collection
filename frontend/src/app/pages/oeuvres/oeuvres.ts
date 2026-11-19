import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SessionStore } from '@entities/session';
import { Oeuvre, OeuvreApi, OeuvreCard } from '@entities/oeuvre';
import { DeleteOeuvreButton } from '@features/oeuvre-delete';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';

@Component({
  selector: 'app-oeuvres-page',
  imports: [RouterLink, OeuvreCard, DeleteOeuvreButton, Icon],
  templateUrl: './oeuvres.html',
  styleUrl: './oeuvres.css',
})
export class OeuvresPage {
  private readonly api = inject(OeuvreApi);
  protected readonly session = inject(SessionStore);

  readonly oeuvres = signal<Oeuvre[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    this.api.list().subscribe({
      next: list => {
        this.oeuvres.set(list);
        this.loading.set(false);
      },
      error: err => {
        this.error.set(extractErrors(err)[0]);
        this.loading.set(false);
      },
    });
  }

  onDeleted(id: number) {
    this.oeuvres.update(list => list.filter(o => o.id !== id));
  }
}