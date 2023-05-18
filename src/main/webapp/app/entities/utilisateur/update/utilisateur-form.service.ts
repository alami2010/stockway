import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id' | 'roles'>;

type UtilisateurFormGroupContent = {
  id: FormControl<IUtilisateur['id'] | NewUtilisateur['id']>;
  code: FormControl<IUtilisateur['code']>;
  nom: FormControl<IUtilisateur['nom']>;
  prenom: FormControl<IUtilisateur['prenom']>;
  dateCreation: FormControl<IUtilisateur['dateCreation']>;
  status: FormControl<IUtilisateur['status']>;
  phone: FormControl<IUtilisateur['phone']>;
  email: FormControl<IUtilisateur['email']>;
  information: FormControl<IUtilisateur['information']>;
  type: FormControl<IUtilisateur['type']>;
  user: FormControl<IUtilisateur['user']>;
  roles: FormControl<IUtilisateur['roles']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = {
      ...this.getFormDefaults(),
      ...utilisateur,
    };
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(utilisateurRawValue.code),
      nom: new FormControl(utilisateurRawValue.nom),
      prenom: new FormControl(utilisateurRawValue.prenom),
      dateCreation: new FormControl(utilisateurRawValue.dateCreation),
      status: new FormControl(utilisateurRawValue.status),
      phone: new FormControl(utilisateurRawValue.phone),
      email: new FormControl(utilisateurRawValue.email),
      information: new FormControl(utilisateurRawValue.information),
      type: new FormControl(utilisateurRawValue.type),
      user: new FormControl(utilisateurRawValue.user),
      roles: new FormControl(utilisateurRawValue.roles ?? []),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return form.getRawValue() as IUtilisateur | NewUtilisateur;
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = { ...this.getFormDefaults(), ...utilisateur };
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    return {
      id: null,
      roles: [],
    };
  }
}
