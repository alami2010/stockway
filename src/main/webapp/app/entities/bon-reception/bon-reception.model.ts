import dayjs from 'dayjs/esm';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IBonReception {
  id: number;
  informaton?: string | null;
  numFacture?: number | null;
  numBl?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  fournisseur?: Pick<IFournisseur, 'id'> | null;
}

export type NewBonReception = Omit<IBonReception, 'id'> & { id: null };
