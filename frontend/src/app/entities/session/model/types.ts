export type Role = 'ADMIN' | 'COLLABORATEUR';

export interface User {
  id: number;
  prenom: string;
  nom: string;
  email: string;
  roles: Role[];
}

export interface AuthResponse {
  accessToken: string;
  expiresIn: number;
  user: User;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  prenom: string;
  nom: string;
  email: string;
  tel: string;
  password: string;
}