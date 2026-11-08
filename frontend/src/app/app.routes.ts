import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', loadComponent: () => import('@pages/landing').then(m => m.Landing) },
  { path: 'login', loadComponent: () => import('@pages/login').then(m => m.Login) },
  { path: 'register', loadComponent: () => import('@pages/register').then(m => m.Register) },
  { path: '**', redirectTo: '' },
];