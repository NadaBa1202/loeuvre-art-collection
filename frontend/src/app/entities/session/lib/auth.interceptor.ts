import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthApi } from '../api/auth.api';
import { SessionStore } from '../model/session.store';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const session = inject(SessionStore);
  const auth = inject(AuthApi);
  const router = inject(Router);

  if (!req.url.startsWith('/api/')) return next(req);
  // login / register / refresh / logout never carry the access token
  if (req.url.startsWith('/api/auth/')) return next(req);

  const withToken = (r: HttpRequest<unknown>) => {
    const token = session.accessToken;
    return token ? r.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : r;
  };

  return next(withToken(req)).pipe(
    catchError(err => {
      if (err instanceof HttpErrorResponse && err.status === 401) {
        return auth.refresh().pipe(
          catchError(refreshErr => {          // session really over
            session.clear();
            router.navigate(['/login']);
            return throwError(() => refreshErr);
          }),
          switchMap(() => next(withToken(req))),   // retry once with the new token
        );
      }
      return throwError(() => err);
    }),
  );
};