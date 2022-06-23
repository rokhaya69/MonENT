export interface IContenu {
  id?: number;
  nomContenu?: string | null;
  contenuContentType?: string | null;
  contenu?: string | null;
}

export class Contenu implements IContenu {
  constructor(
    public id?: number,
    public nomContenu?: string | null,
    public contenuContentType?: string | null,
    public contenu?: string | null
  ) {}
}

export function getContenuIdentifier(contenu: IContenu): number | undefined {
  return contenu.id;
}
