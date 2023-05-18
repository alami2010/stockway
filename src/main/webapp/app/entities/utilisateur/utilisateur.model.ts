import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IRole } from 'app/entities/role/role.model';
import { UserType } from 'app/entities/enumerations/user-type.model';

export interface IUtilisateur {
  id: number;
  code?: string | null;
  nom?: string | null;
  prenom?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  status?: string | null;
  phone?: string | null;
  email?: string | null;
  information?: string | null;
  type?: UserType | null;
  user?: Pick<IUser, 'id'> | null;
  roles?: Pick<IRole, 'id'>[] | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
