import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';

export interface INote {
  id?: number;
  uneNote?: number;
  apprenant?: IApprenant | null;
  evaluation?: IEvaluation | null;
}

export class Note implements INote {
  constructor(public id?: number, public uneNote?: number, public apprenant?: IApprenant | null, public evaluation?: IEvaluation | null) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
