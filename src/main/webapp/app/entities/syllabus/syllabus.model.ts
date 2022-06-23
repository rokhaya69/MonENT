import { ICours } from 'app/entities/cours/cours.model';
import { IProgramme } from 'app/entities/programme/programme.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface ISyllabus {
  id?: number;
  nomSyllabus?: string;
  syllabusContentType?: string | null;
  syllabus?: string | null;
  cours?: ICours[] | null;
  programme?: IProgramme | null;
  matiere?: IMatiere | null;
}

export class Syllabus implements ISyllabus {
  constructor(
    public id?: number,
    public nomSyllabus?: string,
    public syllabusContentType?: string | null,
    public syllabus?: string | null,
    public cours?: ICours[] | null,
    public programme?: IProgramme | null,
    public matiere?: IMatiere | null
  ) {}
}

export function getSyllabusIdentifier(syllabus: ISyllabus): number | undefined {
  return syllabus.id;
}
