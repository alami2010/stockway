import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICharge, NewCharge } from '../charge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICharge for edit and NewChargeFormGroupInput for create.
 */
type ChargeFormGroupInput = ICharge | PartialWithRequiredKeyOf<NewCharge>;

type ChargeFormDefaults = Pick<NewCharge, 'id'>;

type ChargeFormGroupContent = {
  id: FormControl<ICharge['id'] | NewCharge['id']>;
  nom: FormControl<ICharge['nom']>;
  valeur: FormControl<ICharge['valeur']>;
  dateCreation: FormControl<ICharge['dateCreation']>;
  type: FormControl<ICharge['type']>;
};

export type ChargeFormGroup = FormGroup<ChargeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChargeFormService {
  createChargeFormGroup(charge: ChargeFormGroupInput = { id: null }): ChargeFormGroup {
    const chargeRawValue = {
      ...this.getFormDefaults(),
      ...charge,
    };
    return new FormGroup<ChargeFormGroupContent>({
      id: new FormControl(
        { value: chargeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(chargeRawValue.nom),
      valeur: new FormControl(chargeRawValue.valeur),
      dateCreation: new FormControl(chargeRawValue.dateCreation),
      type: new FormControl(chargeRawValue.type),
    });
  }

  getCharge(form: ChargeFormGroup): ICharge | NewCharge {
    return form.getRawValue() as ICharge | NewCharge;
  }

  resetForm(form: ChargeFormGroup, charge: ChargeFormGroupInput): void {
    const chargeRawValue = { ...this.getFormDefaults(), ...charge };
    form.reset(
      {
        ...chargeRawValue,
        id: { value: chargeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ChargeFormDefaults {
    return {
      id: null,
    };
  }
}
