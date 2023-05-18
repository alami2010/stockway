import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeCharge, NewTypeCharge } from '../type-charge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeCharge for edit and NewTypeChargeFormGroupInput for create.
 */
type TypeChargeFormGroupInput = ITypeCharge | PartialWithRequiredKeyOf<NewTypeCharge>;

type TypeChargeFormDefaults = Pick<NewTypeCharge, 'id'>;

type TypeChargeFormGroupContent = {
  id: FormControl<ITypeCharge['id'] | NewTypeCharge['id']>;
  nom: FormControl<ITypeCharge['nom']>;
};

export type TypeChargeFormGroup = FormGroup<TypeChargeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeChargeFormService {
  createTypeChargeFormGroup(typeCharge: TypeChargeFormGroupInput = { id: null }): TypeChargeFormGroup {
    const typeChargeRawValue = {
      ...this.getFormDefaults(),
      ...typeCharge,
    };
    return new FormGroup<TypeChargeFormGroupContent>({
      id: new FormControl(
        { value: typeChargeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(typeChargeRawValue.nom),
    });
  }

  getTypeCharge(form: TypeChargeFormGroup): ITypeCharge | NewTypeCharge {
    return form.getRawValue() as ITypeCharge | NewTypeCharge;
  }

  resetForm(form: TypeChargeFormGroup, typeCharge: TypeChargeFormGroupInput): void {
    const typeChargeRawValue = { ...this.getFormDefaults(), ...typeCharge };
    form.reset(
      {
        ...typeChargeRawValue,
        id: { value: typeChargeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TypeChargeFormDefaults {
    return {
      id: null,
    };
  }
}
