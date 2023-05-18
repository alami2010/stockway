import dayjs from 'dayjs/esm';

import { IPaiement, NewPaiement } from './paiement.model';

export const sampleWithRequiredData: IPaiement = {
  id: 69996,
};

export const sampleWithPartialData: IPaiement = {
  id: 34485,
  remunuration: 80929,
  dateCreation: dayjs('2023-05-13'),
};

export const sampleWithFullData: IPaiement = {
  id: 97812,
  remunuration: 4235,
  dateCreation: dayjs('2023-05-13'),
};

export const sampleWithNewData: NewPaiement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
