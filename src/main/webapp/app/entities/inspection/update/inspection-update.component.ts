import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInspection, Inspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';
import { TypeInspec } from 'app/entities/enumerations/type-inspec.model';

@Component({
  selector: 'jhi-inspection-update',
  templateUrl: './inspection-update.component.html',
})
export class InspectionUpdateComponent implements OnInit {
  isSaving = false;
  typeInspecValues = Object.keys(TypeInspec);

  communesCollection: ICommune[] = [];
  inspecteursCollection: IInspecteur[] = [];

  editForm = this.fb.group({
    id: [],
    nomInspec: [null, [Validators.required]],
    typeInspec: [null, [Validators.required]],
    email: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    commune: [],
    inspecteur: [],
  });

  constructor(
    protected inspectionService: InspectionService,
    protected communeService: CommuneService,
    protected inspecteurService: InspecteurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspection }) => {
      this.updateForm(inspection);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspection = this.createFromForm();
    if (inspection.id !== undefined) {
      this.subscribeToSaveResponse(this.inspectionService.update(inspection));
    } else {
      this.subscribeToSaveResponse(this.inspectionService.create(inspection));
    }
  }

  trackCommuneById(index: number, item: ICommune): number {
    return item.id!;
  }

  trackInspecteurById(index: number, item: IInspecteur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspection>>): void {
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

  protected updateForm(inspection: IInspection): void {
    this.editForm.patchValue({
      id: inspection.id,
      nomInspec: inspection.nomInspec,
      typeInspec: inspection.typeInspec,
      email: inspection.email,
      telephone: inspection.telephone,
      commune: inspection.commune,
      inspecteur: inspection.inspecteur,
    });

    this.communesCollection = this.communeService.addCommuneToCollectionIfMissing(this.communesCollection, inspection.commune);
    this.inspecteursCollection = this.inspecteurService.addInspecteurToCollectionIfMissing(
      this.inspecteursCollection,
      inspection.inspecteur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communeService
      .query({ filter: 'inspection-is-null' })
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(
        map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing(communes, this.editForm.get('commune')!.value))
      )
      .subscribe((communes: ICommune[]) => (this.communesCollection = communes));

    this.inspecteurService
      .query({ filter: 'inspection-is-null' })
      .pipe(map((res: HttpResponse<IInspecteur[]>) => res.body ?? []))
      .pipe(
        map((inspecteurs: IInspecteur[]) =>
          this.inspecteurService.addInspecteurToCollectionIfMissing(inspecteurs, this.editForm.get('inspecteur')!.value)
        )
      )
      .subscribe((inspecteurs: IInspecteur[]) => (this.inspecteursCollection = inspecteurs));
  }

  protected createFromForm(): IInspection {
    return {
      ...new Inspection(),
      id: this.editForm.get(['id'])!.value,
      nomInspec: this.editForm.get(['nomInspec'])!.value,
      typeInspec: this.editForm.get(['typeInspec'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      commune: this.editForm.get(['commune'])!.value,
      inspecteur: this.editForm.get(['inspecteur'])!.value,
    };
  }
}
