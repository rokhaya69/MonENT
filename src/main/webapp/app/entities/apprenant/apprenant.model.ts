import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IAbsence } from 'app/entities/absence/absence.model';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { IParent } from 'app/entities/parent/parent.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IApprenant {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  adresse?: string;
  telephone?: string;
  sexe?: Sexe;
  photoContentType?: string | null;
  photo?: string | null;
  dateNaissance?: dayjs.Dayjs;
  lieuNaissance?: string;
  user?: IUser | null;
  absences?: IAbsence[] | null;
  ressources?: IRessource[] | null;
  groupe?: IGroupe | null;
  parent?: IParent | null;
}

export class Apprenant implements IApprenant {
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
    public dateNaissance?: dayjs.Dayjs,
    public lieuNaissance?: string,
    public user?: IUser | null,
    public absences?: IAbsence[] | null,
    public ressources?: IRessource[] | null,
    public groupe?: IGroupe | null,
    public parent?: IParent | null
  ) {}
}

export function getApprenantIdentifier(apprenant: IApprenant): number | undefined {
  return apprenant.id;
}
