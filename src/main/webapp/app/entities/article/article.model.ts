import dayjs from 'dayjs/esm';
import { ICategory } from 'app/entities/category/category.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IArticle {
  id: number;
  code?: string | null;
  nom?: string | null;
  description?: string | null;
  prixAchat?: number | null;
  qte?: number | null;
  qteAlert?: number | null;
  status?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  category?: ICategory | null;
  fournisseur?: IFournisseur | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
