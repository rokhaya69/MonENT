import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';
import { EvaluationService } from 'app/entities/evaluation/service/evaluation.service';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  apprenantsCollection: IApprenant[] = [];
  evaluationsSharedCollection: IEvaluation[] = [];

  editForm = this.fb.group({
    id: [],
    uneNote: [null, [Validators.required]],
    apprenant: [],
    evaluation: [],
  });

  constructor(
    protected noteService: NoteService,
    protected apprenantService: ApprenantService,
    protected evaluationService: EvaluationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const note = this.createFromForm();
    if (note.id !== undefined) {
      this.subscribeToSaveResponse(this.noteService.update(note));
    } else {
      this.subscribeToSaveResponse(this.noteService.create(note));
    }
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  trackEvaluationById(index: number, item: IEvaluation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
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

  protected updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      uneNote: note.uneNote,
      apprenant: note.apprenant,
      evaluation: note.evaluation,
    });

    this.apprenantsCollection = this.apprenantService.addApprenantToCollectionIfMissing(this.apprenantsCollection, note.apprenant);
    this.evaluationsSharedCollection = this.evaluationService.addEvaluationToCollectionIfMissing(
      this.evaluationsSharedCollection,
      note.evaluation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apprenantService
      .query({ filter: 'note-is-null' })
      .pipe(map((res: HttpResponse<IApprenant[]>) => res.body ?? []))
      .pipe(
        map((apprenants: IApprenant[]) =>
          this.apprenantService.addApprenantToCollectionIfMissing(apprenants, this.editForm.get('apprenant')!.value)
        )
      )
      .subscribe((apprenants: IApprenant[]) => (this.apprenantsCollection = apprenants));

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

  protected createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      uneNote: this.editForm.get(['uneNote'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
      evaluation: this.editForm.get(['evaluation'])!.value,
    };
  }
}
