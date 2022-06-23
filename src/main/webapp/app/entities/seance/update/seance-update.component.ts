import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISeance, Seance } from '../seance.model';
import { SeanceService } from '../service/seance.service';
import { IContenu } from 'app/entities/contenu/contenu.model';
import { ContenuService } from 'app/entities/contenu/service/contenu.service';
import { ICours } from 'app/entities/cours/cours.model';
import { CoursService } from 'app/entities/cours/service/cours.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';
import { Jour } from 'app/entities/enumerations/jour.model';

@Component({
  selector: 'jhi-seance-update',
  templateUrl: './seance-update.component.html',
})
export class SeanceUpdateComponent implements OnInit {
  isSaving = false;
  jourValues = Object.keys(Jour);

  contenusCollection: IContenu[] = [];
  coursSharedCollection: ICours[] = [];
  sallesSharedCollection: ISalle[] = [];
  groupesSharedCollection: IGroupe[] = [];

  editForm = this.fb.group({
    id: [],
    jourSeance: [null, [Validators.required]],
    dateSeance: [null, [Validators.required]],
    dateDebut: [null, [Validators.required]],
    dateFin: [null, [Validators.required]],
    contenu: [],
    cours: [],
    salle: [],
    groupe: [],
  });

  constructor(
    protected seanceService: SeanceService,
    protected contenuService: ContenuService,
    protected coursService: CoursService,
    protected salleService: SalleService,
    protected groupeService: GroupeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seance }) => {
      if (seance.id === undefined) {
        const today = dayjs().startOf('day');
        seance.dateDebut = today;
        seance.dateFin = today;
      }

      this.updateForm(seance);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seance = this.createFromForm();
    if (seance.id !== undefined) {
      this.subscribeToSaveResponse(this.seanceService.update(seance));
    } else {
      this.subscribeToSaveResponse(this.seanceService.create(seance));
    }
  }

  trackContenuById(index: number, item: IContenu): number {
    return item.id!;
  }

  trackCoursById(index: number, item: ICours): number {
    return item.id!;
  }

  trackSalleById(index: number, item: ISalle): number {
    return item.id!;
  }

  trackGroupeById(index: number, item: IGroupe): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeance>>): void {
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

  protected updateForm(seance: ISeance): void {
    this.editForm.patchValue({
      id: seance.id,
      jourSeance: seance.jourSeance,
      dateSeance: seance.dateSeance,
      dateDebut: seance.dateDebut ? seance.dateDebut.format(DATE_TIME_FORMAT) : null,
      dateFin: seance.dateFin ? seance.dateFin.format(DATE_TIME_FORMAT) : null,
      contenu: seance.contenu,
      cours: seance.cours,
      salle: seance.salle,
      groupe: seance.groupe,
    });

    this.contenusCollection = this.contenuService.addContenuToCollectionIfMissing(this.contenusCollection, seance.contenu);
    this.coursSharedCollection = this.coursService.addCoursToCollectionIfMissing(this.coursSharedCollection, seance.cours);
    this.sallesSharedCollection = this.salleService.addSalleToCollectionIfMissing(this.sallesSharedCollection, seance.salle);
    this.groupesSharedCollection = this.groupeService.addGroupeToCollectionIfMissing(this.groupesSharedCollection, seance.groupe);
  }

  protected loadRelationshipsOptions(): void {
    this.contenuService
      .query({ filter: 'seance-is-null' })
      .pipe(map((res: HttpResponse<IContenu[]>) => res.body ?? []))
      .pipe(
        map((contenus: IContenu[]) => this.contenuService.addContenuToCollectionIfMissing(contenus, this.editForm.get('contenu')!.value))
      )
      .subscribe((contenus: IContenu[]) => (this.contenusCollection = contenus));

    this.coursService
      .query()
      .pipe(map((res: HttpResponse<ICours[]>) => res.body ?? []))
      .pipe(map((cours: ICours[]) => this.coursService.addCoursToCollectionIfMissing(cours, this.editForm.get('cours')!.value)))
      .subscribe((cours: ICours[]) => (this.coursSharedCollection = cours));

    this.salleService
      .query()
      .pipe(map((res: HttpResponse<ISalle[]>) => res.body ?? []))
      .pipe(map((salles: ISalle[]) => this.salleService.addSalleToCollectionIfMissing(salles, this.editForm.get('salle')!.value)))
      .subscribe((salles: ISalle[]) => (this.sallesSharedCollection = salles));

    this.groupeService
      .query()
      .pipe(map((res: HttpResponse<IGroupe[]>) => res.body ?? []))
      .pipe(map((groupes: IGroupe[]) => this.groupeService.addGroupeToCollectionIfMissing(groupes, this.editForm.get('groupe')!.value)))
      .subscribe((groupes: IGroupe[]) => (this.groupesSharedCollection = groupes));
  }

  protected createFromForm(): ISeance {
    return {
      ...new Seance(),
      id: this.editForm.get(['id'])!.value,
      jourSeance: this.editForm.get(['jourSeance'])!.value,
      dateSeance: this.editForm.get(['dateSeance'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value ? dayjs(this.editForm.get(['dateDebut'])!.value, DATE_TIME_FORMAT) : undefined,
      dateFin: this.editForm.get(['dateFin'])!.value ? dayjs(this.editForm.get(['dateFin'])!.value, DATE_TIME_FORMAT) : undefined,
      contenu: this.editForm.get(['contenu'])!.value,
      cours: this.editForm.get(['cours'])!.value,
      salle: this.editForm.get(['salle'])!.value,
      groupe: this.editForm.get(['groupe'])!.value,
    };
  }
}
