export interface IFournisseur {
  id: number;
  code?: string | null;
  ville?: string | null;
  adresse?: string | null;
  activite?: string | null;
  nom?: string | null;
  description?: string | null;
}

export type NewFournisseur = Omit<IFournisseur, 'id'> & { id: null };
