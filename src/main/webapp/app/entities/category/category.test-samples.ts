import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 2529,
};

export const sampleWithPartialData: ICategory = {
  id: 98520,
  code: 'Sausages',
  libelle: 'Clothing navigate gold',
};

export const sampleWithFullData: ICategory = {
  id: 97365,
  code: 'transparent task-force Garden',
  libelle: 'Borders bus',
};

export const sampleWithNewData: NewCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
