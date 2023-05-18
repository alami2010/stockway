import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ChargeFormService, ChargeFormGroup } from './charge-form.service';
import { ICharge } from '../charge.model';
import { ChargeService } from '../service/charge.service';
import { ITypeCharge } from 'app/entities/type-charge/type-charge.model';
import { TypeChargeService } from 'app/entities/type-charge/service/type-charge.service';

@Component({
  selector: 'jhi-charge-update',
  templateUrl: './charge-update.component.html',
})
export class ChargeUpdateComponent implements OnInit {
  isSaving = false;
  charge: ICharge | null = null;

  typeChargesSharedCollection: ITypeCharge[] = [];

  editForm: ChargeFormGroup = this.chargeFormService.createChargeFormGroup();

  constructor(
    protected chargeService: ChargeService,
    protected chargeFormService: ChargeFormService,
    protected typeChargeService: TypeChargeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTypeCharge = (o1: ITypeCharge | null, o2: ITypeCharge | null): boolean => this.typeChargeService.compareTypeCharge(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ charge }) => {
      this.charge = charge;
      if (charge) {
        this.updateForm(charge);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const charge = this.chargeFormService.getCharge(this.editForm);
    if (charge.id !== null) {
      this.subscribeToSaveResponse(this.chargeService.update(charge));
    } else {
      this.subscribeToSaveResponse(this.chargeService.create(charge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharge>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(charge: ICharge): void {
    this.charge = charge;
    this.chargeFormService.resetForm(this.editForm, charge);

    this.typeChargesSharedCollection = this.typeChargeService.addTypeChargeToCollectionIfMissing<ITypeCharge>(
      this.typeChargesSharedCollection,
      charge.type
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeChargeService
      .query()
      .pipe(map((res: HttpResponse<ITypeCharge[]>) => res.body ?? []))
      .pipe(
        map((typeCharges: ITypeCharge[]) =>
          this.typeChargeService.addTypeChargeToCollectionIfMissing<ITypeCharge>(typeCharges, this.charge?.type)
        )
      )
      .subscribe((typeCharges: ITypeCharge[]) => (this.typeChargesSharedCollection = typeCharges));
  }
}
