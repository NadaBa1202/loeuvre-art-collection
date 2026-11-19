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
      { path: 'dashboard', loadComponent: () => import('@pages/dashboard').then(m => m.DashboardPage) },
      { path: 'users', canActivate: [roleGuard('ADMIN')], loadComponent: () => import('@pages/users').then(m => m.UsersPage) },
      // Artists
      { path: 'artistes', loadComponent: () => import('@pages/artistes').then(m => m.ArtistesPage) },
      { path: 'artistes/nouveau', loadComponent: () => import('@pages/artiste-form').then(m => m.ArtisteFormPage) },
      {
        path: 'artistes/:id/modifier',
        canActivate: [roleGuard('ADMIN')],
        loadComponent: () => import('@pages/artiste-form').then(m => m.ArtisteFormPage),
      },
      { path: 'artistes/:id', loadComponent: () => import('@pages/artiste-detail').then(m => m.ArtisteDetailPage) },

      // Artworks
      { path: 'oeuvres', loadComponent: () => import('@pages/oeuvres').then(m => m.OeuvresPage) },
      { path: 'oeuvres/nouvelle', loadComponent: () => import('@pages/oeuvre-form').then(m => m.OeuvreFormPage) },
      {
        path: 'oeuvres/:id/modifier',
        canActivate: [roleGuard('ADMIN')],
        loadComponent: () => import('@pages/oeuvre-form').then(m => m.OeuvreFormPage),
      },
      { path: 'oeuvres/:id', loadComponent: () => import('@pages/oeuvre-detail').then(m => m.OeuvreDetailPage) },
    ],
  },
  { path: '**', redirectTo: '' },
];