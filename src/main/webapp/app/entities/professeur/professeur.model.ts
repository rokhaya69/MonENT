import { IUser } from 'app/entities/user/user.model';
import { ICours } from 'app/entities/cours/cours.model';
import { IClasse } from 'app/entities/classe/classe.model';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NiveauEnseignement } from 'app/entities/enumerations/niveau-enseignement.model';

export interface IProfesseur {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  adresse?: string;
  telephone?: string;
  sexe?: Sexe;
  photoContentType?: string | null;
  photo?: string | null;
  specialite?: string;
  niveauEnseign?: NiveauEnseignement;
  user?: IUser | null;
  cours?: ICours[] | null;
  classes?: IClasse[] | null;
  evaluations?: IEvaluation[] | null;
  etablissement?: IEtablissement | null;
}

export class Professeur implements IProfesseur {
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
    public specialite?: string,
    public niveauEnseign?: NiveauEnseignement,
    public user?: IUser | null,
    public cours?: ICours[] | null,
    public classes?: IClasse[] | null,
    public evaluations?: IEvaluation[] | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getProfesseurIdentifier(professeur: IProfesseur): number | undefined {
  return professeur.id;
}
