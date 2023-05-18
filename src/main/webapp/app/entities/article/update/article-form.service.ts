import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IArticle, NewArticle } from '../article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticle for edit and NewArticleFormGroupInput for create.
 */
type ArticleFormGroupInput = IArticle | PartialWithRequiredKeyOf<NewArticle>;

type ArticleFormDefaults = Pick<NewArticle, 'id'>;

type ArticleFormGroupContent = {
  id: FormControl<IArticle['id'] | NewArticle['id']>;
  code: FormControl<IArticle['code']>;
  nom: FormControl<IArticle['nom']>;
  description: FormControl<IArticle['description']>;
  prixAchat: FormControl<IArticle['prixAchat']>;
  qte: FormControl<IArticle['qte']>;
  qteAlert: FormControl<IArticle['qteAlert']>;
  status: FormControl<IArticle['status']>;
  dateCreation: FormControl<IArticle['dateCreation']>;
  category: FormControl<IArticle['category']>;
  fournisseur: FormControl<IArticle['fournisseur']>;
};

export type ArticleFormGroup = FormGroup<ArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleFormService {
  createArticleFormGroup(article: ArticleFormGroupInput = { id: null }): ArticleFormGroup {
    const articleRawValue = {
      ...this.getFormDefaults(),
      ...article,
    };
    return new FormGroup<ArticleFormGroupContent>({
      id: new FormControl(
        { value: articleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(articleRawValue.code),
      nom: new FormControl(articleRawValue.nom),
      description: new FormControl(articleRawValue.description),
      prixAchat: new FormControl(articleRawValue.prixAchat),
      qte: new FormControl(articleRawValue.qte),
      qteAlert: new FormControl(articleRawValue.qteAlert),
      status: new FormControl(articleRawValue.status),
      dateCreation: new FormControl(articleRawValue.dateCreation),
      category: new FormControl(articleRawValue.category),
      fournisseur: new FormControl(articleRawValue.fournisseur),
    });
  }

  getArticle(form: ArticleFormGroup): IArticle | NewArticle {
    return form.getRawValue() as IArticle | NewArticle;
  }

  resetForm(form: ArticleFormGroup, article: ArticleFormGroupInput): void {
    const articleRawValue = { ...this.getFormDefaults(), ...article };
    form.reset(
      {
        ...articleRawValue,
        id: { value: articleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ArticleFormDefaults {
    return {
      id: null,
    };
  }
}
