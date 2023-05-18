import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeChargeComponent } from '../list/type-charge.component';
import { TypeChargeDetailComponent } from '../detail/type-charge-detail.component';
import { TypeChargeUpdateComponent } from '../update/type-charge-update.component';
import { TypeChargeRoutingResolveService } from './type-charge-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const typeChargeRoute: Routes = [
  {
    path: '',
    component: TypeChargeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeChargeDetailComponent,
    resolve: {
      typeCharge: TypeChargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeChargeUpdateComponent,
    resolve: {
      typeCharge: TypeChargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeChargeUpdateComponent,
    resolve: {
      typeCharge: TypeChargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeChargeRoute)],
  exports: [RouterModule],
})
export class TypeChargeRoutingModule {}
