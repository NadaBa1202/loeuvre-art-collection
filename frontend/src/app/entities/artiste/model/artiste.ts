export interface Artiste {
  id: number;
  nom: string;
  nationalite: string | null;
  anneeNaissance: number | null;
  anneeDeces: number | null;
  mouvement: string | null;
  biographie: string | null;
  photoUrl: string | null;
  nbOeuvres: number;
}

export type ArtisteInput = Omit<Artiste, 'id' | 'nbOeuvres'>;