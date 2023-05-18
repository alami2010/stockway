import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBonReception } from '../bon-reception.model';

@Component({
  selector: 'jhi-bon-reception-detail',
  templateUrl: './bon-reception-detail.component.html',
})
export class BonReceptionDetailComponent implements OnInit {
  bonReception: IBonReception | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bonReception }) => {
      this.bonReception = bonReception;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
