export interface Oeuvre {
  id: number;
  titre: string;
  anneeCreation: number | null;
  technique: string | null;
  description: string | null;
  imageUrl: string | null;
  credit: string | null;
  artisteId: number;
  artisteNom: string;
}

export interface OeuvreInput {
  titre: string;
  idArtiste: number;
  anneeCreation: number | null;
  technique: string | null;
  description: string | null;
  imageUrl: string | null;
  credit: string | null;
}