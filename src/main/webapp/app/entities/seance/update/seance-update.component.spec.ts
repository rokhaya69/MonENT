import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SeanceService } from '../service/seance.service';
import { ISeance, Seance } from '../seance.model';
import { IContenu } from 'app/entities/contenu/contenu.model';
import { ContenuService } from 'app/entities/contenu/service/contenu.service';
import { ICours } from 'app/entities/cours/cours.model';
import { CoursService } from 'app/entities/cours/service/cours.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';

import { SeanceUpdateComponent } from './seance-update.component';

describe('Seance Management Update Component', () => {
  let comp: SeanceUpdateComponent;
  let fixture: ComponentFixture<SeanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let seanceService: SeanceService;
  let contenuService: ContenuService;
  let coursService: CoursService;
  let salleService: SalleService;
  let groupeService: GroupeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SeanceUpdateComponent],
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
      .overrideTemplate(SeanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SeanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    seanceService = TestBed.inject(SeanceService);
    contenuService = TestBed.inject(ContenuService);
    coursService = TestBed.inject(CoursService);
    salleService = TestBed.inject(SalleService);
    groupeService = TestBed.inject(GroupeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call contenu query and add missing value', () => {
      const seance: ISeance = { id: 456 };
      const contenu: IContenu = { id: 33989 };
      seance.contenu = contenu;

      const contenuCollection: IContenu[] = [{ id: 65832 }];
      jest.spyOn(contenuService, 'query').mockReturnValue(of(new HttpResponse({ body: contenuCollection })));
      const expectedCollection: IContenu[] = [contenu, ...contenuCollection];
      jest.spyOn(contenuService, 'addContenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      expect(contenuService.query).toHaveBeenCalled();
      expect(contenuService.addContenuToCollectionIfMissing).toHaveBeenCalledWith(contenuCollection, contenu);
      expect(comp.contenusCollection).toEqual(expectedCollection);
    });

    it('Should call Cours query and add missing value', () => {
      const seance: ISeance = { id: 456 };
      const cours: ICours = { id: 67600 };
      seance.cours = cours;

      const coursCollection: ICours[] = [{ id: 5873 }];
      jest.spyOn(coursService, 'query').mockReturnValue(of(new HttpResponse({ body: coursCollection })));
      const additionalCours = [cours];
      const expectedCollection: ICours[] = [...additionalCours, ...coursCollection];
      jest.spyOn(coursService, 'addCoursToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      expect(coursService.query).toHaveBeenCalled();
      expect(coursService.addCoursToCollectionIfMissing).toHaveBeenCalledWith(coursCollection, ...additionalCours);
      expect(comp.coursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Salle query and add missing value', () => {
      const seance: ISeance = { id: 456 };
      const salle: ISalle = { id: 94178 };
      seance.salle = salle;

      const salleCollection: ISalle[] = [{ id: 6670 }];
      jest.spyOn(salleService, 'query').mockReturnValue(of(new HttpResponse({ body: salleCollection })));
      const additionalSalles = [salle];
      const expectedCollection: ISalle[] = [...additionalSalles, ...salleCollection];
      jest.spyOn(salleService, 'addSalleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      expect(salleService.query).toHaveBeenCalled();
      expect(salleService.addSalleToCollectionIfMissing).toHaveBeenCalledWith(salleCollection, ...additionalSalles);
      expect(comp.sallesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Groupe query and add missing value', () => {
      const seance: ISeance = { id: 456 };
      const groupe: IGroupe = { id: 76109 };
      seance.groupe = groupe;

      const groupeCollection: IGroupe[] = [{ id: 14464 }];
      jest.spyOn(groupeService, 'query').mockReturnValue(of(new HttpResponse({ body: groupeCollection })));
      const additionalGroupes = [groupe];
      const expectedCollection: IGroupe[] = [...additionalGroupes, ...groupeCollection];
      jest.spyOn(groupeService, 'addGroupeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      expect(groupeService.query).toHaveBeenCalled();
      expect(groupeService.addGroupeToCollectionIfMissing).toHaveBeenCalledWith(groupeCollection, ...additionalGroupes);
      expect(comp.groupesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const seance: ISeance = { id: 456 };
      const contenu: IContenu = { id: 68441 };
      seance.contenu = contenu;
      const cours: ICours = { id: 20857 };
      seance.cours = cours;
      const salle: ISalle = { id: 28122 };
      seance.salle = salle;
      const groupe: IGroupe = { id: 86905 };
      seance.groupe = groupe;

      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(seance));
      expect(comp.contenusCollection).toContain(contenu);
      expect(comp.coursSharedCollection).toContain(cours);
      expect(comp.sallesSharedCollection).toContain(salle);
      expect(comp.groupesSharedCollection).toContain(groupe);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Seance>>();
      const seance = { id: 123 };
      jest.spyOn(seanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seance }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(seanceService.update).toHaveBeenCalledWith(seance);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Seance>>();
      const seance = new Seance();
      jest.spyOn(seanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seance }));
      saveSubject.complete();

      // THEN
      expect(seanceService.create).toHaveBeenCalledWith(seance);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Seance>>();
      const seance = { id: 123 };
      jest.spyOn(seanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(seanceService.update).toHaveBeenCalledWith(seance);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContenuById', () => {
      it('Should return tracked Contenu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContenuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCoursById', () => {
      it('Should return tracked Cours primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCoursById(0, entity);
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

    describe('trackGroupeById', () => {
      it('Should return tracked Groupe primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGroupeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
