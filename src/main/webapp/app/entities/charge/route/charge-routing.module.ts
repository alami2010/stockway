import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChargeComponent } from '../list/charge.component';
import { ChargeDetailComponent } from '../detail/charge-detail.component';
import { ChargeUpdateComponent } from '../update/charge-update.component';
import { ChargeRoutingResolveService } from './charge-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const chargeRoute: Routes = [
  {
    path: '',
    component: ChargeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChargeDetailComponent,
    resolve: {
      charge: ChargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChargeUpdateComponent,
    resolve: {
      charge: ChargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChargeUpdateComponent,
    resolve: {
      charge: ChargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chargeRoute)],
  exports: [RouterModule],
})
export class ChargeRoutingModule {}
