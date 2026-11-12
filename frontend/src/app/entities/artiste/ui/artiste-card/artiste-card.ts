import { Component, computed, input } from '@angular/core';
import { ImgFallback } from '@shared/ui/img-fallback';
import { Artiste } from '../../model/artiste';
import { formatLifespan } from '../../lib/format-lifespan';

@Component({
  selector: 'app-artiste-card',
  imports: [ImgFallback],
  templateUrl: './artiste-card.html',
  styleUrl: './artiste-card.css',
})
export class ArtisteCard {
  artiste = input.required<Artiste>();

  readonly lifespan = computed(() => formatLifespan(this.artiste()));
  readonly meta = computed(() =>
    [this.artiste().nationalite, this.artiste().mouvement].filter(Boolean).join(' · '));
}