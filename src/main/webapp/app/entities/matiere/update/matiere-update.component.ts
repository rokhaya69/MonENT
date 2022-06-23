import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatiere, Matiere } from '../matiere.model';
import { MatiereService } from '../service/matiere.service';
import { IProgramme } from 'app/entities/programme/programme.model';
import { ProgrammeService } from 'app/entities/programme/service/programme.service';

@Component({
  selector: 'jhi-matiere-update',
  templateUrl: './matiere-update.component.html',
})
export class MatiereUpdateComponent implements OnInit {
  isSaving = false;

  programmesSharedCollection: IProgramme[] = [];

  editForm = this.fb.group({
    id: [],
    intituleMatiere: [null, [Validators.required]],
    programme: [],
  });

  constructor(
    protected matiereService: MatiereService,
    protected programmeService: ProgrammeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.updateForm(matiere);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matiere = this.createFromForm();
    if (matiere.id !== undefined) {
      this.subscribeToSaveResponse(this.matiereService.update(matiere));
    } else {
      this.subscribeToSaveResponse(this.matiereService.create(matiere));
    }
  }

  trackProgrammeById(index: number, item: IProgramme): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatiere>>): void {
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

  protected updateForm(matiere: IMatiere): void {
    this.editForm.patchValue({
      id: matiere.id,
      intituleMatiere: matiere.intituleMatiere,
      programme: matiere.programme,
    });

    this.programmesSharedCollection = this.programmeService.addProgrammeToCollectionIfMissing(
      this.programmesSharedCollection,
      matiere.programme
    );
  }

  protected loadRelationshipsOptions(): void {
    this.programmeService
      .query()
      .pipe(map((res: HttpResponse<IProgramme[]>) => res.body ?? []))
      .pipe(
        map((programmes: IProgramme[]) =>
          this.programmeService.addProgrammeToCollectionIfMissing(programmes, this.editForm.get('programme')!.value)
        )
      )
      .subscribe((programmes: IProgramme[]) => (this.programmesSharedCollection = programmes));
  }

  protected createFromForm(): IMatiere {
    return {
      ...new Matiere(),
      id: this.editForm.get(['id'])!.value,
      intituleMatiere: this.editForm.get(['intituleMatiere'])!.value,
      programme: this.editForm.get(['programme'])!.value,
    };
  }
}
