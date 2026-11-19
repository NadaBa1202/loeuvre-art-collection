export interface TechniqueCount { technique: string; total: number; }
export interface RecentOeuvre {
  id: number; titre: string; artisteNom: string;
  technique: string | null; anneeCreation: number | null; imageUrl: string | null;
}
export interface Dashboard {
  nbArtistes: number;
  nbOeuvres: number;
  nbUtilisateurs: number | null;
  parTechnique: TechniqueCount[];
  recentes: RecentOeuvre[];
}