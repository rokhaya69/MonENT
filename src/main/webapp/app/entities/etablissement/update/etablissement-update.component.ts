import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEtablissement, Etablissement } from '../etablissement.model';
import { EtablissementService } from '../service/etablissement.service';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { ProviseurService } from 'app/entities/proviseur/service/proviseur.service';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { DirecteurService } from 'app/entities/directeur/service/directeur.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { RessourceService } from 'app/entities/ressource/service/ressource.service';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { InspectionService } from 'app/entities/inspection/service/inspection.service';
import { TypeEtab } from 'app/entities/enumerations/type-etab.model';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  typeEtabValues = Object.keys(TypeEtab);

  proviseursCollection: IProviseur[] = [];
  directeursCollection: IDirecteur[] = [];
  communesCollection: ICommune[] = [];
  ressourcesSharedCollection: IRessource[] = [];
  inspectionsSharedCollection: IInspection[] = [];

  editForm = this.fb.group({
    id: [],
    nomEtab: [null, [Validators.required]],
    typeEtab: [null, [Validators.required]],
    email: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    proviseur: [],
    directeur: [],
    commune: [],
    ressources: [],
    inspection: [],
  });

  constructor(
    protected etablissementService: EtablissementService,
    protected proviseurService: ProviseurService,
    protected directeurService: DirecteurService,
    protected communeService: CommuneService,
    protected ressourceService: RessourceService,
    protected inspectionService: InspectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  trackProviseurById(index: number, item: IProviseur): number {
    return item.id!;
  }

  trackDirecteurById(index: number, item: IDirecteur): number {
    return item.id!;
  }

  trackCommuneById(index: number, item: ICommune): number {
    return item.id!;
  }

  trackRessourceById(index: number, item: IRessource): number {
    return item.id!;
  }

  trackInspectionById(index: number, item: IInspection): number {
    return item.id!;
  }

  getSelectedRessource(option: IRessource, selectedVals?: IRessource[]): IRessource {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
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

  protected updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      nomEtab: etablissement.nomEtab,
      typeEtab: etablissement.typeEtab,
      email: etablissement.email,
      telephone: etablissement.telephone,
      proviseur: etablissement.proviseur,
      directeur: etablissement.directeur,
      commune: etablissement.commune,
      ressources: etablissement.ressources,
      inspection: etablissement.inspection,
    });

    this.proviseursCollection = this.proviseurService.addProviseurToCollectionIfMissing(this.proviseursCollection, etablissement.proviseur);
    this.directeursCollection = this.directeurService.addDirecteurToCollectionIfMissing(this.directeursCollection, etablissement.directeur);
    this.communesCollection = this.communeService.addCommuneToCollectionIfMissing(this.communesCollection, etablissement.commune);
    this.ressourcesSharedCollection = this.ressourceService.addRessourceToCollectionIfMissing(
      this.ressourcesSharedCollection,
      ...(etablissement.ressources ?? [])
    );
    this.inspectionsSharedCollection = this.inspectionService.addInspectionToCollectionIfMissing(
      this.inspectionsSharedCollection,
      etablissement.inspection
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proviseurService
      .query({ filter: 'etablissement-is-null' })
      .pipe(map((res: HttpResponse<IProviseur[]>) => res.body ?? []))
      .pipe(
        map((proviseurs: IProviseur[]) =>
          this.proviseurService.addProviseurToCollectionIfMissing(proviseurs, this.editForm.get('proviseur')!.value)
        )
      )
      .subscribe((proviseurs: IProviseur[]) => (this.proviseursCollection = proviseurs));

    this.directeurService
      .query({ filter: 'etablissement-is-null' })
      .pipe(map((res: HttpResponse<IDirecteur[]>) => res.body ?? []))
      .pipe(
        map((directeurs: IDirecteur[]) =>
          this.directeurService.addDirecteurToCollectionIfMissing(directeurs, this.editForm.get('directeur')!.value)
        )
      )
      .subscribe((directeurs: IDirecteur[]) => (this.directeursCollection = directeurs));

    this.communeService
      .query({ filter: 'etablissement-is-null' })
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(
        map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing(communes, this.editForm.get('commune')!.value))
      )
      .subscribe((communes: ICommune[]) => (this.communesCollection = communes));

    this.ressourceService
      .query()
      .pipe(map((res: HttpResponse<IRessource[]>) => res.body ?? []))
      .pipe(
        map((ressources: IRessource[]) =>
          this.ressourceService.addRessourceToCollectionIfMissing(ressources, ...(this.editForm.get('ressources')!.value ?? []))
        )
      )
      .subscribe((ressources: IRessource[]) => (this.ressourcesSharedCollection = ressources));

    this.inspectionService
      .query()
      .pipe(map((res: HttpResponse<IInspection[]>) => res.body ?? []))
      .pipe(
        map((inspections: IInspection[]) =>
          this.inspectionService.addInspectionToCollectionIfMissing(inspections, this.editForm.get('inspection')!.value)
        )
      )
      .subscribe((inspections: IInspection[]) => (this.inspectionsSharedCollection = inspections));
  }

  protected createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      nomEtab: this.editForm.get(['nomEtab'])!.value,
      typeEtab: this.editForm.get(['typeEtab'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      proviseur: this.editForm.get(['proviseur'])!.value,
      directeur: this.editForm.get(['directeur'])!.value,
      commune: this.editForm.get(['commune'])!.value,
      ressources: this.editForm.get(['ressources'])!.value,
      inspection: this.editForm.get(['inspection'])!.value,
    };
  }
}
