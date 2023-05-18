import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBonReceptionItem } from '../bon-reception-item.model';
import { BonReceptionItemService } from '../service/bon-reception-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bon-reception-item-delete-dialog.component.html',
})
export class BonReceptionItemDeleteDialogComponent {
  bonReceptionItem?: IBonReceptionItem;

  constructor(protected bonReceptionItemService: BonReceptionItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bonReceptionItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
