import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBonReception, NewBonReception } from '../bon-reception.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBonReception for edit and NewBonReceptionFormGroupInput for create.
 */
type BonReceptionFormGroupInput = IBonReception | PartialWithRequiredKeyOf<NewBonReception>;

type BonReceptionFormDefaults = Pick<NewBonReception, 'id'>;

type BonReceptionFormGroupContent = {
  id: FormControl<IBonReception['id'] | NewBonReception['id']>;
  informaton: FormControl<IBonReception['informaton']>;
  numFacture: FormControl<IBonReception['numFacture']>;
  numBl: FormControl<IBonReception['numBl']>;
  dateCreation: FormControl<IBonReception['dateCreation']>;
  fournisseur: FormControl<IBonReception['fournisseur']>;
};

export type BonReceptionFormGroup = FormGroup<BonReceptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BonReceptionFormService {
  createBonReceptionFormGroup(bonReception: BonReceptionFormGroupInput = { id: null }): BonReceptionFormGroup {
    const bonReceptionRawValue = {
      ...this.getFormDefaults(),
      ...bonReception,
    };
    return new FormGroup<BonReceptionFormGroupContent>({
      id: new FormControl(
        { value: bonReceptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      informaton: new FormControl(bonReceptionRawValue.informaton),
      numFacture: new FormControl(bonReceptionRawValue.numFacture),
      numBl: new FormControl(bonReceptionRawValue.numBl),
      dateCreation: new FormControl(bonReceptionRawValue.dateCreation),
      fournisseur: new FormControl(bonReceptionRawValue.fournisseur),
    });
  }

  getBonReception(form: BonReceptionFormGroup): IBonReception | NewBonReception {
    return form.getRawValue() as IBonReception | NewBonReception;
  }

  resetForm(form: BonReceptionFormGroup, bonReception: BonReceptionFormGroupInput): void {
    const bonReceptionRawValue = { ...this.getFormDefaults(), ...bonReception };
    form.reset(
      {
        ...bonReceptionRawValue,
        id: { value: bonReceptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BonReceptionFormDefaults {
    return {
      id: null,
    };
  }
}
