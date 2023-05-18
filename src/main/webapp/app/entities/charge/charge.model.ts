import dayjs from 'dayjs/esm';
import { ITypeCharge } from 'app/entities/type-charge/type-charge.model';

export interface ICharge {
  id: number;
  nom?: string | null;
  valeur?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  type?: Pick<ITypeCharge, 'id'> | null;
}

export type NewCharge = Omit<ICharge, 'id'> & { id: null };
