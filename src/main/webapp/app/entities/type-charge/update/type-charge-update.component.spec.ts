import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeChargeFormService } from './type-charge-form.service';
import { TypeChargeService } from '../service/type-charge.service';
import { ITypeCharge } from '../type-charge.model';

import { TypeChargeUpdateComponent } from './type-charge-update.component';

describe('TypeCharge Management Update Component', () => {
  let comp: TypeChargeUpdateComponent;
  let fixture: ComponentFixture<TypeChargeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeChargeFormService: TypeChargeFormService;
  let typeChargeService: TypeChargeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypeChargeUpdateComponent],
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
      .overrideTemplate(TypeChargeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeChargeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeChargeFormService = TestBed.inject(TypeChargeFormService);
    typeChargeService = TestBed.inject(TypeChargeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeCharge: ITypeCharge = { id: 456 };

      activatedRoute.data = of({ typeCharge });
      comp.ngOnInit();

      expect(comp.typeCharge).toEqual(typeCharge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeCharge>>();
      const typeCharge = { id: 123 };
      jest.spyOn(typeChargeFormService, 'getTypeCharge').mockReturnValue(typeCharge);
      jest.spyOn(typeChargeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeCharge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeCharge }));
      saveSubject.complete();

      // THEN
      expect(typeChargeFormService.getTypeCharge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeChargeService.update).toHaveBeenCalledWith(expect.objectContaining(typeCharge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeCharge>>();
      const typeCharge = { id: 123 };
      jest.spyOn(typeChargeFormService, 'getTypeCharge').mockReturnValue({ id: null });
      jest.spyOn(typeChargeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeCharge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeCharge }));
      saveSubject.complete();

      // THEN
      expect(typeChargeFormService.getTypeCharge).toHaveBeenCalled();
      expect(typeChargeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeCharge>>();
      const typeCharge = { id: 123 };
      jest.spyOn(typeChargeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeCharge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeChargeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
