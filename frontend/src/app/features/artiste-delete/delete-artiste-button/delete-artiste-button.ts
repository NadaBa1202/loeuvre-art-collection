import { Component, inject, input, output, signal } from '@angular/core';
import { Artiste, ArtisteApi } from '@entities/artiste';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';

@Component({
  selector: 'app-delete-artiste-button',
  imports: [Icon],
  templateUrl: './delete-artiste-button.html',
  styleUrl: './delete-artiste-button.css',
})
export class DeleteArtisteButton {
  private readonly api = inject(ArtisteApi);

  artiste = input.required<Artiste>();
  deleted = output<number>();

  readonly confirming = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);

  confirm() {
    this.busy.set(true);
    this.api.remove(this.artiste().id).subscribe({
      next: () => this.deleted.emit(this.artiste().id),
      error: err => {
        this.error.set(extractErrors(err)[0]);
        this.busy.set(false);
        this.confirming.set(false);
      },
    });
  }
}