import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bon-reception.test-samples';

import { BonReceptionFormService } from './bon-reception-form.service';

describe('BonReception Form Service', () => {
  let service: BonReceptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BonReceptionFormService);
  });

  describe('Service methods', () => {
    describe('createBonReceptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBonReceptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            informaton: expect.any(Object),
            numFacture: expect.any(Object),
            numBl: expect.any(Object),
            dateCreation: expect.any(Object),
            fournisseur: expect.any(Object),
          })
        );
      });

      it('passing IBonReception should create a new form with FormGroup', () => {
        const formGroup = service.createBonReceptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            informaton: expect.any(Object),
            numFacture: expect.any(Object),
            numBl: expect.any(Object),
            dateCreation: expect.any(Object),
            fournisseur: expect.any(Object),
          })
        );
      });
    });

    describe('getBonReception', () => {
      it('should return NewBonReception for default BonReception initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBonReceptionFormGroup(sampleWithNewData);

        const bonReception = service.getBonReception(formGroup) as any;

        expect(bonReception).toMatchObject(sampleWithNewData);
      });

      it('should return NewBonReception for empty BonReception initial value', () => {
        const formGroup = service.createBonReceptionFormGroup();

        const bonReception = service.getBonReception(formGroup) as any;

        expect(bonReception).toMatchObject({});
      });

      it('should return IBonReception', () => {
        const formGroup = service.createBonReceptionFormGroup(sampleWithRequiredData);

        const bonReception = service.getBonReception(formGroup) as any;

        expect(bonReception).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBonReception should not enable id FormControl', () => {
        const formGroup = service.createBonReceptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBonReception should disable id FormControl', () => {
        const formGroup = service.createBonReceptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
