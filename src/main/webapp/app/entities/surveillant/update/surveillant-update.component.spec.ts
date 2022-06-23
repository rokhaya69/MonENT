import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SurveillantService } from '../service/surveillant.service';
import { ISurveillant, Surveillant } from '../surveillant.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEvaluation } from 'app/entities/evaluation/evaluation.model';
import { EvaluationService } from 'app/entities/evaluation/service/evaluation.service';

import { SurveillantUpdateComponent } from './surveillant-update.component';

describe('Surveillant Management Update Component', () => {
  let comp: SurveillantUpdateComponent;
  let fixture: ComponentFixture<SurveillantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let surveillantService: SurveillantService;
  let userService: UserService;
  let evaluationService: EvaluationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SurveillantUpdateComponent],
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
      .overrideTemplate(SurveillantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SurveillantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    surveillantService = TestBed.inject(SurveillantService);
    userService = TestBed.inject(UserService);
    evaluationService = TestBed.inject(EvaluationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const surveillant: ISurveillant = { id: 456 };
      const user: IUser = { id: 19999 };
      surveillant.user = user;

      const userCollection: IUser[] = [{ id: 3520 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ surveillant });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Evaluation query and add missing value', () => {
      const surveillant: ISurveillant = { id: 456 };
      const evaluations: IEvaluation[] = [{ id: 95289 }];
      surveillant.evaluations = evaluations;

      const evaluationCollection: IEvaluation[] = [{ id: 74835 }];
      jest.spyOn(evaluationService, 'query').mockReturnValue(of(new HttpResponse({ body: evaluationCollection })));
      const additionalEvaluations = [...evaluations];
      const expectedCollection: IEvaluation[] = [...additionalEvaluations, ...evaluationCollection];
      jest.spyOn(evaluationService, 'addEvaluationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ surveillant });
      comp.ngOnInit();

      expect(evaluationService.query).toHaveBeenCalled();
      expect(evaluationService.addEvaluationToCollectionIfMissing).toHaveBeenCalledWith(evaluationCollection, ...additionalEvaluations);
      expect(comp.evaluationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const surveillant: ISurveillant = { id: 456 };
      const user: IUser = { id: 98289 };
      surveillant.user = user;
      const evaluations: IEvaluation = { id: 77230 };
      surveillant.evaluations = [evaluations];

      activatedRoute.data = of({ surveillant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(surveillant));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.evaluationsSharedCollection).toContain(evaluations);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Surveillant>>();
      const surveillant = { id: 123 };
      jest.spyOn(surveillantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surveillant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surveillant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(surveillantService.update).toHaveBeenCalledWith(surveillant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Surveillant>>();
      const surveillant = new Surveillant();
      jest.spyOn(surveillantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surveillant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surveillant }));
      saveSubject.complete();

      // THEN
      expect(surveillantService.create).toHaveBeenCalledWith(surveillant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Surveillant>>();
      const surveillant = { id: 123 };
      jest.spyOn(surveillantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surveillant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(surveillantService.update).toHaveBeenCalledWith(surveillant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
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

  describe('Getting selected relationships', () => {
    describe('getSelectedEvaluation', () => {
      it('Should return option if no Evaluation is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedEvaluation(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Evaluation for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedEvaluation(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Evaluation is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedEvaluation(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
