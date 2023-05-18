import { IBonReception } from 'app/entities/bon-reception/bon-reception.model';
import { IArticle } from 'app/entities/article/article.model';

export interface IBonReceptionItem {
  id: number;
  qte?: number | null;
  bon?: Pick<IBonReception, 'id'> | null;
  article?: Pick<IArticle, 'id'> | null;
}

export type NewBonReceptionItem = Omit<IBonReceptionItem, 'id'> & { id: null };
