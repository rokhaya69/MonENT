import { IDepartement } from 'app/entities/departement/departement.model';

export interface ICommune {
  id?: number;
  nomCom?: string;
  departement?: IDepartement | null;
}

export class Commune implements ICommune {
  constructor(public id?: number, public nomCom?: string, public departement?: IDepartement | null) {}
}

export function getCommuneIdentifier(commune: ICommune): number | undefined {
  return commune.id;
}
