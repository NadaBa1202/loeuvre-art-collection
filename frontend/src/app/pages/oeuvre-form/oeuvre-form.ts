import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Oeuvre, OeuvreApi } from '@entities/oeuvre';
import { OeuvreForm } from '@features/oeuvre-save';
import { extractErrors } from '@shared/api';

@Component({
  selector: 'app-oeuvre-form-page',
  imports: [OeuvreForm],
  templateUrl: './oeuvre-form.html',
})
export class OeuvreFormPage {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly api = inject(OeuvreApi);

  private readonly id = this.route.snapshot.paramMap.get('id');
  readonly editing = this.id !== null;
  readonly defaultArtisteId = Number(this.route.snapshot.queryParamMap.get('artisteId')) || null;

  readonly oeuvre = signal<Oeuvre | null>(null);
  readonly error = signal<string | null>(null);

  constructor() {
    if (this.id !== null) {
      this.api.get(Number(this.id)).subscribe({
        next: o => this.oeuvre.set(o),
        error: err => this.error.set(extractErrors(err)[0]),
      });
    }
  }

  onSaved(o: Oeuvre) {
    this.router.navigate(['/oeuvres', o.id]);
  }
}