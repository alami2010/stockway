import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../utilisateur.test-samples';

import { UtilisateurFormService } from './utilisateur-form.service';

describe('Utilisateur Form Service', () => {
  let service: UtilisateurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilisateurFormService);
  });

  describe('Service methods', () => {
    describe('createUtilisateurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtilisateurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            dateCreation: expect.any(Object),
            status: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            information: expect.any(Object),
            type: expect.any(Object),
            user: expect.any(Object),
            roles: expect.any(Object),
          })
        );
      });

      it('passing IUtilisateur should create a new form with FormGroup', () => {
        const formGroup = service.createUtilisateurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            dateCreation: expect.any(Object),
            status: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            information: expect.any(Object),
            type: expect.any(Object),
            user: expect.any(Object),
            roles: expect.any(Object),
          })
        );
      });
    });

    describe('getUtilisateur', () => {
      it('should return NewUtilisateur for default Utilisateur initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUtilisateurFormGroup(sampleWithNewData);

        const utilisateur = service.getUtilisateur(formGroup) as any;

        expect(utilisateur).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtilisateur for empty Utilisateur initial value', () => {
        const formGroup = service.createUtilisateurFormGroup();

        const utilisateur = service.getUtilisateur(formGroup) as any;

        expect(utilisateur).toMatchObject({});
      });

      it('should return IUtilisateur', () => {
        const formGroup = service.createUtilisateurFormGroup(sampleWithRequiredData);

        const utilisateur = service.getUtilisateur(formGroup) as any;

        expect(utilisateur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtilisateur should not enable id FormControl', () => {
        const formGroup = service.createUtilisateurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtilisateur should disable id FormControl', () => {
        const formGroup = service.createUtilisateurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
