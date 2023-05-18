import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeChargeComponent } from './list/type-charge.component';
import { TypeChargeDetailComponent } from './detail/type-charge-detail.component';
import { TypeChargeUpdateComponent } from './update/type-charge-update.component';
import { TypeChargeDeleteDialogComponent } from './delete/type-charge-delete-dialog.component';
import { TypeChargeRoutingModule } from './route/type-charge-routing.module';

@NgModule({
  imports: [SharedModule, TypeChargeRoutingModule],
  declarations: [TypeChargeComponent, TypeChargeDetailComponent, TypeChargeUpdateComponent, TypeChargeDeleteDialogComponent],
})
export class TypeChargeModule {}
