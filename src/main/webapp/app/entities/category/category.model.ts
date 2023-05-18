export interface ICategory {
  id: number;
  code?: string | null;
  libelle?: string | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
