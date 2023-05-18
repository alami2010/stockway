import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBonReceptionItem, NewBonReceptionItem } from '../bon-reception-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBonReceptionItem for edit and NewBonReceptionItemFormGroupInput for create.
 */
type BonReceptionItemFormGroupInput = IBonReceptionItem | PartialWithRequiredKeyOf<NewBonReceptionItem>;

type BonReceptionItemFormDefaults = Pick<NewBonReceptionItem, 'id'>;

type BonReceptionItemFormGroupContent = {
  id: FormControl<IBonReceptionItem['id'] | NewBonReceptionItem['id']>;
  qte: FormControl<IBonReceptionItem['qte']>;
  bon: FormControl<IBonReceptionItem['bon']>;
  article: FormControl<IBonReceptionItem['article']>;
};

export type BonReceptionItemFormGroup = FormGroup<BonReceptionItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BonReceptionItemFormService {
  createBonReceptionItemFormGroup(bonReceptionItem: BonReceptionItemFormGroupInput = { id: null }): BonReceptionItemFormGroup {
    const bonReceptionItemRawValue = {
      ...this.getFormDefaults(),
      ...bonReceptionItem,
    };
    return new FormGroup<BonReceptionItemFormGroupContent>({
      id: new FormControl(
        { value: bonReceptionItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      qte: new FormControl(bonReceptionItemRawValue.qte),
      bon: new FormControl(bonReceptionItemRawValue.bon),
      article: new FormControl(bonReceptionItemRawValue.article),
    });
  }

  getBonReceptionItem(form: BonReceptionItemFormGroup): IBonReceptionItem | NewBonReceptionItem {
    return form.getRawValue() as IBonReceptionItem | NewBonReceptionItem;
  }

  resetForm(form: BonReceptionItemFormGroup, bonReceptionItem: BonReceptionItemFormGroupInput): void {
    const bonReceptionItemRawValue = { ...this.getFormDefaults(), ...bonReceptionItem };
    form.reset(
      {
        ...bonReceptionItemRawValue,
        id: { value: bonReceptionItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BonReceptionItemFormDefaults {
    return {
      id: null,
    };
  }
}
