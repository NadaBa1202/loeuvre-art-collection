import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Artiste, ArtisteApi } from '@entities/artiste';
import { ArtisteForm } from '@features/artiste-save';
import { extractErrors } from '@shared/api';

@Component({
  selector: 'app-artiste-form-page',
  imports: [ArtisteForm],
  templateUrl: './artiste-form.html',
})
export class ArtisteFormPage {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly api = inject(ArtisteApi);

  readonly editing = this.route.snapshot.paramMap.has('id');
  readonly artiste = signal<Artiste | null>(null);
  readonly error = signal<string | null>(null);

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.api.get(Number(id)).subscribe({
        next: a => this.artiste.set(a),
        error: err => this.error.set(extractErrors(err)[0]),
      });
    }
  }

  onSaved() {
    this.router.navigate(['/artistes']);
  }
}