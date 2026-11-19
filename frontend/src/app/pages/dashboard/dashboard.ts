import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Dashboard, DashboardApi } from '@entities/dashboard';
import { extractErrors } from '@shared/api';

const PALETTE = ['#172b38', '#b8955a', '#775a25', '#d5cebf', '#5f7d8c', '#a45a3c'];
const PLACEHOLDER = 'placeholder.svg'; // adjust to your file in the public folder

@Component({
  selector: 'app-dashboard-page',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardPage {
  private readonly api = inject(DashboardApi);

  readonly data = signal<Dashboard | null>(null);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  readonly slices = computed(() => {
    const d = this.data();
    if (!d) return [];
    const sum = d.parTechnique.reduce((s, t) => s + t.total, 0) || 1;
    return d.parTechnique.map((t, i) => ({
      ...t,
      color: PALETTE[i % PALETTE.length],
      pct: Math.round((t.total / sum) * 100),
    }));
  });

  readonly donut = computed(() => {
    const total = this.data()?.nbOeuvres ?? 0;
    if (total === 0) return 'none';
    let acc = 0;
    const stops = this.slices().map(s => {
      const from = (acc / total) * 100;
      acc += s.total;
      return `${s.color} ${from}% ${(acc / total) * 100}%`;
    });
    return `conic-gradient(${stops.join(', ')})`;
  });

  readonly average = computed(() => {
    const d = this.data();
    return d && d.nbArtistes > 0 ? (d.nbOeuvres / d.nbArtistes).toFixed(1) : '0';
  });

  constructor() {
    this.api.get().subscribe({
      next: d => { this.data.set(d); this.loading.set(false); },
      error: err => { this.error.set(extractErrors(err)[0]); this.loading.set(false); },
    });
  }

  onImgError(e: Event) {
    const img = e.target as HTMLImageElement;
    if (img.dataset['fallback']) return; // avoid an infinite loop
    img.dataset['fallback'] = '1';
    img.src = PLACEHOLDER;
  }
}