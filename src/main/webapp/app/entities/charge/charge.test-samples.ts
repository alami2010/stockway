import dayjs from 'dayjs/esm';

import { ICharge, NewCharge } from './charge.model';

export const sampleWithRequiredData: ICharge = {
  id: 39214,
};

export const sampleWithPartialData: ICharge = {
  id: 76728,
  valeur: 3845,
};

export const sampleWithFullData: ICharge = {
  id: 22355,
  nom: 'SMTP gold Pays',
  valeur: 76517,
  dateCreation: dayjs('2023-05-13'),
};

export const sampleWithNewData: NewCharge = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
