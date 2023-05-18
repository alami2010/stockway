import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  id: 56879,
};

export const sampleWithPartialData: IRole = {
  id: 46244,
  name: 'b technologies',
};

export const sampleWithFullData: IRole = {
  id: 49410,
  name: "indexing Directeur d'Assas",
};

export const sampleWithNewData: NewRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
