import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBonReception } from '../bon-reception.model';
import { BonReceptionService } from '../service/bon-reception.service';

@Injectable({ providedIn: 'root' })
export class BonReceptionRoutingResolveService implements Resolve<IBonReception | null> {
  constructor(protected service: BonReceptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBonReception | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bonReception: HttpResponse<IBonReception>) => {
          if (bonReception.body) {
            return of(bonReception.body);
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
