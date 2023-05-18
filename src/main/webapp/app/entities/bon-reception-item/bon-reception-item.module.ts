import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BonReceptionItemComponent } from './list/bon-reception-item.component';
import { BonReceptionItemDetailComponent } from './detail/bon-reception-item-detail.component';
import { BonReceptionItemUpdateComponent } from './update/bon-reception-item-update.component';
import { BonReceptionItemDeleteDialogComponent } from './delete/bon-reception-item-delete-dialog.component';
import { BonReceptionItemRoutingModule } from './route/bon-reception-item-routing.module';

@NgModule({
  imports: [SharedModule, BonReceptionItemRoutingModule],
  declarations: [
    BonReceptionItemComponent,
    BonReceptionItemDetailComponent,
    BonReceptionItemUpdateComponent,
    BonReceptionItemDeleteDialogComponent,
  ],
})
export class BonReceptionItemModule {}
