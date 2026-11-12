import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStore } from '../model/session.store';
import { Role } from '../model/types';

export const authGuard: CanActivateFn = (_route, state) => {
  return inject(SessionStore).isAuthenticated()
    ? true
    : inject(Router).createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};

export const guestGuard: CanActivateFn = () => {
  return inject(SessionStore).isAuthenticated() ? inject(Router).createUrlTree(['/artistes']) : true;
};

export const roleGuard = (...roles: Role[]): CanActivateFn => () => {
  return inject(SessionStore).hasAnyRole(roles) ? true : inject(Router).createUrlTree(['/artistes']);
};