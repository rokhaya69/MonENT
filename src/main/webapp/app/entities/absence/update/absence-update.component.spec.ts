import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AbsenceService } from '../service/absence.service';
import { IAbsence, Absence } from '../absence.model';
import { ISeance } from 'app/entities/seance/seance.model';
import { SeanceService } from 'app/entities/seance/service/seance.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';
import { EvaluationService } from 'app/entities/evaluation/service/evaluation.service';

import { AbsenceUpdateComponent } from './absence-update.component';

describe('Absence Management Update Component', () => {
  let comp: AbsenceUpdateComponent;
  let fixture: ComponentFixture<AbsenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let absenceService: AbsenceService;
  let seanceService: SeanceService;
  let apprenantService: ApprenantService;
  let evaluationService: EvaluationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AbsenceUpdateComponent],
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
      .overrideTemplate(AbsenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AbsenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    absenceService = TestBed.inject(AbsenceService);
    seanceService = TestBed.inject(SeanceService);
    apprenantService = TestBed.inject(ApprenantService);
    evaluationService = TestBed.inject(EvaluationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Seance query and add missing value', () => {
      const absence: IAbsence = { id: 456 };
      const seance: ISeance = { id: 33094 };
      absence.seance = seance;

      const seanceCollection: ISeance[] = [{ id: 77501 }];
      jest.spyOn(seanceService, 'query').mockReturnValue(of(new HttpResponse({ body: seanceCollection })));
      const additionalSeances = [seance];
      const expectedCollection: ISeance[] = [...additionalSeances, ...seanceCollection];
      jest.spyOn(seanceService, 'addSeanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      expect(seanceService.query).toHaveBeenCalled();
      expect(seanceService.addSeanceToCollectionIfMissing).toHaveBeenCalledWith(seanceCollection, ...additionalSeances);
      expect(comp.seancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Apprenant query and add missing value', () => {
      const absence: IAbsence = { id: 456 };
      const apprenant: IApprenant = { id: 4995 };
      absence.apprenant = apprenant;

      const apprenantCollection: IApprenant[] = [{ id: 26665 }];
      jest.spyOn(apprenantService, 'query').mockReturnValue(of(new HttpResponse({ body: apprenantCollection })));
      const additionalApprenants = [apprenant];
      const expectedCollection: IApprenant[] = [...additionalApprenants, ...apprenantCollection];
      jest.spyOn(apprenantService, 'addApprenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      expect(apprenantService.query).toHaveBeenCalled();
      expect(apprenantService.addApprenantToCollectionIfMissing).toHaveBeenCalledWith(apprenantCollection, ...additionalApprenants);
      expect(comp.apprenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Evaluation query and add missing value', () => {
      const absence: IAbsence = { id: 456 };
      const evaluation: IEvaluation = { id: 9959 };
      absence.evaluation = evaluation;

      const evaluationCollection: IEvaluation[] = [{ id: 88783 }];
      jest.spyOn(evaluationService, 'query').mockReturnValue(of(new HttpResponse({ body: evaluationCollection })));
      const additionalEvaluations = [evaluation];
      const expectedCollection: IEvaluation[] = [...additionalEvaluations, ...evaluationCollection];
      jest.spyOn(evaluationService, 'addEvaluationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      expect(evaluationService.query).toHaveBeenCalled();
      expect(evaluationService.addEvaluationToCollectionIfMissing).toHaveBeenCalledWith(evaluationCollection, ...additionalEvaluations);
      expect(comp.evaluationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const absence: IAbsence = { id: 456 };
      const seance: ISeance = { id: 81084 };
      absence.seance = seance;
      const apprenant: IApprenant = { id: 78777 };
      absence.apprenant = apprenant;
      const evaluation: IEvaluation = { id: 94367 };
      absence.evaluation = evaluation;

      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(absence));
      expect(comp.seancesSharedCollection).toContain(seance);
      expect(comp.apprenantsSharedCollection).toContain(apprenant);
      expect(comp.evaluationsSharedCollection).toContain(evaluation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Absence>>();
      const absence = { id: 123 };
      jest.spyOn(absenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: absence }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(absenceService.update).toHaveBeenCalledWith(absence);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Absence>>();
      const absence = new Absence();
      jest.spyOn(absenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: absence }));
      saveSubject.complete();

      // THEN
      expect(absenceService.create).toHaveBeenCalledWith(absence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Absence>>();
      const absence = { id: 123 };
      jest.spyOn(absenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(absenceService.update).toHaveBeenCalledWith(absence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSeanceById', () => {
      it('Should return tracked Seance primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSeanceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApprenantById', () => {
      it('Should return tracked Apprenant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApprenantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEvaluationById', () => {
      it('Should return tracked Evaluation primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEvaluationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
