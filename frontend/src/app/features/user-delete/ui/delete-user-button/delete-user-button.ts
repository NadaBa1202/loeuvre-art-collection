import { Component, inject, input, output, signal } from '@angular/core';
import { ManagedUser, UserApi } from '@entities/user';
import { extractErrors } from '@shared/api';
import { Icon } from '@shared/ui/icon';

@Component({
  selector: 'app-delete-user-button',
  imports: [Icon],
  templateUrl: './delete-user-button.html',
  styleUrl: './delete-user-button.css',
})
export class DeleteUserButton {
  private readonly api = inject(UserApi);

  user = input.required<ManagedUser>();
  deleted = output<number>();

  readonly confirming = signal(false);
  readonly busy = signal(false);
  readonly error = signal<string | null>(null);

  confirm() {
    this.busy.set(true);
    this.api.remove(this.user().id).subscribe({
      next: () => this.deleted.emit(this.user().id),
      error: err => {
        this.error.set(extractErrors(err)[0]);
        this.busy.set(false);
        this.confirming.set(false);
      },
    });
  }
}