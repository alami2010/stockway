import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';
import { CategoryService } from '../../category/service/category.service';
import { ICategory } from '../../category/category.model';
import { ArticleService } from '../service/article.service';
import { saveAs } from 'file-saver';

@Component({
  selector: 'jhi-article-detail',
  templateUrl: './article-export.component.html',
})
export class ArticleExportComponent implements OnInit {
  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;
  categories: ICategory[] | null | undefined;
  currentStatusFilter = '0';

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected articleService: ArticleService,
    private router: Router,
    protected categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.categoryService.all().subscribe(value1 => {
      this.categories = value1.body;
    });
  }

  previousState(): void {
    window.history.back();
  }

  export() {
    //this.loader.open('chargement');
    this.articleService.downloadHistroy(this.currentStatusFilter).subscribe(
      blob => {
        saveAs(blob.body, 'history_articles_' + this.currentStatusFilter + '.csv');
        // this.loader.close();
      },
      error => {
        // this.notifyService.showError('Aucun colis a generer Erreur lors de la géneration du bon de ramassage');
        // this.loader.close();
      }
    );
  }
}
