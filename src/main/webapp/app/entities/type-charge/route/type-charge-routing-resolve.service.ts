import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeCharge } from '../type-charge.model';
import { TypeChargeService } from '../service/type-charge.service';

@Injectable({ providedIn: 'root' })
export class TypeChargeRoutingResolveService implements Resolve<ITypeCharge | null> {
  constructor(protected service: TypeChargeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeCharge | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeCharge: HttpResponse<ITypeCharge>) => {
          if (typeCharge.body) {
            return of(typeCharge.body);
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
