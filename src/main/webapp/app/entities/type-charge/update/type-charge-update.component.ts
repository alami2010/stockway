import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TypeChargeFormService, TypeChargeFormGroup } from './type-charge-form.service';
import { ITypeCharge } from '../type-charge.model';
import { TypeChargeService } from '../service/type-charge.service';

@Component({
  selector: 'jhi-type-charge-update',
  templateUrl: './type-charge-update.component.html',
})
export class TypeChargeUpdateComponent implements OnInit {
  isSaving = false;
  typeCharge: ITypeCharge | null = null;

  editForm: TypeChargeFormGroup = this.typeChargeFormService.createTypeChargeFormGroup();

  constructor(
    protected typeChargeService: TypeChargeService,
    protected typeChargeFormService: TypeChargeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeCharge }) => {
      this.typeCharge = typeCharge;
      if (typeCharge) {
        this.updateForm(typeCharge);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeCharge = this.typeChargeFormService.getTypeCharge(this.editForm);
    if (typeCharge.id !== null) {
      this.subscribeToSaveResponse(this.typeChargeService.update(typeCharge));
    } else {
      this.subscribeToSaveResponse(this.typeChargeService.create(typeCharge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeCharge>>): void {
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

  protected updateForm(typeCharge: ITypeCharge): void {
    this.typeCharge = typeCharge;
    this.typeChargeFormService.resetForm(this.editForm, typeCharge);
  }
}
