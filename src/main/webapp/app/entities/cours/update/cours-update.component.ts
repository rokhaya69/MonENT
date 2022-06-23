import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICours, Cours } from '../cours.model';
import { CoursService } from '../service/cours.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IClasse } from 'app/entities/classe/classe.model';
import { ClasseService } from 'app/entities/classe/service/classe.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { ISyllabus } from 'app/entities/syllabus/syllabus.model';
import { SyllabusService } from 'app/entities/syllabus/service/syllabus.service';

@Component({
  selector: 'jhi-cours-update',
  templateUrl: './cours-update.component.html',
})
export class CoursUpdateComponent implements OnInit {
  isSaving = false;

  matieresSharedCollection: IMatiere[] = [];
  classesSharedCollection: IClasse[] = [];
  professeursSharedCollection: IProfesseur[] = [];
  syllabiSharedCollection: ISyllabus[] = [];

  editForm = this.fb.group({
    id: [],
    libelleCours: [null, [Validators.required]],
    matiere: [],
    classe: [],
    professeur: [],
    syllabus: [],
  });

  constructor(
    protected coursService: CoursService,
    protected matiereService: MatiereService,
    protected classeService: ClasseService,
    protected professeurService: ProfesseurService,
    protected syllabusService: SyllabusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cours }) => {
      this.updateForm(cours);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cours = this.createFromForm();
    if (cours.id !== undefined) {
      this.subscribeToSaveResponse(this.coursService.update(cours));
    } else {
      this.subscribeToSaveResponse(this.coursService.create(cours));
    }
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  trackClasseById(index: number, item: IClasse): number {
    return item.id!;
  }

  trackProfesseurById(index: number, item: IProfesseur): number {
    return item.id!;
  }

  trackSyllabusById(index: number, item: ISyllabus): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICours>>): void {
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

  protected updateForm(cours: ICours): void {
    this.editForm.patchValue({
      id: cours.id,
      libelleCours: cours.libelleCours,
      matiere: cours.matiere,
      classe: cours.classe,
      professeur: cours.professeur,
      syllabus: cours.syllabus,
    });

    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, cours.matiere);
    this.classesSharedCollection = this.classeService.addClasseToCollectionIfMissing(this.classesSharedCollection, cours.classe);
    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      cours.professeur
    );
    this.syllabiSharedCollection = this.syllabusService.addSyllabusToCollectionIfMissing(this.syllabiSharedCollection, cours.syllabus);
  }

  protected loadRelationshipsOptions(): void {
    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));

    this.classeService
      .query()
      .pipe(map((res: HttpResponse<IClasse[]>) => res.body ?? []))
      .pipe(map((classes: IClasse[]) => this.classeService.addClasseToCollectionIfMissing(classes, this.editForm.get('classe')!.value)))
      .subscribe((classes: IClasse[]) => (this.classesSharedCollection = classes));

    this.professeurService
      .query()
      .pipe(map((res: HttpResponse<IProfesseur[]>) => res.body ?? []))
      .pipe(
        map((professeurs: IProfesseur[]) =>
          this.professeurService.addProfesseurToCollectionIfMissing(professeurs, this.editForm.get('professeur')!.value)
        )
      )
      .subscribe((professeurs: IProfesseur[]) => (this.professeursSharedCollection = professeurs));

    this.syllabusService
      .query()
      .pipe(map((res: HttpResponse<ISyllabus[]>) => res.body ?? []))
      .pipe(
        map((syllabi: ISyllabus[]) => this.syllabusService.addSyllabusToCollectionIfMissing(syllabi, this.editForm.get('syllabus')!.value))
      )
      .subscribe((syllabi: ISyllabus[]) => (this.syllabiSharedCollection = syllabi));
  }

  protected createFromForm(): ICours {
    return {
      ...new Cours(),
      id: this.editForm.get(['id'])!.value,
      libelleCours: this.editForm.get(['libelleCours'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
      classe: this.editForm.get(['classe'])!.value,
      professeur: this.editForm.get(['professeur'])!.value,
      syllabus: this.editForm.get(['syllabus'])!.value,
    };
  }
}
