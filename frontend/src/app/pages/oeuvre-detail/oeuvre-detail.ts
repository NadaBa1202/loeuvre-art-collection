import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SessionStore } from '@entities/session';
import { Oeuvre, OeuvreApi } from '@entities/oeuvre';
import { DeleteOeuvreButton } from '@features/oeuvre-delete';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';
import { ImgFallback } from '@shared/ui/img-fallback';

@Component({
  selector: 'app-oeuvre-detail-page',
  imports: [RouterLink, DeleteOeuvreButton, Icon, ImgFallback],
  templateUrl: './oeuvre-detail.html',
  styleUrl: './oeuvre-detail.css',
})
export class OeuvreDetailPage {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly api = inject(OeuvreApi);
  protected readonly session = inject(SessionStore);

  readonly oeuvre = signal<Oeuvre | null>(null);
  readonly error = signal<string | null>(null);

  constructor() {
    this.api.get(Number(this.route.snapshot.paramMap.get('id'))).subscribe({
      next: o => this.oeuvre.set(o),
      error: err => this.error.set(extractErrors(err)[0]),
    });
  }

  onDeleted() {
    this.router.navigate(['/oeuvres']);
  }
}