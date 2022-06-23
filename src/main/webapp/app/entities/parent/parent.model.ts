import { IUser } from 'app/entities/user/user.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IParent {
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
  apprenants?: IApprenant[] | null;
}

export class Parent implements IParent {
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
    public user?: IUser | null,
    public apprenants?: IApprenant[] | null
  ) {}
}

export function getParentIdentifier(parent: IParent): number | undefined {
  return parent.id;
}
