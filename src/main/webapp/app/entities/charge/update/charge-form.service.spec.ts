import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../charge.test-samples';

import { ChargeFormService } from './charge-form.service';

describe('Charge Form Service', () => {
  let service: ChargeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChargeFormService);
  });

  describe('Service methods', () => {
    describe('createChargeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChargeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            valeur: expect.any(Object),
            dateCreation: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing ICharge should create a new form with FormGroup', () => {
        const formGroup = service.createChargeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            valeur: expect.any(Object),
            dateCreation: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getCharge', () => {
      it('should return NewCharge for default Charge initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createChargeFormGroup(sampleWithNewData);

        const charge = service.getCharge(formGroup) as any;

        expect(charge).toMatchObject(sampleWithNewData);
      });

      it('should return NewCharge for empty Charge initial value', () => {
        const formGroup = service.createChargeFormGroup();

        const charge = service.getCharge(formGroup) as any;

        expect(charge).toMatchObject({});
      });

      it('should return ICharge', () => {
        const formGroup = service.createChargeFormGroup(sampleWithRequiredData);

        const charge = service.getCharge(formGroup) as any;

        expect(charge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICharge should not enable id FormControl', () => {
        const formGroup = service.createChargeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCharge should disable id FormControl', () => {
        const formGroup = service.createChargeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
