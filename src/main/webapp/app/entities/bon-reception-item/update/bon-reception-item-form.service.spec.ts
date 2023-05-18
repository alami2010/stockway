import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bon-reception-item.test-samples';

import { BonReceptionItemFormService } from './bon-reception-item-form.service';

describe('BonReceptionItem Form Service', () => {
  let service: BonReceptionItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BonReceptionItemFormService);
  });

  describe('Service methods', () => {
    describe('createBonReceptionItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBonReceptionItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            qte: expect.any(Object),
            bon: expect.any(Object),
            article: expect.any(Object),
          })
        );
      });

      it('passing IBonReceptionItem should create a new form with FormGroup', () => {
        const formGroup = service.createBonReceptionItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            qte: expect.any(Object),
            bon: expect.any(Object),
            article: expect.any(Object),
          })
        );
      });
    });

    describe('getBonReceptionItem', () => {
      it('should return NewBonReceptionItem for default BonReceptionItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBonReceptionItemFormGroup(sampleWithNewData);

        const bonReceptionItem = service.getBonReceptionItem(formGroup) as any;

        expect(bonReceptionItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewBonReceptionItem for empty BonReceptionItem initial value', () => {
        const formGroup = service.createBonReceptionItemFormGroup();

        const bonReceptionItem = service.getBonReceptionItem(formGroup) as any;

        expect(bonReceptionItem).toMatchObject({});
      });

      it('should return IBonReceptionItem', () => {
        const formGroup = service.createBonReceptionItemFormGroup(sampleWithRequiredData);

        const bonReceptionItem = service.getBonReceptionItem(formGroup) as any;

        expect(bonReceptionItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBonReceptionItem should not enable id FormControl', () => {
        const formGroup = service.createBonReceptionItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBonReceptionItem should disable id FormControl', () => {
        const formGroup = service.createBonReceptionItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
