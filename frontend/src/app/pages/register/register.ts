import { Component } from '@angular/core';
import { AuthLayout } from '@widgets/auth-layout';
import { RegisterForm } from '@features/auth-register';

@Component({
  selector: 'app-register',
  imports: [AuthLayout, RegisterForm],
  templateUrl: './register.html',
})
export class Register {}