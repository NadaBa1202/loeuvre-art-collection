import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Artiste, ArtisteApi, formatLifespan } from '@entities/artiste';
import { Oeuvre, OeuvreApi, OeuvreCard } from '@entities/oeuvre';
import { SessionStore } from '@entities/session';
import { DeleteArtisteButton } from '@features/artiste-delete';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';
import { ImgFallback } from '@shared/ui/img-fallback';

@Component({
  selector: 'app-artiste-detail-page',
  imports: [RouterLink, OeuvreCard, DeleteArtisteButton, Icon, ImgFallback],
  templateUrl: './artiste-detail.html',
  styleUrl: './artiste-detail.css',
})
export class ArtisteDetailPage {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly artisteApi = inject(ArtisteApi);
  private readonly oeuvreApi = inject(OeuvreApi);
  protected readonly session = inject(SessionStore);

  readonly artiste = signal<Artiste | null>(null);
  readonly oeuvres = signal<Oeuvre[]>([]);
  readonly error = signal<string | null>(null);

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin({ artiste: this.artisteApi.get(id), oeuvres: this.oeuvreApi.list(id) }).subscribe({
      next: ({ artiste, oeuvres }) => {
        this.artiste.set(artiste);
        this.oeuvres.set(oeuvres);
      },
      error: err => this.error.set(extractErrors(err)[0]),
    });
  }

  lifespan(a: Artiste): string {
    return formatLifespan(a);
  }

  onArtisteDeleted() {
    this.router.navigate(['/artistes']);
  }
}