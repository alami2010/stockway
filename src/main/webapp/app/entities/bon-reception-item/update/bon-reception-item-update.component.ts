import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BonReceptionItemFormService, BonReceptionItemFormGroup } from './bon-reception-item-form.service';
import { IBonReceptionItem } from '../bon-reception-item.model';
import { BonReceptionItemService } from '../service/bon-reception-item.service';
import { IBonReception } from 'app/entities/bon-reception/bon-reception.model';
import { BonReceptionService } from 'app/entities/bon-reception/service/bon-reception.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';

@Component({
  selector: 'jhi-bon-reception-item-update',
  templateUrl: './bon-reception-item-update.component.html',
})
export class BonReceptionItemUpdateComponent implements OnInit {
  isSaving = false;
  bonReceptionItem: IBonReceptionItem | null = null;

  bonReceptionsSharedCollection: IBonReception[] = [];
  articlesSharedCollection: IArticle[] = [];

  editForm: BonReceptionItemFormGroup = this.bonReceptionItemFormService.createBonReceptionItemFormGroup();

  constructor(
    protected bonReceptionItemService: BonReceptionItemService,
    protected bonReceptionItemFormService: BonReceptionItemFormService,
    protected bonReceptionService: BonReceptionService,
    protected articleService: ArticleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBonReception = (o1: IBonReception | null, o2: IBonReception | null): boolean =>
    this.bonReceptionService.compareBonReception(o1, o2);

  compareArticle = (o1: IArticle | null, o2: IArticle | null): boolean => this.articleService.compareArticle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bonReceptionItem }) => {
      this.bonReceptionItem = bonReceptionItem;
      if (bonReceptionItem) {
        this.updateForm(bonReceptionItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bonReceptionItem = this.bonReceptionItemFormService.getBonReceptionItem(this.editForm);
    if (bonReceptionItem.id !== null) {
      this.subscribeToSaveResponse(this.bonReceptionItemService.update(bonReceptionItem));
    } else {
      this.subscribeToSaveResponse(this.bonReceptionItemService.create(bonReceptionItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBonReceptionItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bonReceptionItem: IBonReceptionItem): void {
    this.bonReceptionItem = bonReceptionItem;
    this.bonReceptionItemFormService.resetForm(this.editForm, bonReceptionItem);

    this.bonReceptionsSharedCollection = this.bonReceptionService.addBonReceptionToCollectionIfMissing<IBonReception>(
      this.bonReceptionsSharedCollection,
      bonReceptionItem.bon
    );
    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing<IArticle>(
      this.articlesSharedCollection,
      bonReceptionItem.article
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bonReceptionService
      .query()
      .pipe(map((res: HttpResponse<IBonReception[]>) => res.body ?? []))
      .pipe(
        map((bonReceptions: IBonReception[]) =>
          this.bonReceptionService.addBonReceptionToCollectionIfMissing<IBonReception>(bonReceptions, this.bonReceptionItem?.bon)
        )
      )
      .subscribe((bonReceptions: IBonReception[]) => (this.bonReceptionsSharedCollection = bonReceptions));

    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(
        map((articles: IArticle[]) =>
          this.articleService.addArticleToCollectionIfMissing<IArticle>(articles, this.bonReceptionItem?.article)
        )
      )
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));
  }
}
