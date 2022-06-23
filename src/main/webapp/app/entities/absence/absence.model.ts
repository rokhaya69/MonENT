import { ISeance } from 'app/entities/seance/seance.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';

export interface IAbsence {
  id?: number;
  motif?: string;
  justifie?: boolean;
  seance?: ISeance | null;
  apprenant?: IApprenant | null;
  evaluation?: IEvaluation | null;
}

export class Absence implements IAbsence {
  constructor(
    public id?: number,
    public motif?: string,
    public justifie?: boolean,
    public seance?: ISeance | null,
    public apprenant?: IApprenant | null,
    public evaluation?: IEvaluation | null
  ) {
    this.justifie = this.justifie ?? false;
  }
}

export function getAbsenceIdentifier(absence: IAbsence): number | undefined {
  return absence.id;
}
