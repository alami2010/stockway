import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BonReceptionFormService, BonReceptionFormGroup } from './bon-reception-form.service';
import { IBonReception } from '../bon-reception.model';
import { BonReceptionService } from '../service/bon-reception.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  selector: 'jhi-bon-reception-update',
  templateUrl: './bon-reception-update.component.html',
})
export class BonReceptionUpdateComponent implements OnInit {
  isSaving = false;
  bonReception: IBonReception | null = null;

  fournisseursSharedCollection: IFournisseur[] = [];

  editForm: BonReceptionFormGroup = this.bonReceptionFormService.createBonReceptionFormGroup();

  constructor(
    protected bonReceptionService: BonReceptionService,
    protected bonReceptionFormService: BonReceptionFormService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bonReception }) => {
      this.bonReception = bonReception;
      if (bonReception) {
        this.updateForm(bonReception);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bonReception = this.bonReceptionFormService.getBonReception(this.editForm);
    if (bonReception.id !== null) {
      this.subscribeToSaveResponse(this.bonReceptionService.update(bonReception));
    } else {
      this.subscribeToSaveResponse(this.bonReceptionService.create(bonReception));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBonReception>>): void {
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

  protected updateForm(bonReception: IBonReception): void {
    this.bonReception = bonReception;
    this.bonReceptionFormService.resetForm(this.editForm, bonReception);

    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(
      this.fournisseursSharedCollection,
      bonReception.fournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.bonReception?.fournisseur)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));
  }
}
