import { ISeance } from 'app/entities/seance/seance.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';
import { IClasse } from 'app/entities/classe/classe.model';

export interface IGroupe {
  id?: number;
  nomGroupe?: string;
  seances?: ISeance[] | null;
  apprenants?: IApprenant[] | null;
  ressources?: IRessource[] | null;
  evaluations?: IEvaluation[] | null;
  classe?: IClasse | null;
}

export class Groupe implements IGroupe {
  constructor(
    public id?: number,
    public nomGroupe?: string,
    public seances?: ISeance[] | null,
    public apprenants?: IApprenant[] | null,
    public ressources?: IRessource[] | null,
    public evaluations?: IEvaluation[] | null,
    public classe?: IClasse | null
  ) {}
}

export function getGroupeIdentifier(groupe: IGroupe): number | undefined {
  return groupe.id;
}
