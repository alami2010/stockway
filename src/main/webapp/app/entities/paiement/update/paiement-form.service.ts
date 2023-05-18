import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaiement, NewPaiement } from '../paiement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaiement for edit and NewPaiementFormGroupInput for create.
 */
type PaiementFormGroupInput = IPaiement | PartialWithRequiredKeyOf<NewPaiement>;

type PaiementFormDefaults = Pick<NewPaiement, 'id'>;

type PaiementFormGroupContent = {
  id: FormControl<IPaiement['id'] | NewPaiement['id']>;
  remunuration: FormControl<IPaiement['remunuration']>;
  dateCreation: FormControl<IPaiement['dateCreation']>;
  user: FormControl<IPaiement['user']>;
};

export type PaiementFormGroup = FormGroup<PaiementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaiementFormService {
  createPaiementFormGroup(paiement: PaiementFormGroupInput = { id: null }): PaiementFormGroup {
    const paiementRawValue = {
      ...this.getFormDefaults(),
      ...paiement,
    };
    return new FormGroup<PaiementFormGroupContent>({
      id: new FormControl(
        { value: paiementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      remunuration: new FormControl(paiementRawValue.remunuration),
      dateCreation: new FormControl(paiementRawValue.dateCreation),
      user: new FormControl(paiementRawValue.user),
    });
  }

  getPaiement(form: PaiementFormGroup): IPaiement | NewPaiement {
    return form.getRawValue() as IPaiement | NewPaiement;
  }

  resetForm(form: PaiementFormGroup, paiement: PaiementFormGroupInput): void {
    const paiementRawValue = { ...this.getFormDefaults(), ...paiement };
    form.reset(
      {
        ...paiementRawValue,
        id: { value: paiementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaiementFormDefaults {
    return {
      id: null,
    };
  }
}
