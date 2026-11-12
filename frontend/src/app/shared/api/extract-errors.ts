import { HttpErrorResponse } from '@angular/common/http';

/** Turns an API error into a list of readable messages (server ProblemDetail: `errors` or `detail`). */
export function extractErrors(err: unknown, fallback = 'Une erreur est survenue.'): string[] {
  if (err instanceof HttpErrorResponse) {
    if (err.status === 0) {
      return ['Serveur injoignable. Vérifiez que le backend est démarré.'];
    }
    const body = err.error;
    if (body && typeof body === 'object') {
      const list = Array.isArray(body.errors) ? body.errors.filter((e: unknown) => typeof e === 'string') : [];
      if (list.length) return list;
      if (typeof body.detail === 'string') return [body.detail];
    }
    if (err.status === 403) return ["Vous n'avez pas le droit d'effectuer cette action."];
  }
  return [fallback];
}