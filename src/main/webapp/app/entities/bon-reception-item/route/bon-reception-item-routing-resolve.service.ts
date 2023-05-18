import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBonReceptionItem } from '../bon-reception-item.model';
import { BonReceptionItemService } from '../service/bon-reception-item.service';

@Injectable({ providedIn: 'root' })
export class BonReceptionItemRoutingResolveService implements Resolve<IBonReceptionItem | null> {
  constructor(protected service: BonReceptionItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBonReceptionItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bonReceptionItem: HttpResponse<IBonReceptionItem>) => {
          if (bonReceptionItem.body) {
            return of(bonReceptionItem.body);
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
