import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BonReceptionItemComponent } from '../list/bon-reception-item.component';
import { BonReceptionItemDetailComponent } from '../detail/bon-reception-item-detail.component';
import { BonReceptionItemUpdateComponent } from '../update/bon-reception-item-update.component';
import { BonReceptionItemRoutingResolveService } from './bon-reception-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bonReceptionItemRoute: Routes = [
  {
    path: '',
    component: BonReceptionItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BonReceptionItemDetailComponent,
    resolve: {
      bonReceptionItem: BonReceptionItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BonReceptionItemUpdateComponent,
    resolve: {
      bonReceptionItem: BonReceptionItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BonReceptionItemUpdateComponent,
    resolve: {
      bonReceptionItem: BonReceptionItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bonReceptionItemRoute)],
  exports: [RouterModule],
})
export class BonReceptionItemRoutingModule {}
