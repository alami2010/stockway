import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IOrder {
  id: number;
  date?: dayjs.Dayjs | null;
  clientName?: string | null;
  clientContact?: string | null;
  subTotal?: number | null;
  vat?: string | null;
  totalAmount?: number | null;
  discount?: number | null;
  grandTotal?: string | null;
  paid?: boolean | null;
  due?: string | null;
  paymentType?: PaymentType | null;
  paymentStatus?: PaymentStatus | null;
  status?: string | null;
  user?: Pick<IUtilisateur, 'id'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
