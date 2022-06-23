import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRessource, Ressource } from '../ressource.model';
import { RessourceService } from '../service/ressource.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';
import { ICours } from 'app/entities/cours/cours.model';
import { CoursService } from 'app/entities/cours/service/cours.service';
import { ISurveillant } from 'app/entities/surveillant/surveillant.model';
import { SurveillantService } from 'app/entities/surveillant/service/surveillant.service';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { ProviseurService } from 'app/entities/proviseur/service/proviseur.service';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { DirecteurService } from 'app/entities/directeur/service/directeur.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';
import { TypeRessource } from 'app/entities/enumerations/type-ressource.model';

@Component({
  selector: 'jhi-ressource-update',
  templateUrl: './ressource-update.component.html',
})
export class RessourceUpdateComponent implements OnInit {
  isSaving = false;
  typeRessourceValues = Object.keys(TypeRessource);

  apprenantsSharedCollection: IApprenant[] = [];
  groupesSharedCollection: IGroupe[] = [];
  coursSharedCollection: ICours[] = [];
  surveillantsSharedCollection: ISurveillant[] = [];
  proviseursSharedCollection: IProviseur[] = [];
  directeursSharedCollection: IDirecteur[] = [];
  inspecteursSharedCollection: IInspecteur[] = [];

