import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeCharge } from '../type-charge.model';

@Component({
  selector: 'jhi-type-charge-detail',
  templateUrl: './type-charge-detail.component.html',
})
export class TypeChargeDetailComponent implements OnInit {
  typeCharge: ITypeCharge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeCharge }) => {
      this.typeCharge = typeCharge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
