import dayjs from 'dayjs/esm';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { ISerie } from 'app/entities/serie/serie.model';
import { ISyllabus } from 'app/entities/syllabus/syllabus.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface IProgramme {
  id?: number;
  nomProgram?: string;
  contenuProgramContentType?: string | null;
  contenuProgram?: string | null;
  annee?: dayjs.Dayjs;
  filiere?: IFiliere | null;
  serie?: ISerie | null;
  syllabi?: ISyllabus[] | null;
  matieres?: IMatiere[] | null;
}

export class Programme implements IProgramme {
  constructor(
    public id?: number,
    public nomProgram?: string,
    public contenuProgramContentType?: string | null,
    public contenuProgram?: string | null,
    public annee?: dayjs.Dayjs,
    public filiere?: IFiliere | null,
    public serie?: ISerie | null,
    public syllabi?: ISyllabus[] | null,
    public matieres?: IMatiere[] | null
  ) {}
}

export function getProgrammeIdentifier(programme: IProgramme): number | undefined {
  return programme.id;
}
