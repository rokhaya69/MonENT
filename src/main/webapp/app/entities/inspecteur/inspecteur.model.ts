import { IUser } from 'app/entities/user/user.model';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IInspecteur {
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
  ressources?: IRessource[] | null;
}

export class Inspecteur implements IInspecteur {
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
    public ressources?: IRessource[] | null
  ) {}
}

export function getInspecteurIdentifier(inspecteur: IInspecteur): number | undefined {
  return inspecteur.id;
}
