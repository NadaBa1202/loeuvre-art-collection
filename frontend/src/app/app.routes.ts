import { Routes } from '@angular/router';
import { authGuard, guestGuard, roleGuard } from '@entities/session';

export const routes: Routes = [
  { path: '', pathMatch: 'full', loadComponent: () => import('@pages/landing').then(m => m.Landing) },
  { path: 'login', canActivate: [guestGuard], loadComponent: () => import('@pages/login').then(m => m.Login) },
  { path: 'register', canActivate: [guestGuard], loadComponent: () => import('@pages/register').then(m => m.Register) },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('@widgets/app-shell').then(m => m.AppShell),
    children: [
      { path: 'artistes', loadComponent: () => import('@pages/artistes').then(m => m.ArtistesPage) },
      { path: 'artistes/nouveau', loadComponent: () => import('@pages/artiste-form').then(m => m.ArtisteFormPage) },
      {
        path: 'artistes/:id/modifier',
        canActivate: [roleGuard('ADMIN')],
        loadComponent: () => import('@pages/artiste-form').then(m => m.ArtisteFormPage),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];