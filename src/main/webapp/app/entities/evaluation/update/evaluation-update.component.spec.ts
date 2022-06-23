import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvaluationService } from '../service/evaluation.service';
import { IEvaluation, Evaluation } from '../evaluation.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';

import { EvaluationUpdateComponent } from './evaluation-update.component';

describe('Evaluation Management Update Component', () => {
  let comp: EvaluationUpdateComponent;
  let fixture: ComponentFixture<EvaluationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaluationService: EvaluationService;
  let matiereService: MatiereService;
  let groupeService: GroupeService;
  let professeurService: ProfesseurService;
  let salleService: SalleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvaluationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EvaluationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaluationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaluationService = TestBed.inject(EvaluationService);
    matiereService = TestBed.inject(MatiereService);
    groupeService = TestBed.inject(GroupeService);
    professeurService = TestBed.inject(ProfesseurService);
    salleService = TestBed.inject(SalleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Matiere query and add missing value', () => {
      const evaluation: IEvaluation = { id: 456 };
      const matiere: IMatiere = { id: 61051 };
      evaluation.matiere = matiere;

      const matiereCollection: IMatiere[] = [{ id: 15701 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [matiere];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Groupe query and add missing value', () => {
      const evaluation: IEvaluation = { id: 456 };
      const groupe: IGroupe = { id: 17760 };
      evaluation.groupe = groupe;

      const groupeCollection: IGroupe[] = [{ id: 9493 }];
      jest.spyOn(groupeService, 'query').mockReturnValue(of(new HttpResponse({ body: groupeCollection })));
      const additionalGroupes = [groupe];
      const expectedCollection: IGroupe[] = [...additionalGroupes, ...groupeCollection];
      jest.spyOn(groupeService, 'addGroupeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(groupeService.query).toHaveBeenCalled();
      expect(groupeService.addGroupeToCollectionIfMissing).toHaveBeenCalledWith(groupeCollection, ...additionalGroupes);
      expect(comp.groupesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Professeur query and add missing value', () => {
      const evaluation: IEvaluation = { id: 456 };
      const professeur: IProfesseur = { id: 16787 };
      evaluation.professeur = professeur;

      const professeurCollection: IProfesseur[] = [{ id: 61063 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [professeur];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Salle query and add missing value', () => {
      const evaluation: IEvaluation = { id: 456 };
      const salle: ISalle = { id: 52463 };
      evaluation.salle = salle;

      const salleCollection: ISalle[] = [{ id: 74101 }];
      jest.spyOn(salleService, 'query').mockReturnValue(of(new HttpResponse({ body: salleCollection })));
      const additionalSalles = [salle];
      const expectedCollection: ISalle[] = [...additionalSalles, ...salleCollection];
      jest.spyOn(salleService, 'addSalleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(salleService.query).toHaveBeenCalled();
      expect(salleService.addSalleToCollectionIfMissing).toHaveBeenCalledWith(salleCollection, ...additionalSalles);
      expect(comp.sallesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evaluation: IEvaluation = { id: 456 };
      const matiere: IMatiere = { id: 47268 };
      evaluation.matiere = matiere;
      const groupe: IGroupe = { id: 40667 };
      evaluation.groupe = groupe;
      const professeur: IProfesseur = { id: 32645 };
      evaluation.professeur = professeur;
      const salle: ISalle = { id: 74207 };
      evaluation.salle = salle;

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(evaluation));
      expect(comp.matieresSharedCollection).toContain(matiere);
      expect(comp.groupesSharedCollection).toContain(groupe);
      expect(comp.professeursSharedCollection).toContain(professeur);
      expect(comp.sallesSharedCollection).toContain(salle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evaluation>>();
      const evaluation = { id: 123 };
      jest.spyOn(evaluationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaluationService.update).toHaveBeenCalledWith(evaluation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evaluation>>();
      const evaluation = new Evaluation();
      jest.spyOn(evaluationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluation }));
      saveSubject.complete();

      // THEN
      expect(evaluationService.create).toHaveBeenCalledWith(evaluation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evaluation>>();
      const evaluation = { id: 123 };
      jest.spyOn(evaluationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaluationService.update).toHaveBeenCalledWith(evaluation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMatiereById', () => {
      it('Should return tracked Matiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatiereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackGroupeById', () => {
      it('Should return tracked Groupe primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGroupeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProfesseurById', () => {
      it('Should return tracked Professeur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProfesseurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSalleById', () => {
      it('Should return tracked Salle primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSalleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
