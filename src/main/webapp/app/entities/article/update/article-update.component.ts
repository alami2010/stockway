import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ArticleFormService, ArticleFormGroup } from './article-form.service';
import { IArticle } from '../article.model';
import { ArticleService } from '../service/article.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  article: IArticle | null = null;

  categoriesSharedCollection: ICategory[] = [];
  fournisseursSharedCollection: IFournisseur[] = [];

  editForm: ArticleFormGroup = this.articleFormService.createArticleFormGroup();

  constructor(
    protected articleService: ArticleService,
    protected articleFormService: ArticleFormService,
    protected categoryService: CategoryService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.article = article;
      if (article) {
        this.updateForm(article);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.articleFormService.getArticle(this.editForm);
    if (article.id !== null) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
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

  protected updateForm(article: IArticle): void {
    this.article = article;
    this.articleFormService.resetForm(this.editForm, article);

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      article.category
    );
    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(
      this.fournisseursSharedCollection,
      article.fournisseur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.article?.category)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.article?.fournisseur)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));
  }
}
