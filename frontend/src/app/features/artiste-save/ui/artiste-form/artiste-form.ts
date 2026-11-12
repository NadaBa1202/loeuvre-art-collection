import { Component, computed, effect, inject, input, output, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Artiste, ArtisteApi, ArtisteInput, formatLifespan } from '@entities/artiste';
import { extractErrors } from '@shared/api';
import { ImgFallback } from '@shared/ui/img-fallback';

const HTTPS = /^https:\/\/\S+$/;
const blank = (s: string | null | undefined) => (s && s.trim() ? s.trim() : null);

@Component({
  selector: 'app-artiste-form',
  imports: [ReactiveFormsModule, RouterLink, ImgFallback],
  templateUrl: './artiste-form.html',
  styleUrl: './artiste-form.css',
})
export class ArtisteForm {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ArtisteApi);

  /** Present when editing, absent when creating. */
  artiste = input<Artiste | null>(null);
  saved = output<Artiste>();

  readonly loading = signal(false);
  readonly errors = signal<string[]>([]);

  readonly form = this.fb.group({
    nom: ['', [Validators.required, Validators.maxLength(150)]],
    nationalite: ['', [Validators.maxLength(100)]],
    mouvement: ['', [Validators.maxLength(150)]],
    anneeNaissance: [null as number | null, [Validators.min(-3000), Validators.max(2100)]],
    anneeDeces: [null as number | null, [Validators.min(-3000), Validators.max(2100)]],
    photoUrl: ['', [Validators.maxLength(500), Validators.pattern(HTTPS)]],
    biographie: ['', [Validators.maxLength(5000)]],
  });

  private readonly photoValue = toSignal(this.form.controls.photoUrl.valueChanges, { initialValue: '' });
  readonly previewUrl = computed(() => {
    const v = this.photoValue();
    return v && HTTPS.test(v) ? v : null;
  });

  constructor() {
    effect(() => {
      const a = this.artiste();
      if (a) this.form.patchValue(a);
    });
  }

  lifespan(): string {
    return formatLifespan({
      anneeNaissance: this.form.controls.anneeNaissance.value,
      anneeDeces: this.form.controls.anneeDeces.value,
    });
  }

  invalid(name: string): boolean {
    const c = this.form.get(name);
    return !!c && c.invalid && c.touched;
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const input: ArtisteInput = {
      nom: (v.nom ?? '').trim(),
      nationalite: blank(v.nationalite),
      mouvement: blank(v.mouvement),
      anneeNaissance: v.anneeNaissance ?? null,
      anneeDeces: v.anneeDeces ?? null,
      photoUrl: blank(v.photoUrl),
      biographie: blank(v.biographie),
    };
    const current = this.artiste();
    const request$ = current ? this.api.update(current.id, input) : this.api.create(input);

    this.loading.set(true);
    this.errors.set([]);
    request$.subscribe({
      next: a => this.saved.emit(a),
      error: err => {
        this.errors.set(extractErrors(err));
        this.loading.set(false);
      },
    });
  }
}