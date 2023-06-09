import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';

@Injectable({ providedIn: 'root' })
export class UtilisateurRoutingResolveService implements Resolve<IUtilisateur | null> {
  constructor(protected service: UtilisateurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUtilisateur | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((utilisateur: HttpResponse<IUtilisateur>) => {
          if (utilisateur.body) {
            return of(utilisateur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
