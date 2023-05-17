import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArticle } from '../article.model';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';

@Component({
  selector: 'jhi-article-detail',
  templateUrl: './article-export.component.html',
})
export class ArticleExportComponent implements OnInit {
  elementType = NgxQrcodeElementTypes.URL;
  correctionLevel = NgxQrcodeErrorCorrectionLevels.HIGH;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {}

  previousState(): void {
    window.history.back();
  }
}
