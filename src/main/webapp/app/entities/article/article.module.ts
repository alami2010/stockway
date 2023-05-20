import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArticleComponent } from './list/article.component';
import { ArticleDetailComponent } from './detail/article-detail.component';
import { ArticleUpdateComponent } from './update/article-update.component';
import { ArticleDeleteDialogComponent } from './delete/article-delete-dialog.component';
import { ArticleRoutingModule } from './route/article-routing.module';
import { NgxQRCodeModule } from '@techiediaries/ngx-qrcode';
import { ArticleExportComponent } from './export/article-export.component';
import { ArticleImportComponent } from './import/article-import.component';
import { ArticleInventaireComponent } from './inventaire/article-inventaire.component';
import { NgxLoadingModule } from 'ngx-loading';
@NgModule({
  imports: [SharedModule, ArticleRoutingModule, NgxQRCodeModule, NgxLoadingModule],
  declarations: [
    ArticleComponent,
    ArticleDetailComponent,
    ArticleUpdateComponent,
    ArticleDeleteDialogComponent,
    ArticleImportComponent,
    ArticleExportComponent,
    ArticleInventaireComponent,
  ],
})
export class ArticleModule {}
