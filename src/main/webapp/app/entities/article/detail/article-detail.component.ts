import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IArticle} from '../article.model';
import {NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels} from "@techiediaries/ngx-qrcode";

@Component({
  selector: 'jhi-article-detail',
  templateUrl: './article-detail.component.html',
})
export class ArticleDetailComponent implements OnInit {

  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;
  article: IArticle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({article}) => {
      this.article = article;
    });
  }

  previousState(): void {
    window.history.back();
  }


  public downloadQRCode() {
    const qrImageElement = document.getElementsByTagName("img")[0]; //as for me there is only one img tag in my view.
    const imageData = qrImageElement.src; //base64 data is inside the image element.
    const downloadLink = document.createElement("a");
    downloadLink.href = imageData;
    downloadLink.download = "etiquette_" + (this.article?.id ?? '');
    downloadLink.click();
  }
}
