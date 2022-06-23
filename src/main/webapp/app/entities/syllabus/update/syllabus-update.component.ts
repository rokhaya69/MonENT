import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISyllabus, Syllabus } from '../syllabus.model';
import { SyllabusService } from '../service/syllabus.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProgramme } from 'app/entities/programme/programme.model';
import { ProgrammeService } from 'app/entities/programme/service/programme.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-syllabus-update',
  templateUrl: './syllabus-update.component.html',
})
export class SyllabusUpdateComponent implements OnInit {
  isSaving = false;

  programmesSharedCollection: IProgramme[] = [];
  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    nomSyllabus: [null, [Validators.required]],
    syllabus: [],
    syllabusContentType: [],
    programme: [],
    matiere: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected syllabusService: SyllabusService,
    protected programmeService: ProgrammeService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ syllabus }) => {
      this.updateForm(syllabus);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('entMefpaiv2App.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const syllabus = this.createFromForm();
    if (syllabus.id !== undefined) {
      this.subscribeToSaveResponse(this.syllabusService.update(syllabus));
    } else {
      this.subscribeToSaveResponse(this.syllabusService.create(syllabus));
    }
  }

  trackProgrammeById(index: number, item: IProgramme): number {
    return item.id!;
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISyllabus>>): void {
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

  protected updateForm(syllabus: ISyllabus): void {
    this.editForm.patchValue({
      id: syllabus.id,
      nomSyllabus: syllabus.nomSyllabus,
      syllabus: syllabus.syllabus,
      syllabusContentType: syllabus.syllabusContentType,
      programme: syllabus.programme,
      matiere: syllabus.matiere,
    });

    this.programmesSharedCollection = this.programmeService.addProgrammeToCollectionIfMissing(
      this.programmesSharedCollection,
      syllabus.programme
    );
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, syllabus.matiere);
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

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): ISyllabus {
    return {
      ...new Syllabus(),
      id: this.editForm.get(['id'])!.value,
      nomSyllabus: this.editForm.get(['nomSyllabus'])!.value,
      syllabusContentType: this.editForm.get(['syllabusContentType'])!.value,
      syllabus: this.editForm.get(['syllabus'])!.value,
      programme: this.editForm.get(['programme'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
    };
  }
}
