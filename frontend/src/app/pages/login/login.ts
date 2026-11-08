import { Component } from '@angular/core';
import { AuthLayout } from '@widgets/auth-layout';
import { LoginForm } from '@features/auth-login';

@Component({
  selector: 'app-login',
  imports: [AuthLayout, LoginForm],
  templateUrl: './login.html',
})
export class Login {}