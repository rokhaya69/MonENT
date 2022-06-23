import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SyllabusService } from '../service/syllabus.service';
import { ISyllabus, Syllabus } from '../syllabus.model';
import { IProgramme } from 'app/entities/programme/programme.model';
import { ProgrammeService } from 'app/entities/programme/service/programme.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { SyllabusUpdateComponent } from './syllabus-update.component';

describe('Syllabus Management Update Component', () => {
  let comp: SyllabusUpdateComponent;
  let fixture: ComponentFixture<SyllabusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let syllabusService: SyllabusService;
  let programmeService: ProgrammeService;
  let matiereService: MatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SyllabusUpdateComponent],
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
      .overrideTemplate(SyllabusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SyllabusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    syllabusService = TestBed.inject(SyllabusService);
    programmeService = TestBed.inject(ProgrammeService);
    matiereService = TestBed.inject(MatiereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Programme query and add missing value', () => {
      const syllabus: ISyllabus = { id: 456 };
      const programme: IProgramme = { id: 9977 };
      syllabus.programme = programme;

      const programmeCollection: IProgramme[] = [{ id: 81234 }];
      jest.spyOn(programmeService, 'query').mockReturnValue(of(new HttpResponse({ body: programmeCollection })));
      const additionalProgrammes = [programme];
      const expectedCollection: IProgramme[] = [...additionalProgrammes, ...programmeCollection];
      jest.spyOn(programmeService, 'addProgrammeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ syllabus });
      comp.ngOnInit();

      expect(programmeService.query).toHaveBeenCalled();
      expect(programmeService.addProgrammeToCollectionIfMissing).toHaveBeenCalledWith(programmeCollection, ...additionalProgrammes);
      expect(comp.programmesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Matiere query and add missing value', () => {
      const syllabus: ISyllabus = { id: 456 };
      const matiere: IMatiere = { id: 87300 };
      syllabus.matiere = matiere;

      const matiereCollection: IMatiere[] = [{ id: 29162 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [matiere];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ syllabus });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const syllabus: ISyllabus = { id: 456 };
      const programme: IProgramme = { id: 64998 };
      syllabus.programme = programme;
      const matiere: IMatiere = { id: 3433 };
      syllabus.matiere = matiere;

      activatedRoute.data = of({ syllabus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(syllabus));
      expect(comp.programmesSharedCollection).toContain(programme);
      expect(comp.matieresSharedCollection).toContain(matiere);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Syllabus>>();
      const syllabus = { id: 123 };
      jest.spyOn(syllabusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ syllabus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: syllabus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(syllabusService.update).toHaveBeenCalledWith(syllabus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Syllabus>>();
      const syllabus = new Syllabus();
      jest.spyOn(syllabusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ syllabus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: syllabus }));
      saveSubject.complete();

      // THEN
      expect(syllabusService.create).toHaveBeenCalledWith(syllabus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Syllabus>>();
      const syllabus = { id: 123 };
      jest.spyOn(syllabusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ syllabus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(syllabusService.update).toHaveBeenCalledWith(syllabus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProgrammeById', () => {
      it('Should return tracked Programme primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProgrammeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMatiereById', () => {
      it('Should return tracked Matiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatiereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
