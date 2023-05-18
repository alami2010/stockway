import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BonReceptionComponent } from '../list/bon-reception.component';
import { BonReceptionDetailComponent } from '../detail/bon-reception-detail.component';
import { BonReceptionUpdateComponent } from '../update/bon-reception-update.component';
import { BonReceptionRoutingResolveService } from './bon-reception-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bonReceptionRoute: Routes = [
  {
    path: '',
    component: BonReceptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BonReceptionDetailComponent,
    resolve: {
      bonReception: BonReceptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BonReceptionUpdateComponent,
    resolve: {
      bonReception: BonReceptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BonReceptionUpdateComponent,
    resolve: {
      bonReception: BonReceptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bonReceptionRoute)],
  exports: [RouterModule],
})
export class BonReceptionRoutingModule {}
