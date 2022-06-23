import { IUser } from 'app/entities/user/user.model';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { IClasse } from 'app/entities/classe/classe.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IDirecteur {
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
  classes?: IClasse[] | null;
}

export class Directeur implements IDirecteur {
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
    public ressources?: IRessource[] | null,
    public classes?: IClasse[] | null
  ) {}
}

export function getDirecteurIdentifier(directeur: IDirecteur): number | undefined {
  return directeur.id;
}
