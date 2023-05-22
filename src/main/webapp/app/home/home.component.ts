import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ArticleService } from '../entities/article/service/article.service';
import { IDashboard } from './dashboard.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  statistic: IDashboard = { categories: [], articleOutOfStock: [] };

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private articleService: ArticleService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.proccess(account));
  }

  private proccess(account: Account | null) {
    this.articleService.findStatistic().subscribe(statistic => this.process(statistic));

    return (this.account = account);
  }

  private process(statistic: IDashboard) {
    console.info(statistic);
    return (this.statistic = statistic);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
