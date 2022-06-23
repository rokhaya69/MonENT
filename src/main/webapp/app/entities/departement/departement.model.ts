import { ICommune } from 'app/entities/commune/commune.model';
import { IRegion } from 'app/entities/region/region.model';
import { NomDept } from 'app/entities/enumerations/nom-dept.model';

export interface IDepartement {
  id?: number;
  nomDept?: NomDept;
  autreDept?: string | null;
  communes?: ICommune[] | null;
  region?: IRegion | null;
}

export class Departement implements IDepartement {
  constructor(
    public id?: number,
    public nomDept?: NomDept,
    public autreDept?: string | null,
    public communes?: ICommune[] | null,
    public region?: IRegion | null
  ) {}
}

export function getDepartementIdentifier(departement: IDepartement): number | undefined {
  return departement.id;
}
