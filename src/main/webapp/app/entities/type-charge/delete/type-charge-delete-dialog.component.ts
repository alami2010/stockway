import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeCharge } from '../type-charge.model';
import { TypeChargeService } from '../service/type-charge.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './type-charge-delete-dialog.component.html',
})
export class TypeChargeDeleteDialogComponent {
  typeCharge?: ITypeCharge;

  constructor(protected typeChargeService: TypeChargeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeChargeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
