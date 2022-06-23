import { IUser } from 'app/entities/user/user.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IMefpai {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  adresse?: string;
  telephone?: string;
  sexe?: Sexe;
  photoContentType?: string | null;
  photo?: string | null;
  user?: IUser | null;
}

export class Mefpai implements IMefpai {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public email?: string,
    public adresse?: string,
    public telephone?: string,
    public sexe?: Sexe,
    public photoContentType?: string | null,
    public photo?: string | null,
    public user?: IUser | null
  ) {}
}

export function getMefpaiIdentifier(mefpai: IMefpai): number | undefined {
  return mefpai.id;
}
