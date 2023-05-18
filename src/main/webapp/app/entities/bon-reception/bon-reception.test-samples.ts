import dayjs from 'dayjs/esm';

import { IBonReception, NewBonReception } from './bon-reception.model';

export const sampleWithRequiredData: IBonReception = {
  id: 4971,
};

export const sampleWithPartialData: IBonReception = {
  id: 72663,
};

export const sampleWithFullData: IBonReception = {
  id: 77826,
  informaton: 'executive dynamic end-to-end',
  numFacture: 74308,
  numBl: 23769,
  dateCreation: dayjs('2023-05-14'),
};

export const sampleWithNewData: NewBonReception = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
