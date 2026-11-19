import { User } from '@entities/session';

export interface ManagedUser extends User {
  tel?: string | null;
}