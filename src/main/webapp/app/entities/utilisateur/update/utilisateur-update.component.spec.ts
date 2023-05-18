import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UtilisateurFormService } from './utilisateur-form.service';
import { UtilisateurService } from '../service/utilisateur.service';
import { IUtilisateur } from '../utilisateur.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRole } from 'app/entities/role/role.model';
import { RoleService } from 'app/entities/role/service/role.service';

import { UtilisateurUpdateComponent } from './utilisateur-update.component';

describe('Utilisateur Management Update Component', () => {
  let comp: UtilisateurUpdateComponent;
  let fixture: ComponentFixture<UtilisateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utilisateurFormService: UtilisateurFormService;
  let utilisateurService: UtilisateurService;
  let userService: UserService;
  let roleService: RoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UtilisateurUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UtilisateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilisateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilisateurFormService = TestBed.inject(UtilisateurFormService);
    utilisateurService = TestBed.inject(UtilisateurService);
    userService = TestBed.inject(UserService);
    roleService = TestBed.inject(RoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const utilisateur: IUtilisateur = { id: 456 };
      const user: IUser = { id: 74654 };
      utilisateur.user = user;

      const userCollection: IUser[] = [{ id: 95229 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Role query and add missing value', () => {
      const utilisateur: IUtilisateur = { id: 456 };
      const roles: IRole[] = [{ id: 94873 }];
      utilisateur.roles = roles;

      const roleCollection: IRole[] = [{ id: 39821 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleCollection })));
      const additionalRoles = [...roles];
      const expectedCollection: IRole[] = [...additionalRoles, ...roleCollection];
      jest.spyOn(roleService, 'addRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleToCollectionIfMissing).toHaveBeenCalledWith(
        roleCollection,
        ...additionalRoles.map(expect.objectContaining)
      );
      expect(comp.rolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const utilisateur: IUtilisateur = { id: 456 };
      const user: IUser = { id: 57517 };
      utilisateur.user = user;
      const roles: IRole = { id: 57826 };
      utilisateur.roles = [roles];

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.rolesSharedCollection).toContain(roles);
      expect(comp.utilisateur).toEqual(utilisateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateur>>();
      const utilisateur = { id: 123 };
      jest.spyOn(utilisateurFormService, 'getUtilisateur').mockReturnValue(utilisateur);
      jest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisateur }));
      saveSubject.complete();

      // THEN
      expect(utilisateurFormService.getUtilisateur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilisateurService.update).toHaveBeenCalledWith(expect.objectContaining(utilisateur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateur>>();
      const utilisateur = { id: 123 };
      jest.spyOn(utilisateurFormService, 'getUtilisateur').mockReturnValue({ id: null });
      jest.spyOn(utilisateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilisateur }));
      saveSubject.complete();

      // THEN
      expect(utilisateurFormService.getUtilisateur).toHaveBeenCalled();
      expect(utilisateurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilisateur>>();
      const utilisateur = { id: 123 };
      jest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilisateurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRole', () => {
      it('Should forward to roleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roleService, 'compareRole');
        comp.compareRole(entity, entity2);
        expect(roleService.compareRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
