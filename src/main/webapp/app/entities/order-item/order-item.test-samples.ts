import { OrderStatus } from 'app/entities/enumerations/order-status.model';

import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: 62496,
};

export const sampleWithPartialData: IOrderItem = {
  id: 15882,
  quantity: 'synthesize Rubber',
  rate: 'Games redundant Synergized',
  status: OrderStatus['STATUS1'],
};

export const sampleWithFullData: IOrderItem = {
  id: 7567,
  quantity: 'Toys',
  rate: 'empower b c',
  prixVente: 91642,
  status: OrderStatus['STATUS1'],
};

export const sampleWithNewData: NewOrderItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
