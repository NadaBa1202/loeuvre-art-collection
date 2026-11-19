import { Component, computed, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ImgFallback } from '@shared/ui/img-fallback';
import { Oeuvre } from '../../model/oeuvre';

@Component({
  selector: 'app-oeuvre-card',
  imports: [RouterLink, ImgFallback],
  templateUrl: './oeuvre-card.html',
  styleUrl: './oeuvre-card.css',
})
export class OeuvreCard {
  oeuvre = input.required<Oeuvre>();

  readonly meta = computed(() => {
    const o = this.oeuvre();
    return [o.anneeCreation != null ? String(o.anneeCreation) : null, o.technique]
      .filter(Boolean).join(' · ');
  });
}