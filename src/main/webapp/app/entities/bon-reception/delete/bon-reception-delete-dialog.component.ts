import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBonReception } from '../bon-reception.model';
import { BonReceptionService } from '../service/bon-reception.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bon-reception-delete-dialog.component.html',
})
export class BonReceptionDeleteDialogComponent {
  bonReception?: IBonReception;

  constructor(protected bonReceptionService: BonReceptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bonReceptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
