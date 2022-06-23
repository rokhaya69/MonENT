import dayjs from 'dayjs/esm';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { ICours } from 'app/entities/cours/cours.model';
import { ISurveillant } from 'app/entities/surveillant/surveillant.model';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { TypeRessource } from 'app/entities/enumerations/type-ressource.model';

export interface IRessource {
  id?: number;
  libelRessource?: string;
  typeRessource?: TypeRessource;
  lienRessourceContentType?: string;
  lienRessource?: string;
  dateMise?: dayjs.Dayjs | null;
  apprenant?: IApprenant | null;
  groupe?: IGroupe | null;
  cours?: ICours | null;
  persoAdmin?: ISurveillant | null;
  proviseur?: IProviseur | null;
  directeur?: IDirecteur | null;
  inspecteur?: IInspecteur | null;
  etablissements?: IEtablissement[] | null;
}

export class Ressource implements IRessource {
  constructor(
    public id?: number,
    public libelRessource?: string,
    public typeRessource?: TypeRessource,
    public lienRessourceContentType?: string,
    public lienRessource?: string,
    public dateMise?: dayjs.Dayjs | null,
    public apprenant?: IApprenant | null,
    public groupe?: IGroupe | null,
    public cours?: ICours | null,
    public persoAdmin?: ISurveillant | null,
    public proviseur?: IProviseur | null,
    public directeur?: IDirecteur | null,
    public inspecteur?: IInspecteur | null,
    public etablissements?: IEtablissement[] | null
  ) {}
}

export function getRessourceIdentifier(ressource: IRessource): number | undefined {
  return ressource.id;
}
