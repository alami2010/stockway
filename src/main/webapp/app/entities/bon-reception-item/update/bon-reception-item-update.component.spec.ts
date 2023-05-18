import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BonReceptionItemFormService } from './bon-reception-item-form.service';
import { BonReceptionItemService } from '../service/bon-reception-item.service';
import { IBonReceptionItem } from '../bon-reception-item.model';
import { IBonReception } from 'app/entities/bon-reception/bon-reception.model';
import { BonReceptionService } from 'app/entities/bon-reception/service/bon-reception.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';

import { BonReceptionItemUpdateComponent } from './bon-reception-item-update.component';

describe('BonReceptionItem Management Update Component', () => {
  let comp: BonReceptionItemUpdateComponent;
  let fixture: ComponentFixture<BonReceptionItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bonReceptionItemFormService: BonReceptionItemFormService;
  let bonReceptionItemService: BonReceptionItemService;
  let bonReceptionService: BonReceptionService;
  let articleService: ArticleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BonReceptionItemUpdateComponent],
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
      .overrideTemplate(BonReceptionItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BonReceptionItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bonReceptionItemFormService = TestBed.inject(BonReceptionItemFormService);
    bonReceptionItemService = TestBed.inject(BonReceptionItemService);
    bonReceptionService = TestBed.inject(BonReceptionService);
    articleService = TestBed.inject(ArticleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BonReception query and add missing value', () => {
      const bonReceptionItem: IBonReceptionItem = { id: 456 };
      const bon: IBonReception = { id: 19305 };
      bonReceptionItem.bon = bon;

      const bonReceptionCollection: IBonReception[] = [{ id: 92178 }];
      jest.spyOn(bonReceptionService, 'query').mockReturnValue(of(new HttpResponse({ body: bonReceptionCollection })));
      const additionalBonReceptions = [bon];
      const expectedCollection: IBonReception[] = [...additionalBonReceptions, ...bonReceptionCollection];
      jest.spyOn(bonReceptionService, 'addBonReceptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bonReceptionItem });
      comp.ngOnInit();

      expect(bonReceptionService.query).toHaveBeenCalled();
      expect(bonReceptionService.addBonReceptionToCollectionIfMissing).toHaveBeenCalledWith(
        bonReceptionCollection,
        ...additionalBonReceptions.map(expect.objectContaining)
      );
      expect(comp.bonReceptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Article query and add missing value', () => {
      const bonReceptionItem: IBonReceptionItem = { id: 456 };
      const article: IArticle = { id: 76935 };
      bonReceptionItem.article = article;

      const articleCollection: IArticle[] = [{ id: 45767 }];
      jest.spyOn(articleService, 'query').mockReturnValue(of(new HttpResponse({ body: articleCollection })));
      const additionalArticles = [article];
      const expectedCollection: IArticle[] = [...additionalArticles, ...articleCollection];
      jest.spyOn(articleService, 'addArticleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bonReceptionItem });
      comp.ngOnInit();

      expect(articleService.query).toHaveBeenCalled();
      expect(articleService.addArticleToCollectionIfMissing).toHaveBeenCalledWith(
        articleCollection,
        ...additionalArticles.map(expect.objectContaining)
      );
      expect(comp.articlesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bonReceptionItem: IBonReceptionItem = { id: 456 };
      const bon: IBonReception = { id: 32517 };
      bonReceptionItem.bon = bon;
      const article: IArticle = { id: 44923 };
      bonReceptionItem.article = article;

      activatedRoute.data = of({ bonReceptionItem });
      comp.ngOnInit();

      expect(comp.bonReceptionsSharedCollection).toContain(bon);
      expect(comp.articlesSharedCollection).toContain(article);
      expect(comp.bonReceptionItem).toEqual(bonReceptionItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonReceptionItem>>();
      const bonReceptionItem = { id: 123 };
      jest.spyOn(bonReceptionItemFormService, 'getBonReceptionItem').mockReturnValue(bonReceptionItem);
      jest.spyOn(bonReceptionItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonReceptionItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bonReceptionItem }));
      saveSubject.complete();

      // THEN
      expect(bonReceptionItemFormService.getBonReceptionItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bonReceptionItemService.update).toHaveBeenCalledWith(expect.objectContaining(bonReceptionItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonReceptionItem>>();
      const bonReceptionItem = { id: 123 };
      jest.spyOn(bonReceptionItemFormService, 'getBonReceptionItem').mockReturnValue({ id: null });
      jest.spyOn(bonReceptionItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonReceptionItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bonReceptionItem }));
      saveSubject.complete();

      // THEN
      expect(bonReceptionItemFormService.getBonReceptionItem).toHaveBeenCalled();
      expect(bonReceptionItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonReceptionItem>>();
      const bonReceptionItem = { id: 123 };
      jest.spyOn(bonReceptionItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonReceptionItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bonReceptionItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBonReception', () => {
      it('Should forward to bonReceptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bonReceptionService, 'compareBonReception');
        comp.compareBonReception(entity, entity2);
        expect(bonReceptionService.compareBonReception).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareArticle', () => {
      it('Should forward to articleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(articleService, 'compareArticle');
        comp.compareArticle(entity, entity2);
        expect(articleService.compareArticle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
