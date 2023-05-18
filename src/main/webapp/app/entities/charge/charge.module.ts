import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChargeComponent } from './list/charge.component';
import { ChargeDetailComponent } from './detail/charge-detail.component';
import { ChargeUpdateComponent } from './update/charge-update.component';
import { ChargeDeleteDialogComponent } from './delete/charge-delete-dialog.component';
import { ChargeRoutingModule } from './route/charge-routing.module';

@NgModule({
  imports: [SharedModule, ChargeRoutingModule],
  declarations: [ChargeComponent, ChargeDetailComponent, ChargeUpdateComponent, ChargeDeleteDialogComponent],
})
export class ChargeModule {}
