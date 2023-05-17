import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { saveAs } from 'file-saver';
import { IArticle } from '../article.model';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';
import { ArticleService } from '../service/article.service';

@Component({
  selector: 'jhi-article-detail',
  templateUrl: './article-import.component.html',
})
export class ArticleImportComponent implements OnInit {
  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;

  uploadFile: File | undefined;
  lineRejected: string[] = [];

  constructor(protected activatedRoute: ActivatedRoute, protected articleService: ArticleService) {}

  ngOnInit(): void {}

  previousState(): void {
    window.history.back();
  }

  downloadListCategories() {
    this.downloadFile('CATEGORY_LIST', '.csv');
  }

  downloadColisUploadExcel() {
    this.downloadFile('ARTICLE_UPLOAD_EXCEL', '.xlsx');
  }

  private downloadFile(file: string, extension: string) {
    //this.loader.open('chargement');
    this.articleService.downloadFile(file).subscribe(
      blob => {
        saveAs(blob.body, file + extension);
      },
      error => {
        //this.notifyService.showError('Erreur lors de le téléchargement du fichier');
        //this.loader.close();
        console.info('error');
      },
      () => {
        //this.loader.close();
      }
    );
  }

  downloadListVille() {}

  selectFile1($event: Event) {}

  upload() {}
}
