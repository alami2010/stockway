import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICharge } from '../charge.model';

@Component({
  selector: 'jhi-charge-detail',
  templateUrl: './charge-detail.component.html',
})
export class ChargeDetailComponent implements OnInit {
  charge: ICharge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ charge }) => {
      this.charge = charge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
