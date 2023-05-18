import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BonReceptionComponent } from './list/bon-reception.component';
import { BonReceptionDetailComponent } from './detail/bon-reception-detail.component';
import { BonReceptionUpdateComponent } from './update/bon-reception-update.component';
import { BonReceptionDeleteDialogComponent } from './delete/bon-reception-delete-dialog.component';
import { BonReceptionRoutingModule } from './route/bon-reception-routing.module';

@NgModule({
  imports: [SharedModule, BonReceptionRoutingModule],
  declarations: [BonReceptionComponent, BonReceptionDetailComponent, BonReceptionUpdateComponent, BonReceptionDeleteDialogComponent],
})
export class BonReceptionModule {}
