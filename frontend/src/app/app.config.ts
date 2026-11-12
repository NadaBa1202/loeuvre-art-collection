import { ApplicationConfig, inject, provideAppInitializer, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';   // NEW
import { provideRouter } from '@angular/router';
import { catchError, firstValueFrom, of } from 'rxjs';                          // NEW
import { AuthApi, authInterceptor } from '@entities/session';                   // NEW
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),                     // NEW
    // On every page load, try to restore the session from the refresh cookie.
    provideAppInitializer(() =>                                                 // NEW
      firstValueFrom(inject(AuthApi).refresh().pipe(catchError(() => of(null))))),
  ],
};