import { IFournisseur, NewFournisseur } from './fournisseur.model';

export const sampleWithRequiredData: IFournisseur = {
  id: 12366,
};

export const sampleWithPartialData: IFournisseur = {
  id: 58731,
  code: 'synthesize',
  ville: 'Presbourg Enhanced JBOD',
  adresse: 'Account',
  activite: 'Cambridgeshire c compress',
};

export const sampleWithFullData: IFournisseur = {
  id: 75630,
  code: 'de Euro',
  ville: 'composite JSON',
  adresse: 'navigating',
  activite: 'Rustic overriding Alsace',
  nom: 'c',
  description: 'policy cyan',
};

export const sampleWithNewData: NewFournisseur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
