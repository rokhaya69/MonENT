import { ICours } from 'app/entities/cours/cours.model';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { ISerie } from 'app/entities/serie/serie.model';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { ISurveillant } from 'app/entities/surveillant/surveillant.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

export interface IClasse {
  id?: number;
  titre?: string;
  niveauEtude?: NiveauEtude;
  autreNiveau?: string | null;
  cours?: ICours[] | null;
  groupes?: IGroupe[] | null;
  filiere?: IFiliere | null;
  serie?: ISerie | null;
  proviseur?: IProviseur | null;
  directeur?: IDirecteur | null;
  surveillant?: ISurveillant | null;
  professeur?: IProfesseur | null;
}

export class Classe implements IClasse {
  constructor(
    public id?: number,
    public titre?: string,
    public niveauEtude?: NiveauEtude,
    public autreNiveau?: string | null,
    public cours?: ICours[] | null,
    public groupes?: IGroupe[] | null,
    public filiere?: IFiliere | null,
    public serie?: ISerie | null,
    public proviseur?: IProviseur | null,
    public directeur?: IDirecteur | null,
    public surveillant?: ISurveillant | null,
    public professeur?: IProfesseur | null
  ) {}
}

export function getClasseIdentifier(classe: IClasse): number | undefined {
  return classe.id;
}
