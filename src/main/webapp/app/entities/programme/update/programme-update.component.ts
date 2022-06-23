import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProgramme, Programme } from '../programme.model';
import { ProgrammeService } from '../service/programme.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { ISerie } from 'app/entities/serie/serie.model';
import { SerieService } from 'app/entities/serie/service/serie.service';

@Component({
  selector: 'jhi-programme-update',
  templateUrl: './programme-update.component.html',
})
export class ProgrammeUpdateComponent implements OnInit {
  isSaving = false;

  filieresCollection: IFiliere[] = [];
  seriesCollection: ISerie[] = [];

  editForm = this.fb.group({
    id: [],
    nomProgram: [null, [Validators.required]],
    contenuProgram: [],
    contenuProgramContentType: [],
    annee: [null, [Validators.required]],
    filiere: [],
    serie: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected programmeService: ProgrammeService,
    protected filiereService: FiliereService,
    protected serieService: SerieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programme }) => {
      this.updateForm(programme);

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
    const programme = this.createFromForm();
    if (programme.id !== undefined) {
      this.subscribeToSaveResponse(this.programmeService.update(programme));
    } else {
      this.subscribeToSaveResponse(this.programmeService.create(programme));
    }
  }

  trackFiliereById(index: number, item: IFiliere): number {
    return item.id!;
  }

  trackSerieById(index: number, item: ISerie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgramme>>): void {
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

  protected updateForm(programme: IProgramme): void {
    this.editForm.patchValue({
      id: programme.id,
      nomProgram: programme.nomProgram,
      contenuProgram: programme.contenuProgram,
      contenuProgramContentType: programme.contenuProgramContentType,
      annee: programme.annee,
      filiere: programme.filiere,
      serie: programme.serie,
    });

    this.filieresCollection = this.filiereService.addFiliereToCollectionIfMissing(this.filieresCollection, programme.filiere);
    this.seriesCollection = this.serieService.addSerieToCollectionIfMissing(this.seriesCollection, programme.serie);
  }

  protected loadRelationshipsOptions(): void {
    this.filiereService
      .query({ filter: 'programme-is-null' })
      .pipe(map((res: HttpResponse<IFiliere[]>) => res.body ?? []))
      .pipe(
        map((filieres: IFiliere[]) => this.filiereService.addFiliereToCollectionIfMissing(filieres, this.editForm.get('filiere')!.value))
      )
      .subscribe((filieres: IFiliere[]) => (this.filieresCollection = filieres));

    this.serieService
      .query({ filter: 'programme-is-null' })
      .pipe(map((res: HttpResponse<ISerie[]>) => res.body ?? []))
      .pipe(map((series: ISerie[]) => this.serieService.addSerieToCollectionIfMissing(series, this.editForm.get('serie')!.value)))
      .subscribe((series: ISerie[]) => (this.seriesCollection = series));
  }

  protected createFromForm(): IProgramme {
    return {
      ...new Programme(),
      id: this.editForm.get(['id'])!.value,
      nomProgram: this.editForm.get(['nomProgram'])!.value,
      contenuProgramContentType: this.editForm.get(['contenuProgramContentType'])!.value,
      contenuProgram: this.editForm.get(['contenuProgram'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      serie: this.editForm.get(['serie'])!.value,
    };
  }
}
