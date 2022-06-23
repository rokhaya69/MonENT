import { IClasse } from 'app/entities/classe/classe.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Qualification } from 'app/entities/enumerations/qualification.model';

export interface IFiliere {
  id?: number;
  nomFiliere?: NomFiliere;
  niveauQualif?: Qualification;
  autreFiliere?: string | null;
  autreQualif?: string | null;
  classes?: IClasse[] | null;
  etablissement?: IEtablissement | null;
}

export class Filiere implements IFiliere {
  constructor(
    public id?: number,
    public nomFiliere?: NomFiliere,
    public niveauQualif?: Qualification,
    public autreFiliere?: string | null,
    public autreQualif?: string | null,
    public classes?: IClasse[] | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getFiliereIdentifier(filiere: IFiliere): number | undefined {
  return filiere.id;
}
