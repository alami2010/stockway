import { IArticle } from '../entities/article/article.model';

export interface StatisticsCount {
  value: string;
  count: number;
}

export interface IDashboard {
  articleOutOfStock: IArticle[];

  categories: StatisticsCount[];
}
