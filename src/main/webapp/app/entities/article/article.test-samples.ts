import dayjs from 'dayjs/esm';

import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 61675,
};

export const sampleWithPartialData: IArticle = {
  id: 30399,
  code: 'Pants',
  nom: 'Gorgeous',
  description: 'c',
  qteAlert: 50781,
  status: 'c infomediaries',
};

export const sampleWithFullData: IArticle = {
  id: 92974,
  code: 'channels Sports Qatar',
  nom: 'SAS Ball bricks-and-clicks',
  description: 'copy Rubber de',
  prixAchat: 10925,
  qte: 50569,
  qteAlert: 94997,
  status: 'Handmade access',
  dateCreation: dayjs('2023-05-13'),
};

export const sampleWithNewData: NewArticle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
