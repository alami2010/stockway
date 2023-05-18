import dayjs from 'dayjs/esm';

import { PaymentType } from 'app/entities/enumerations/payment-type.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 47761,
};

export const sampleWithPartialData: IOrder = {
  id: 82425,
  clientName: 'b Rhône-Alpes',
  clientContact: 'indigo microchip Fantastic',
  subTotal: 32103,
  discount: 92419,
  grandTotal: 'Centre Unbranded 24/7',
  due: 'Haute-Normandie de overriding',
  paymentType: PaymentType['CACHE'],
  paymentStatus: PaymentStatus['FULL'],
};

export const sampleWithFullData: IOrder = {
  id: 56450,
  date: dayjs('2023-05-14'),
  clientName: 'copying Frozen Shoes',
  clientContact: 'Rustic Ball',
  subTotal: 13164,
  vat: 'copying',
  totalAmount: 53488,
  discount: 66475,
  grandTotal: 'a',
  paid: true,
  due: 'Music',
  paymentType: PaymentType['CACHE'],
  paymentStatus: PaymentStatus['NONE'],
  status: 'SSL SMTP',
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
