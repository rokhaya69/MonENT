import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAbsence, Absence } from '../absence.model';
import { AbsenceService } from '../service/absence.service';
import { ISeance } from 'app/entities/seance/seance.model';
import { SeanceService } from 'app/entities/seance/service/seance.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';
import { EvaluationService } from 'app/entities/evaluation/service/evaluation.service';

@Component({
  selector: 'jhi-absence-update',
  templateUrl: './absence-update.component.html',
})
export class AbsenceUpdateComponent implements OnInit {
  isSaving = false;

  seancesSharedCollection: ISeance[] = [];
  apprenantsSharedCollection: IApprenant[] = [];
  evaluationsSharedCollection: IEvaluation[] = [];

  editForm = this.fb.group({
    id: [],
    motif: [null, [Validators.required]],
    justifie: [null, [Validators.required]],
    seance: [],
    apprenant: [],
    evaluation: [],
  });

  constructor(
    protected absenceService: AbsenceService,
    protected seanceService: SeanceService,
    protected apprenantService: ApprenantService,
    protected evaluationService: EvaluationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ absence }) => {
      this.updateForm(absence);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const absence = this.createFromForm();
    if (absence.id !== undefined) {
      this.subscribeToSaveResponse(this.absenceService.update(absence));
    } else {
      this.subscribeToSaveResponse(this.absenceService.create(absence));
    }
  }

  trackSeanceById(index: number, item: ISeance): number {
    return item.id!;
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  trackEvaluationById(index: number, item: IEvaluation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbsence>>): void {
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

  protected updateForm(absence: IAbsence): void {
    this.editForm.patchValue({
      id: absence.id,
      motif: absence.motif,
      justifie: absence.justifie,
      seance: absence.seance,
      apprenant: absence.apprenant,
      evaluation: absence.evaluation,
    });

    this.seancesSharedCollection = this.seanceService.addSeanceToCollectionIfMissing(this.seancesSharedCollection, absence.seance);
    this.apprenantsSharedCollection = this.apprenantService.addApprenantToCollectionIfMissing(
      this.apprenantsSharedCollection,
      absence.apprenant
    );
    this.evaluationsSharedCollection = this.evaluationService.addEvaluationToCollectionIfMissing(
      this.evaluationsSharedCollection,
      absence.evaluation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.seanceService
      .query()
      .pipe(map((res: HttpResponse<ISeance[]>) => res.body ?? []))
      .pipe(map((seances: ISeance[]) => this.seanceService.addSeanceToCollectionIfMissing(seances, this.editForm.get('seance')!.value)))
      .subscribe((seances: ISeance[]) => (this.seancesSharedCollection = seances));

    this.apprenantService
      .query()
      .pipe(map((res: HttpResponse<IApprenant[]>) => res.body ?? []))
      .pipe(
        map((apprenants: IApprenant[]) =>
          this.apprenantService.addApprenantToCollectionIfMissing(apprenants, this.editForm.get('apprenant')!.value)
        )
      )
      .subscribe((apprenants: IApprenant[]) => (this.apprenantsSharedCollection = apprenants));

    this.evaluationService
      .query()
      .pipe(map((res: HttpResponse<IEvaluation[]>) => res.body ?? []))
      .pipe(
        map((evaluations: IEvaluation[]) =>
          this.evaluationService.addEvaluationToCollectionIfMissing(evaluations, this.editForm.get('evaluation')!.value)
        )
      )
      .subscribe((evaluations: IEvaluation[]) => (this.evaluationsSharedCollection = evaluations));
  }

  protected createFromForm(): IAbsence {
    return {
      ...new Absence(),
      id: this.editForm.get(['id'])!.value,
      motif: this.editForm.get(['motif'])!.value,
      justifie: this.editForm.get(['justifie'])!.value,
      seance: this.editForm.get(['seance'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
      evaluation: this.editForm.get(['evaluation'])!.value,
    };
  }
}
