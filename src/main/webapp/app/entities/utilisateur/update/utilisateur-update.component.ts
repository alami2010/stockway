import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UtilisateurFormService, UtilisateurFormGroup } from './utilisateur-form.service';
import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';
import { UserType } from 'app/entities/enumerations/user-type.model';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;
  utilisateur: IUtilisateur | null = null;
  userTypeValues = Object.keys(UserType);

  usersSharedCollection: IUser[] = [];
  rolesSharedCollection: IRole[] = [];

  editForm: UtilisateurFormGroup = this.utilisateurFormService.createUtilisateurFormGroup();

  constructor(
    protected utilisateurService: UtilisateurService,
    protected utilisateurFormService: UtilisateurFormService,
    protected userService: UserService,
    protected roleService: RoleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareRole = (o1: IRole | null, o2: IRole | null): boolean => this.roleService.compareRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      this.utilisateur = utilisateur;
      if (utilisateur) {
        this.updateForm(utilisateur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateur = this.utilisateurFormService.getUtilisateur(this.editForm);
    if (utilisateur.id !== null) {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    } else {
      this.subscribeToSaveResponse(this.utilisateurService.create(utilisateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateur>>): void {
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

  protected updateForm(utilisateur: IUtilisateur): void {
    this.utilisateur = utilisateur;
    this.utilisateurFormService.resetForm(this.editForm, utilisateur);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, utilisateur.user);
    this.rolesSharedCollection = this.roleService.addRoleToCollectionIfMissing<IRole>(
      this.rolesSharedCollection,
      ...(utilisateur.roles ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.utilisateur?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.roleService
      .query()
      .pipe(map((res: HttpResponse<IRole[]>) => res.body ?? []))
      .pipe(map((roles: IRole[]) => this.roleService.addRoleToCollectionIfMissing<IRole>(roles, ...(this.utilisateur?.roles ?? []))))
      .subscribe((roles: IRole[]) => (this.rolesSharedCollection = roles));
  }
}
