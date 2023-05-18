import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IPaiement {
  id: number;
  remunuration?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  user?: Pick<IUtilisateur, 'id'> | null;
}

export type NewPaiement = Omit<IPaiement, 'id'> & { id: null };
