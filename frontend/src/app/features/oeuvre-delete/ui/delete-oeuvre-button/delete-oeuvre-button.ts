import { Component, inject, input, output, signal } from '@angular/core';
import { Oeuvre, OeuvreApi } from '@entities/oeuvre';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';

@Component({
  selector: 'app-delete-oeuvre-button',
  imports: [Icon],
  templateUrl: './delete-oeuvre-button.html',
  styleUrl: './delete-oeuvre-button.css',
})
export class DeleteOeuvreButton {
  private readonly api = inject(OeuvreApi);

  oeuvre = input.required<Oeuvre>();
  deleted = output<number>();

  readonly confirming = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);

  confirm() {
    this.busy.set(true);
    this.api.remove(this.oeuvre().id).subscribe({
      next: () => this.deleted.emit(this.oeuvre().id),
      error: err => {
        this.error.set(extractErrors(err)[0]);
        this.busy.set(false);
        this.confirming.set(false);
      },
    });
  }
}