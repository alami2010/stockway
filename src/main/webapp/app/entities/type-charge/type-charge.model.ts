export interface ITypeCharge {
  id: number;
  nom?: string | null;
}

export type NewTypeCharge = Omit<ITypeCharge, 'id'> & { id: null };
