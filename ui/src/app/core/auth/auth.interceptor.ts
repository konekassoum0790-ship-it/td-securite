import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { API } from '../api/banque';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  let requete = req;
  if (auth.identifiantsBase64 && req.url.startsWith(API) && !req.headers.has('Authorization')) {
    requete = req.clone({
      setHeaders: { Authorization: 'Basic ' + auth.identifiantsBase64 },
    });
  }

  return next(requete).pipe(
    catchError((erreur: HttpErrorResponse) => {
      if (erreur.status === 401) {
        auth.deconnecter();
        router.navigateByUrl('/login');
      }
      return throwError(() => erreur);
    })
  );
};