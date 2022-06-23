import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CoursService } from '../service/cours.service';
import { ICours, Cours } from '../cours.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IClasse } from 'app/entities/classe/classe.model';
import { ClasseService } from 'app/entities/classe/service/classe.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { ISyllabus } from 'app/entities/syllabus/syllabus.model';
import { SyllabusService } from 'app/entities/syllabus/service/syllabus.service';

import { CoursUpdateComponent } from './cours-update.component';

describe('Cours Management Update Component', () => {
  let comp: CoursUpdateComponent;
  let fixture: ComponentFixture<CoursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coursService: CoursService;
  let matiereService: MatiereService;
  let classeService: ClasseService;
  let professeurService: ProfesseurService;
  let syllabusService: SyllabusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CoursUpdateComponent],
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
      .overrideTemplate(CoursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coursService = TestBed.inject(CoursService);
    matiereService = TestBed.inject(MatiereService);
    classeService = TestBed.inject(ClasseService);
    professeurService = TestBed.inject(ProfesseurService);
    syllabusService = TestBed.inject(SyllabusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Matiere query and add missing value', () => {
      const cours: ICours = { id: 456 };
      const matiere: IMatiere = { id: 90081 };
      cours.matiere = matiere;

      const matiereCollection: IMatiere[] = [{ id: 76279 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [matiere];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Classe query and add missing value', () => {
      const cours: ICours = { id: 456 };
      const classe: IClasse = { id: 9354 };
      cours.classe = classe;

      const classeCollection: IClasse[] = [{ id: 8641 }];
      jest.spyOn(classeService, 'query').mockReturnValue(of(new HttpResponse({ body: classeCollection })));
      const additionalClasses = [classe];
      const expectedCollection: IClasse[] = [...additionalClasses, ...classeCollection];
      jest.spyOn(classeService, 'addClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      expect(classeService.query).toHaveBeenCalled();
      expect(classeService.addClasseToCollectionIfMissing).toHaveBeenCalledWith(classeCollection, ...additionalClasses);
      expect(comp.classesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Professeur query and add missing value', () => {
      const cours: ICours = { id: 456 };
      const professeur: IProfesseur = { id: 8869 };
      cours.professeur = professeur;

      const professeurCollection: IProfesseur[] = [{ id: 1188 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [professeur];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Syllabus query and add missing value', () => {
      const cours: ICours = { id: 456 };
      const syllabus: ISyllabus = { id: 67536 };
      cours.syllabus = syllabus;

      const syllabusCollection: ISyllabus[] = [{ id: 4680 }];
      jest.spyOn(syllabusService, 'query').mockReturnValue(of(new HttpResponse({ body: syllabusCollection })));
      const additionalSyllabi = [syllabus];
      const expectedCollection: ISyllabus[] = [...additionalSyllabi, ...syllabusCollection];
      jest.spyOn(syllabusService, 'addSyllabusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      expect(syllabusService.query).toHaveBeenCalled();
      expect(syllabusService.addSyllabusToCollectionIfMissing).toHaveBeenCalledWith(syllabusCollection, ...additionalSyllabi);
      expect(comp.syllabiSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cours: ICours = { id: 456 };
      const matiere: IMatiere = { id: 25455 };
      cours.matiere = matiere;
      const classe: IClasse = { id: 43868 };
      cours.classe = classe;
      const professeur: IProfesseur = { id: 61146 };
      cours.professeur = professeur;
      const syllabus: ISyllabus = { id: 7745 };
      cours.syllabus = syllabus;

      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cours));
      expect(comp.matieresSharedCollection).toContain(matiere);
      expect(comp.classesSharedCollection).toContain(classe);
      expect(comp.professeursSharedCollection).toContain(professeur);
      expect(comp.syllabiSharedCollection).toContain(syllabus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cours>>();
      const cours = { id: 123 };
      jest.spyOn(coursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cours }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(coursService.update).toHaveBeenCalledWith(cours);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cours>>();
      const cours = new Cours();
      jest.spyOn(coursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cours }));
      saveSubject.complete();

      // THEN
      expect(coursService.create).toHaveBeenCalledWith(cours);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cours>>();
      const cours = { id: 123 };
      jest.spyOn(coursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coursService.update).toHaveBeenCalledWith(cours);
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

    describe('trackClasseById', () => {
      it('Should return tracked Classe primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClasseById(0, entity);
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

    describe('trackSyllabusById', () => {
      it('Should return tracked Syllabus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSyllabusById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
