import { ITypeCharge, NewTypeCharge } from './type-charge.model';

export const sampleWithRequiredData: ITypeCharge = {
  id: 70001,
};

export const sampleWithPartialData: ITypeCharge = {
  id: 42849,
};

export const sampleWithFullData: ITypeCharge = {
  id: 70715,
  nom: 'compress',
};

export const sampleWithNewData: NewTypeCharge = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
