import { Artiste } from '../model/artiste';

export function formatLifespan(a: Pick<Artiste, 'anneeNaissance' | 'anneeDeces'>): string {
  if (a.anneeNaissance == null && a.anneeDeces == null) return '';
  return `${a.anneeNaissance ?? '?'} — ${a.anneeDeces ?? ''}`.trim();
}