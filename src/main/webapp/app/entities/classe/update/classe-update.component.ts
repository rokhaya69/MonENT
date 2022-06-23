import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClasse, Classe } from '../classe.model';
import { ClasseService } from '../service/classe.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { ISerie } from 'app/entities/serie/serie.model';
import { SerieService } from 'app/entities/serie/service/serie.service';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { ProviseurService } from 'app/entities/proviseur/service/proviseur.service';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { DirecteurService } from 'app/entities/directeur/service/directeur.service';
import { ISurveillant } from 'app/entities/surveillant/surveillant.model';
import { SurveillantService } from 'app/entities/surveillant/service/surveillant.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

@Component({
  selector: 'jhi-classe-update',
  templateUrl: './classe-update.component.html',
})
export class ClasseUpdateComponent implements OnInit {
  isSaving = false;
  niveauEtudeValues = Object.keys(NiveauEtude);

  filieresSharedCollection: IFiliere[] = [];
  seriesSharedCollection: ISerie[] = [];
  proviseursSharedCollection: IProviseur[] = [];
  directeursSharedCollection: IDirecteur[] = [];
  surveillantsSharedCollection: ISurveillant[] = [];
  professeursSharedCollection: IProfesseur[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [null, [Validators.required]],
    niveauEtude: [null, [Validators.required]],
    autreNiveau: [],
    filiere: [],
    serie: [],
    proviseur: [],
    directeur: [],
    surveillant: [],
    professeur: [],
  });

  constructor(
    protected classeService: ClasseService,
    protected filiereService: FiliereService,
    protected serieService: SerieService,
    protected proviseurService: ProviseurService,
    protected directeurService: DirecteurService,
    protected surveillantService: SurveillantService,
    protected professeurService: ProfesseurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classe }) => {
      this.updateForm(classe);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classe = this.createFromForm();
    if (classe.id !== undefined) {
      this.subscribeToSaveResponse(this.classeService.update(classe));
    } else {
      this.subscribeToSaveResponse(this.classeService.create(classe));
    }
  }

  trackFiliereById(index: number, item: IFiliere): number {
    return item.id!;
  }

  trackSerieById(index: number, item: ISerie): number {
    return item.id!;
  }

  trackProviseurById(index: number, item: IProviseur): number {
    return item.id!;
  }

  trackDirecteurById(index: number, item: IDirecteur): number {
    return item.id!;
  }

  trackSurveillantById(index: number, item: ISurveillant): number {
    return item.id!;
  }

  trackProfesseurById(index: number, item: IProfesseur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClasse>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(classe: IClasse): void {
    this.editForm.patchValue({
      id: classe.id,
      titre: classe.titre,
      niveauEtude: classe.niveauEtude,
      autreNiveau: classe.autreNiveau,
      filiere: classe.filiere,
      serie: classe.serie,
      proviseur: classe.proviseur,
      directeur: classe.directeur,
      surveillant: classe.surveillant,
      professeur: classe.professeur,
    });

    this.filieresSharedCollection = this.filiereService.addFiliereToCollectionIfMissing(this.filieresSharedCollection, classe.filiere);
    this.seriesSharedCollection = this.serieService.addSerieToCollectionIfMissing(this.seriesSharedCollection, classe.serie);
    this.proviseursSharedCollection = this.proviseurService.addProviseurToCollectionIfMissing(
      this.proviseursSharedCollection,
      classe.proviseur
    );
    this.directeursSharedCollection = this.directeurService.addDirecteurToCollectionIfMissing(
      this.directeursSharedCollection,
      classe.directeur
    );
    this.surveillantsSharedCollection = this.surveillantService.addSurveillantToCollectionIfMissing(
      this.surveillantsSharedCollection,
      classe.surveillant
    );
    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      classe.professeur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.filiereService
      .query()
      .pipe(map((res: HttpResponse<IFiliere[]>) => res.body ?? []))
      .pipe(
        map((filieres: IFiliere[]) => this.filiereService.addFiliereToCollectionIfMissing(filieres, this.editForm.get('filiere')!.value))
      )
      .subscribe((filieres: IFiliere[]) => (this.filieresSharedCollection = filieres));

    this.serieService
      .query()
      .pipe(map((res: HttpResponse<ISerie[]>) => res.body ?? []))
      .pipe(map((series: ISerie[]) => this.serieService.addSerieToCollectionIfMissing(series, this.editForm.get('serie')!.value)))
      .subscribe((series: ISerie[]) => (this.seriesSharedCollection = series));

    this.proviseurService
      .query()
      .pipe(map((res: HttpResponse<IProviseur[]>) => res.body ?? []))
      .pipe(
        map((proviseurs: IProviseur[]) =>
          this.proviseurService.addProviseurToCollectionIfMissing(proviseurs, this.editForm.get('proviseur')!.value)
        )
      )
      .subscribe((proviseurs: IProviseur[]) => (this.proviseursSharedCollection = proviseurs));

    this.directeurService
      .query()
      .pipe(map((res: HttpResponse<IDirecteur[]>) => res.body ?? []))
      .pipe(
        map((directeurs: IDirecteur[]) =>
          this.directeurService.addDirecteurToCollectionIfMissing(directeurs, this.editForm.get('directeur')!.value)
        )
      )
      .subscribe((directeurs: IDirecteur[]) => (this.directeursSharedCollection = directeurs));

    this.surveillantService
      .query()
      .pipe(map((res: HttpResponse<ISurveillant[]>) => res.body ?? []))
      .pipe(
        map((surveillants: ISurveillant[]) =>
          this.surveillantService.addSurveillantToCollectionIfMissing(surveillants, this.editForm.get('surveillant')!.value)
        )
      )
      .subscribe((surveillants: ISurveillant[]) => (this.surveillantsSharedCollection = surveillants));

    this.professeurService
      .query()
      .pipe(map((res: HttpResponse<IProfesseur[]>) => res.body ?? []))
      .pipe(
        map((professeurs: IProfesseur[]) =>
          this.professeurService.addProfesseurToCollectionIfMissing(professeurs, this.editForm.get('professeur')!.value)
        )
      )
      .subscribe((professeurs: IProfesseur[]) => (this.professeursSharedCollection = professeurs));
  }

  protected createFromForm(): IClasse {
    return {
      ...new Classe(),
      id: this.editForm.get(['id'])!.value,
      titre: this.editForm.get(['titre'])!.value,
      niveauEtude: this.editForm.get(['niveauEtude'])!.value,
      autreNiveau: this.editForm.get(['autreNiveau'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      serie: this.editForm.get(['serie'])!.value,
      proviseur: this.editForm.get(['proviseur'])!.value,
      directeur: this.editForm.get(['directeur'])!.value,
      surveillant: this.editForm.get(['surveillant'])!.value,
      professeur: this.editForm.get(['professeur'])!.value,
    };
  }
}
