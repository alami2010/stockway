import dayjs from 'dayjs/esm';

import { UserType } from 'app/entities/enumerations/user-type.model';

import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 52583,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 71845,
  code: 'Uruguayo Indonésie',
  nom: 'Money',
  status: "d'Argenteuil Champagne-Ardenne Afghanistan",
  email: 'Amour61@gmail.com',
  type: UserType['SUPPLIER'],
};

export const sampleWithFullData: IUtilisateur = {
  id: 99014,
  code: 'Personal parse Account',
  nom: 'Viêt Licensed withdrawal',
  prenom: 'hacking Small Manager',
  dateCreation: dayjs('2023-05-14'),
  status: 'Practical Andorre',
  phone: '+33 700865100',
  email: 'Narcisse.Philippe@yahoo.fr',
  information: 'software',
  type: UserType['CLIENT'],
};

export const sampleWithNewData: NewUtilisateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
