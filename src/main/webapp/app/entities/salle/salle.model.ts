import { ISeance } from 'app/entities/seance/seance.model';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';

export interface ISalle {
  id?: number;
  libelleSalle?: string;
  seances?: ISeance[] | null;
  evaluations?: IEvaluation[] | null;
}

export class Salle implements ISalle {
  constructor(
    public id?: number,
    public libelleSalle?: string,
    public seances?: ISeance[] | null,
    public evaluations?: IEvaluation[] | null
  ) {}
}

export function getSalleIdentifier(salle: ISalle): number | undefined {
  return salle.id;
}
