import { IBonReceptionItem, NewBonReceptionItem } from './bon-reception-item.model';

export const sampleWithRequiredData: IBonReceptionItem = {
  id: 4583,
};

export const sampleWithPartialData: IBonReceptionItem = {
  id: 92455,
};

export const sampleWithFullData: IBonReceptionItem = {
  id: 61631,
  qte: 2625,
};

export const sampleWithNewData: NewBonReceptionItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
