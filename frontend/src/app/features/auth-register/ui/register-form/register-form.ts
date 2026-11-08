import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';

function passwordsMatch(group: AbstractControl): ValidationErrors | null {
  return group.get('password')?.value === group.get('confirm')?.value ? null : { mismatch: true };
}

@Component({
  selector: 'app-register-form',
  imports: [ReactiveFormsModule],
  templateUrl: './register-form.html',
})
export class RegisterForm {
  private readonly fb = inject(FormBuilder);

  readonly showPassword = signal(false);
  readonly done = signal(false);

  // Field names match the User entity of the backend (nom, prenom, email, tel, password)
  readonly form = this.fb.nonNullable.group(
    {
      prenom: ['', [Validators.required, Validators.maxLength(50)]],
      nom: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      tel: ['', [Validators.required, Validators.pattern(/^\+?[0-9 ]{8,15}$/)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirm: ['', [Validators.required]],
    },
    { validators: passwordsMatch },
  );

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    // Static for now: POST /api/auth/register comes in the next step.
    const { confirm, password, ...data } = this.form.getRawValue();
    console.log('register', data);
    this.done.set(true);
  }
}