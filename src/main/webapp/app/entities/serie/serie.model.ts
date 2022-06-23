import { IClasse } from 'app/entities/classe/classe.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';

export interface ISerie {
  id?: number;
  nomSerie?: NomSerie;
  autreSerie?: string | null;
  classes?: IClasse[] | null;
  etablissement?: IEtablissement | null;
}

export class Serie implements ISerie {
  constructor(
    public id?: number,
    public nomSerie?: NomSerie,
    public autreSerie?: string | null,
    public classes?: IClasse[] | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getSerieIdentifier(serie: ISerie): number | undefined {
  return serie.id;
}
