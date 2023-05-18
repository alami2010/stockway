import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BonReceptionFormService } from './bon-reception-form.service';
import { BonReceptionService } from '../service/bon-reception.service';
import { IBonReception } from '../bon-reception.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

import { BonReceptionUpdateComponent } from './bon-reception-update.component';

describe('BonReception Management Update Component', () => {
  let comp: BonReceptionUpdateComponent;
  let fixture: ComponentFixture<BonReceptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bonReceptionFormService: BonReceptionFormService;
  let bonReceptionService: BonReceptionService;
  let fournisseurService: FournisseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BonReceptionUpdateComponent],
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
      .overrideTemplate(BonReceptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BonReceptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bonReceptionFormService = TestBed.inject(BonReceptionFormService);
    bonReceptionService = TestBed.inject(BonReceptionService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fournisseur query and add missing value', () => {
      const bonReception: IBonReception = { id: 456 };
      const fournisseur: IFournisseur = { id: 50429 };
      bonReception.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 56183 }];
      jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bonReception });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
        fournisseurCollection,
        ...additionalFournisseurs.map(expect.objectContaining)
      );
      expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bonReception: IBonReception = { id: 456 };
      const fournisseur: IFournisseur = { id: 55019 };
      bonReception.fournisseur = fournisseur;

      activatedRoute.data = of({ bonReception });
      comp.ngOnInit();

      expect(comp.fournisseursSharedCollection).toContain(fournisseur);
      expect(comp.bonReception).toEqual(bonReception);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonReception>>();
      const bonReception = { id: 123 };
      jest.spyOn(bonReceptionFormService, 'getBonReception').mockReturnValue(bonReception);
      jest.spyOn(bonReceptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonReception });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bonReception }));
      saveSubject.complete();

      // THEN
      expect(bonReceptionFormService.getBonReception).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bonReceptionService.update).toHaveBeenCalledWith(expect.objectContaining(bonReception));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonReception>>();
      const bonReception = { id: 123 };
      jest.spyOn(bonReceptionFormService, 'getBonReception').mockReturnValue({ id: null });
      jest.spyOn(bonReceptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonReception: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bonReception }));
      saveSubject.complete();

      // THEN
      expect(bonReceptionFormService.getBonReception).toHaveBeenCalled();
      expect(bonReceptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonReception>>();
      const bonReception = { id: 123 };
      jest.spyOn(bonReceptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonReception });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bonReceptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFournisseur', () => {
      it('Should forward to fournisseurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fournisseurService, 'compareFournisseur');
        comp.compareFournisseur(entity, entity2);
        expect(fournisseurService.compareFournisseur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
