import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChargeFormService } from './charge-form.service';
import { ChargeService } from '../service/charge.service';
import { ICharge } from '../charge.model';
import { ITypeCharge } from 'app/entities/type-charge/type-charge.model';
import { TypeChargeService } from 'app/entities/type-charge/service/type-charge.service';

import { ChargeUpdateComponent } from './charge-update.component';

describe('Charge Management Update Component', () => {
  let comp: ChargeUpdateComponent;
  let fixture: ComponentFixture<ChargeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chargeFormService: ChargeFormService;
  let chargeService: ChargeService;
  let typeChargeService: TypeChargeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChargeUpdateComponent],
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
      .overrideTemplate(ChargeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChargeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chargeFormService = TestBed.inject(ChargeFormService);
    chargeService = TestBed.inject(ChargeService);
    typeChargeService = TestBed.inject(TypeChargeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeCharge query and add missing value', () => {
      const charge: ICharge = { id: 456 };
      const type: ITypeCharge = { id: 54277 };
      charge.type = type;

      const typeChargeCollection: ITypeCharge[] = [{ id: 48563 }];
      jest.spyOn(typeChargeService, 'query').mockReturnValue(of(new HttpResponse({ body: typeChargeCollection })));
      const additionalTypeCharges = [type];
      const expectedCollection: ITypeCharge[] = [...additionalTypeCharges, ...typeChargeCollection];
      jest.spyOn(typeChargeService, 'addTypeChargeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ charge });
      comp.ngOnInit();

      expect(typeChargeService.query).toHaveBeenCalled();
      expect(typeChargeService.addTypeChargeToCollectionIfMissing).toHaveBeenCalledWith(
        typeChargeCollection,
        ...additionalTypeCharges.map(expect.objectContaining)
      );
      expect(comp.typeChargesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const charge: ICharge = { id: 456 };
      const type: ITypeCharge = { id: 7255 };
      charge.type = type;

      activatedRoute.data = of({ charge });
      comp.ngOnInit();

      expect(comp.typeChargesSharedCollection).toContain(type);
      expect(comp.charge).toEqual(charge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharge>>();
      const charge = { id: 123 };
      jest.spyOn(chargeFormService, 'getCharge').mockReturnValue(charge);
      jest.spyOn(chargeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ charge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: charge }));
      saveSubject.complete();

      // THEN
      expect(chargeFormService.getCharge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chargeService.update).toHaveBeenCalledWith(expect.objectContaining(charge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharge>>();
      const charge = { id: 123 };
      jest.spyOn(chargeFormService, 'getCharge').mockReturnValue({ id: null });
      jest.spyOn(chargeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ charge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: charge }));
      saveSubject.complete();

      // THEN
      expect(chargeFormService.getCharge).toHaveBeenCalled();
      expect(chargeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICharge>>();
      const charge = { id: 123 };
      jest.spyOn(chargeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ charge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chargeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeCharge', () => {
      it('Should forward to typeChargeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeChargeService, 'compareTypeCharge');
        comp.compareTypeCharge(entity, entity2);
        expect(typeChargeService.compareTypeCharge).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
