import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IRole {
  id: number;
  name?: string | null;
  users?: Pick<IUtilisateur, 'id'>[] | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
