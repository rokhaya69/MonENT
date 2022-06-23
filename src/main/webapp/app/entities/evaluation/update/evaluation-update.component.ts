import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEvaluation, Evaluation } from '../evaluation.model';
import { EvaluationService } from '../service/evaluation.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';
import { TypeEvalu } from 'app/entities/enumerations/type-evalu.model';

@Component({
  selector: 'jhi-evaluation-update',
  templateUrl: './evaluation-update.component.html',
})
export class EvaluationUpdateComponent implements OnInit {
  isSaving = false;
  typeEvaluValues = Object.keys(TypeEvalu);

  matieresSharedCollection: IMatiere[] = [];
  groupesSharedCollection: IGroupe[] = [];
  professeursSharedCollection: IProfesseur[] = [];
  sallesSharedCollection: ISalle[] = [];

  editForm = this.fb.group({
    id: [],
    nomEvalu: [null, [Validators.required]],
    typeEvalu: [null, [Validators.required]],
    dateEva: [null, [Validators.required]],
    heureDebEva: [null, [Validators.required]],
    heureFinEva: [null, [Validators.required]],
    matiere: [],
    groupe: [],
    professeur: [],
    salle: [],
  });

  constructor(
    protected evaluationService: EvaluationService,
    protected matiereService: MatiereService,
    protected groupeService: GroupeService,
    protected professeurService: ProfesseurService,
    protected salleService: SalleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluation }) => {
      if (evaluation.id === undefined) {
        const today = dayjs().startOf('day');
        evaluation.heureDebEva = today;
        evaluation.heureFinEva = today;
      }

      this.updateForm(evaluation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaluation = this.createFromForm();
    if (evaluation.id !== undefined) {
      this.subscribeToSaveResponse(this.evaluationService.update(evaluation));
    } else {
      this.subscribeToSaveResponse(this.evaluationService.create(evaluation));
    }
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  trackGroupeById(index: number, item: IGroupe): number {
    return item.id!;
  }

  trackProfesseurById(index: number, item: IProfesseur): number {
    return item.id!;
  }

  trackSalleById(index: number, item: ISalle): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluation>>): void {
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

  protected updateForm(evaluation: IEvaluation): void {
    this.editForm.patchValue({
      id: evaluation.id,
      nomEvalu: evaluation.nomEvalu,
      typeEvalu: evaluation.typeEvalu,
      dateEva: evaluation.dateEva,
      heureDebEva: evaluation.heureDebEva ? evaluation.heureDebEva.format(DATE_TIME_FORMAT) : null,
      heureFinEva: evaluation.heureFinEva ? evaluation.heureFinEva.format(DATE_TIME_FORMAT) : null,
      matiere: evaluation.matiere,
      groupe: evaluation.groupe,
      professeur: evaluation.professeur,
      salle: evaluation.salle,
    });

    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, evaluation.matiere);
    this.groupesSharedCollection = this.groupeService.addGroupeToCollectionIfMissing(this.groupesSharedCollection, evaluation.groupe);
    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      evaluation.professeur
    );
    this.sallesSharedCollection = this.salleService.addSalleToCollectionIfMissing(this.sallesSharedCollection, evaluation.salle);
  }

  protected loadRelationshipsOptions(): void {
    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));

    this.groupeService
      .query()
      .pipe(map((res: HttpResponse<IGroupe[]>) => res.body ?? []))
      .pipe(map((groupes: IGroupe[]) => this.groupeService.addGroupeToCollectionIfMissing(groupes, this.editForm.get('groupe')!.value)))
      .subscribe((groupes: IGroupe[]) => (this.groupesSharedCollection = groupes));

    this.professeurService
      .query()
      .pipe(map((res: HttpResponse<IProfesseur[]>) => res.body ?? []))
      .pipe(
        map((professeurs: IProfesseur[]) =>
          this.professeurService.addProfesseurToCollectionIfMissing(professeurs, this.editForm.get('professeur')!.value)
        )
      )
      .subscribe((professeurs: IProfesseur[]) => (this.professeursSharedCollection = professeurs));

    this.salleService
      .query()
      .pipe(map((res: HttpResponse<ISalle[]>) => res.body ?? []))
      .pipe(map((salles: ISalle[]) => this.salleService.addSalleToCollectionIfMissing(salles, this.editForm.get('salle')!.value)))
      .subscribe((salles: ISalle[]) => (this.sallesSharedCollection = salles));
  }

  protected createFromForm(): IEvaluation {
    return {
      ...new Evaluation(),
      id: this.editForm.get(['id'])!.value,
      nomEvalu: this.editForm.get(['nomEvalu'])!.value,
      typeEvalu: this.editForm.get(['typeEvalu'])!.value,
      dateEva: this.editForm.get(['dateEva'])!.value,
      heureDebEva: this.editForm.get(['heureDebEva'])!.value
        ? dayjs(this.editForm.get(['heureDebEva'])!.value, DATE_TIME_FORMAT)
        : undefined,
      heureFinEva: this.editForm.get(['heureFinEva'])!.value
        ? dayjs(this.editForm.get(['heureFinEva'])!.value, DATE_TIME_FORMAT)
        : undefined,
      matiere: this.editForm.get(['matiere'])!.value,
      groupe: this.editForm.get(['groupe'])!.value,
      professeur: this.editForm.get(['professeur'])!.value,
      salle: this.editForm.get(['salle'])!.value,
    };
  }
}
