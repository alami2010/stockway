import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { saveAs } from 'file-saver';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';
import { ArticleService } from '../service/article.service';

@Component({
  selector: 'jhi-article-detail',
  templateUrl: './article-iventaire.component.html',
})
export class ArticleInventaireComponent implements OnInit {
  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;

  uploadFile: File | undefined;
  lineRejected: string[] = [];

  constructor(protected activatedRoute: ActivatedRoute, protected articleService: ArticleService, private router: Router) {}

  ngOnInit(): void {}

  previousState(): void {
    window.history.back();
  }

  downloadColisUploadExcel() {
    this.downloadFile('INVETAIRE_UPLOAD', '.csv');
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

  selectFile1(event: any): void {
    this.uploadFile = event.target.files.item(0);
  }

  uploadCsv() {
    const formData = new FormData();
    if (this.uploadFile) formData.append('uploadFile', this.uploadFile);
    this.upload(formData, true);
  }
  upload(formData: FormData, isCsv: boolean) {
    this.loading = true;

    this.articleService.inventaire(formData, isCsv).subscribe(
      value => {
        this.loading = false;
        this.lineRejected = value;
        if (value.length > 0) {
          //this.notifyService.showWarning('Certain colis sont pas chargé', 'Message');
        } else {
          //this.notifyService.showSuccess('Fichier bien charger', 'Message');
          this.router.navigate(['/article']);
        }
      },
      () => {
        // this.notifyService.showError('Erreur lors de chargement de fichier', 'Message');
        this.loading = false;
      },
      () => (this.loading = false)
    );
  }

  public loading = false;

  showAlert() {
    this.loading = true;
  }
}
