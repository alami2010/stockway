import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharge } from '../charge.model';
import { ChargeService } from '../service/charge.service';

@Injectable({ providedIn: 'root' })
export class ChargeRoutingResolveService implements Resolve<ICharge | null> {
  constructor(protected service: ChargeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharge | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((charge: HttpResponse<ICharge>) => {
          if (charge.body) {
            return of(charge.body);
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
