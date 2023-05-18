import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-charge.test-samples';

import { TypeChargeFormService } from './type-charge-form.service';

describe('TypeCharge Form Service', () => {
  let service: TypeChargeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeChargeFormService);
  });

  describe('Service methods', () => {
    describe('createTypeChargeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeChargeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
          })
        );
      });

      it('passing ITypeCharge should create a new form with FormGroup', () => {
        const formGroup = service.createTypeChargeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
          })
        );
      });
    });

    describe('getTypeCharge', () => {
      it('should return NewTypeCharge for default TypeCharge initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTypeChargeFormGroup(sampleWithNewData);

        const typeCharge = service.getTypeCharge(formGroup) as any;

        expect(typeCharge).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeCharge for empty TypeCharge initial value', () => {
        const formGroup = service.createTypeChargeFormGroup();

        const typeCharge = service.getTypeCharge(formGroup) as any;

        expect(typeCharge).toMatchObject({});
      });

      it('should return ITypeCharge', () => {
        const formGroup = service.createTypeChargeFormGroup(sampleWithRequiredData);

        const typeCharge = service.getTypeCharge(formGroup) as any;

        expect(typeCharge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeCharge should not enable id FormControl', () => {
        const formGroup = service.createTypeChargeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeCharge should disable id FormControl', () => {
        const formGroup = service.createTypeChargeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
