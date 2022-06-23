import { ICommune } from 'app/entities/commune/commune.model';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { TypeInspec } from 'app/entities/enumerations/type-inspec.model';

export interface IInspection {
  id?: number;
  nomInspec?: string;
  typeInspec?: TypeInspec;
  email?: string;
  telephone?: string;
  commune?: ICommune | null;
  inspecteur?: IInspecteur | null;
  etablissements?: IEtablissement[] | null;
}

export class Inspection implements IInspection {
  constructor(
    public id?: number,
    public nomInspec?: string,
    public typeInspec?: TypeInspec,
    public email?: string,
    public telephone?: string,
    public commune?: ICommune | null,
    public inspecteur?: IInspecteur | null,
    public etablissements?: IEtablissement[] | null
  ) {}
}

export function getInspectionIdentifier(inspection: IInspection): number | undefined {
  return inspection.id;
}
