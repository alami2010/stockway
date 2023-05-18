import { IArticle } from 'app/entities/article/article.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IOrderItem {
  id: number;
  quantity?: string | null;
  rate?: string | null;
  prixVente?: number | null;
  status?: OrderStatus | null;
  article?: Pick<IArticle, 'id'> | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };
