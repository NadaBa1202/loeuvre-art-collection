import { Component, computed, effect, inject, input, output, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Artiste, ArtisteApi } from '@entities/artiste';
import { Oeuvre, OeuvreApi, OeuvreInput } from '@entities/oeuvre';
import { extractErrors } from '@shared/api';
import { ImgFallback } from '@shared/ui/img-fallback';

const HTTPS = /^https:\/\/\S+$/;
const blank = (s: string | null | undefined) => (s && s.trim() ? s.trim() : null);

@Component({
  selector: 'app-oeuvre-form',
  imports: [ReactiveFormsModule, RouterLink, ImgFallback],
  templateUrl: './oeuvre-form.html',
  styleUrl: './oeuvre-form.css',
})
export class OeuvreForm {
  private readonly fb = inject(FormBuilder);
  private readonly oeuvreApi = inject(OeuvreApi);
  private readonly artisteApi = inject(ArtisteApi);

  /** Present when editing. */
  oeuvre = input<Oeuvre | null>(null);
  /** Pre-selected artist when arriving from an artist page. */
  defaultArtisteId = input<number | null>(null);
  saved = output<Oeuvre>();

  readonly loading = signal(false);
  readonly errors = signal<string[]>([]);
  readonly artistes = signal<Artiste[]>([]);
  readonly artistesLoaded = signal(false);

  readonly techniques = [
    'Huile sur toile', 'Huile sur bois', 'Aquarelle', 'Gouache', 'Dessin', 'Gravure',
    'Lithographie', 'Sculpture en marbre', 'Sculpture en bronze', 'Photographie',
  ];

  readonly form = this.fb.group({
    titre: ['', [Validators.required, Validators.maxLength(200)]],
    idArtiste: [null as number | null, [Validators.required]],
    anneeCreation: [null as number | null, [Validators.min(-3000), Validators.max(2100)]],
    technique: ['', [Validators.maxLength(150)]],
    description: ['', [Validators.maxLength(5000)]],
    imageUrl: ['', [Validators.maxLength(500), Validators.pattern(HTTPS)]],
    credit: ['', [Validators.maxLength(500)]],
  });

  private readonly imageValue = toSignal(this.form.controls.imageUrl.valueChanges, { initialValue: '' });
  readonly previewUrl = computed(() => {
    const v = this.imageValue();
    return v && HTTPS.test(v) ? v : null;
  });

  constructor() {
    this.artisteApi.list().subscribe({
      next: list => {
        this.artistes.set(list);
        this.artistesLoaded.set(true);
      },
      error: err => this.errors.set(extractErrors(err)),
    });

    effect(() => {
      const o = this.oeuvre();
      if (o) {
        this.form.patchValue({ ...o, idArtiste: o.artisteId });
      } else {
        const id = this.defaultArtisteId();
        if (id != null) this.form.patchValue({ idArtiste: id });
      }
    });
  }

  artisteNom(): string {
    const id = this.form.controls.idArtiste.value;
    return this.artistes().find(a => a.id === id)?.nom ?? '';
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
    const input: OeuvreInput = {
      titre: (v.titre ?? '').trim(),
      idArtiste: v.idArtiste as number,
      anneeCreation: v.anneeCreation ?? null,
      technique: blank(v.technique),
      description: blank(v.description),
      imageUrl: blank(v.imageUrl),
      credit: blank(v.credit),
    };
    const current = this.oeuvre();
    const request$ = current
      ? this.oeuvreApi.update(current.id, input)
      : this.oeuvreApi.create(input);

    this.loading.set(true);
    this.errors.set([]);
    request$.subscribe({
      next: o => this.saved.emit(o),
      error: err => {
        this.errors.set(extractErrors(err));
        this.loading.set(false);
      },
    });
  }
}