import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Artiste, ArtisteApi, ArtisteCard } from '@entities/artiste';
import { SessionStore } from '@entities/session';
import { DeleteArtisteButton } from '@features/artiste-delete';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';

@Component({
  selector: 'app-artistes-page',
  imports: [RouterLink, ArtisteCard, DeleteArtisteButton, Icon],
  templateUrl: './artistes.html',
  styleUrl: './artistes.css',
})
export class ArtistesPage {
  private readonly api = inject(ArtisteApi);
  protected readonly session = inject(SessionStore);

  readonly artistes = signal<Artiste[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    this.api.list().subscribe({
      next: list => {
        this.artistes.set(list);
        this.loading.set(false);
      },
      error: err => {
        this.error.set(extractErrors(err)[0]);
        this.loading.set(false);
      },
    });
  }

  onDeleted(id: number) {
    this.artistes.update(list => list.filter(a => a.id !== id));
  }
}