  editForm = this.fb.group({
    id: [],
    libelRessource: [null, [Validators.required]],
    typeRessource: [null, [Validators.required]],
    lienRessource: [null, [Validators.required]],
    lienRessourceContentType: [],
    dateMise: [],
    apprenant: [],
    groupe: [],
    cours: [],
    persoAdmin: [],
    proviseur: [],
    directeur: [],
    inspecteur: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ressourceService: RessourceService,
    protected apprenantService: ApprenantService,
    protected groupeService: GroupeService,
    protected coursService: CoursService,
    protected surveillantService: SurveillantService,
    protected proviseurService: ProviseurService,
    protected directeurService: DirecteurService,
    protected inspecteurService: InspecteurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ressource }) => {
      if (ressource.id === undefined) {
        const today = dayjs().startOf('day');
        ressource.dateMise = today;
      }

      this.updateForm(ressource);

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
    const ressource = this.createFromForm();
    if (ressource.id !== undefined) {
      this.subscribeToSaveResponse(this.ressourceService.update(ressource));
    } else {
      this.subscribeToSaveResponse(this.ressourceService.create(ressource));
    }
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  trackGroupeById(index: number, item: IGroupe): number {
    return item.id!;
  }

  trackCoursById(index: number, item: ICours): number {
    return item.id!;
  }

  trackSurveillantById(index: number, item: ISurveillant): number {
    return item.id!;
  }

  trackProviseurById(index: number, item: IProviseur): number {
    return item.id!;
  }

  trackDirecteurById(index: number, item: IDirecteur): number {
    return item.id!;
  }

  trackInspecteurById(index: number, item: IInspecteur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRessource>>): void {
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

  protected updateForm(ressource: IRessource): void {
    this.editForm.patchValue({
      id: ressource.id,
      libelRessource: ressource.libelRessource,
      typeRessource: ressource.typeRessource,
      lienRessource: ressource.lienRessource,
      lienRessourceContentType: ressource.lienRessourceContentType,
      dateMise: ressource.dateMise ? ressource.dateMise.format(DATE_TIME_FORMAT) : null,
      apprenant: ressource.apprenant,
      groupe: ressource.groupe,
      cours: ressource.cours,
      persoAdmin: ressource.persoAdmin,
      proviseur: ressource.proviseur,
      directeur: ressource.directeur,
      inspecteur: ressource.inspecteur,
    });

    this.apprenantsSharedCollection = this.apprenantService.addApprenantToCollectionIfMissing(
      this.apprenantsSharedCollection,
      ressource.apprenant
    );
    this.groupesSharedCollection = this.groupeService.addGroupeToCollectionIfMissing(this.groupesSharedCollection, ressource.groupe);
    this.coursSharedCollection = this.coursService.addCoursToCollectionIfMissing(this.coursSharedCollection, ressource.cours);
    this.surveillantsSharedCollection = this.surveillantService.addSurveillantToCollectionIfMissing(
      this.surveillantsSharedCollection,
      ressource.persoAdmin
    );
    this.proviseursSharedCollection = this.proviseurService.addProviseurToCollectionIfMissing(
      this.proviseursSharedCollection,
      ressource.proviseur
    );
    this.directeursSharedCollection = this.directeurService.addDirecteurToCollectionIfMissing(
      this.directeursSharedCollection,
      ressource.directeur
    );
    this.inspecteursSharedCollection = this.inspecteurService.addInspecteurToCollectionIfMissing(
      this.inspecteursSharedCollection,
      ressource.inspecteur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apprenantService
      .query()
      .pipe(map((res: HttpResponse<IApprenant[]>) => res.body ?? []))
      .pipe(
        map((apprenants: IApprenant[]) =>
          this.apprenantService.addApprenantToCollectionIfMissing(apprenants, this.editForm.get('apprenant')!.value)
        )
      )
      .subscribe((apprenants: IApprenant[]) => (this.apprenantsSharedCollection = apprenants));

    this.groupeService
      .query()
      .pipe(map((res: HttpResponse<IGroupe[]>) => res.body ?? []))
      .pipe(map((groupes: IGroupe[]) => this.groupeService.addGroupeToCollectionIfMissing(groupes, this.editForm.get('groupe')!.value)))
      .subscribe((groupes: IGroupe[]) => (this.groupesSharedCollection = groupes));

    this.coursService
      .query()
      .pipe(map((res: HttpResponse<ICours[]>) => res.body ?? []))
      .pipe(map((cours: ICours[]) => this.coursService.addCoursToCollectionIfMissing(cours, this.editForm.get('cours')!.value)))
      .subscribe((cours: ICours[]) => (this.coursSharedCollection = cours));

    this.surveillantService
      .query()
      .pipe(map((res: HttpResponse<ISurveillant[]>) => res.body ?? []))
      .pipe(
        map((surveillants: ISurveillant[]) =>
          this.surveillantService.addSurveillantToCollectionIfMissing(surveillants, this.editForm.get('persoAdmin')!.value)
        )
      )
      .subscribe((surveillants: ISurveillant[]) => (this.surveillantsSharedCollection = surveillants));

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

    this.inspecteurService
      .query()
      .pipe(map((res: HttpResponse<IInspecteur[]>) => res.body ?? []))
      .pipe(
        map((inspecteurs: IInspecteur[]) =>
          this.inspecteurService.addInspecteurToCollectionIfMissing(inspecteurs, this.editForm.get('inspecteur')!.value)
        )
      )
      .subscribe((inspecteurs: IInspecteur[]) => (this.inspecteursSharedCollection = inspecteurs));
  }

  protected createFromForm(): IRessource {
    return {
      ...new Ressource(),
      id: this.editForm.get(['id'])!.value,
      libelRessource: this.editForm.get(['libelRessource'])!.value,
      typeRessource: this.editForm.get(['typeRessource'])!.value,
      lienRessourceContentType: this.editForm.get(['lienRessourceContentType'])!.value,
      lienRessource: this.editForm.get(['lienRessource'])!.value,
      dateMise: this.editForm.get(['dateMise'])!.value ? dayjs(this.editForm.get(['dateMise'])!.value, DATE_TIME_FORMAT) : undefined,
      apprenant: this.editForm.get(['apprenant'])!.value,
      groupe: this.editForm.get(['groupe'])!.value,
      cours: this.editForm.get(['cours'])!.value,
      persoAdmin: this.editForm.get(['persoAdmin'])!.value,
      proviseur: this.editForm.get(['proviseur'])!.value,
      directeur: this.editForm.get(['directeur'])!.value,
      inspecteur: this.editForm.get(['inspecteur'])!.value,
    };
  }
}
