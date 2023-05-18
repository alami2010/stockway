import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBonReceptionItem } from '../bon-reception-item.model';

@Component({
  selector: 'jhi-bon-reception-item-detail',
  templateUrl: './bon-reception-item-detail.component.html',
})
export class BonReceptionItemDetailComponent implements OnInit {
  bonReceptionItem: IBonReceptionItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bonReceptionItem }) => {
      this.bonReceptionItem = bonReceptionItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
