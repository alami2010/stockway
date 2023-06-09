import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArticleComponent } from '../list/article.component';
import { ArticleDetailComponent } from '../detail/article-detail.component';
import { ArticleUpdateComponent } from '../update/article-update.component';
import { ArticleRoutingResolveService } from './article-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { ArticleExportComponent } from '../export/article-export.component';
import { ArticleImportComponent } from '../import/article-import.component';
import { ArticleInventaireComponent } from '../inventaire/article-inventaire.component';

const articleRoute: Routes = [
  {
    path: '',
    component: ArticleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'import',
    component: ArticleImportComponent,

    canActivate: [UserRouteAccessService],
  },
  {
    path: 'inventaire',
    component: ArticleInventaireComponent,

    canActivate: [UserRouteAccessService],
  },
  {
    path: 'export',
    component: ArticleExportComponent,

    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArticleDetailComponent,
    resolve: {
      article: ArticleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArticleUpdateComponent,
    resolve: {
      article: ArticleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArticleUpdateComponent,
    resolve: {
      article: ArticleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(articleRoute)],
  exports: [RouterModule],
})
export class ArticleRoutingModule {}
