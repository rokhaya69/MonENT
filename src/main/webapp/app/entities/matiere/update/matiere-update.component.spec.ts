import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatiereService } from '../service/matiere.service';
import { IMatiere, Matiere } from '../matiere.model';
import { IProgramme } from 'app/entities/programme/programme.model';
import { ProgrammeService } from 'app/entities/programme/service/programme.service';

import { MatiereUpdateComponent } from './matiere-update.component';

describe('Matiere Management Update Component', () => {
  let comp: MatiereUpdateComponent;
  let fixture: ComponentFixture<MatiereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matiereService: MatiereService;
  let programmeService: ProgrammeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatiereUpdateComponent],
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
      .overrideTemplate(MatiereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatiereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matiereService = TestBed.inject(MatiereService);
    programmeService = TestBed.inject(ProgrammeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Programme query and add missing value', () => {
      const matiere: IMatiere = { id: 456 };
      const programme: IProgramme = { id: 62636 };
      matiere.programme = programme;

      const programmeCollection: IProgramme[] = [{ id: 20763 }];
      jest.spyOn(programmeService, 'query').mockReturnValue(of(new HttpResponse({ body: programmeCollection })));
      const additionalProgrammes = [programme];
      const expectedCollection: IProgramme[] = [...additionalProgrammes, ...programmeCollection];
      jest.spyOn(programmeService, 'addProgrammeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matiere });
      comp.ngOnInit();

      expect(programmeService.query).toHaveBeenCalled();
      expect(programmeService.addProgrammeToCollectionIfMissing).toHaveBeenCalledWith(programmeCollection, ...additionalProgrammes);
      expect(comp.programmesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const matiere: IMatiere = { id: 456 };
      const programme: IProgramme = { id: 92105 };
      matiere.programme = programme;

      activatedRoute.data = of({ matiere });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(matiere));
      expect(comp.programmesSharedCollection).toContain(programme);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matiere>>();
      const matiere = { id: 123 };
      jest.spyOn(matiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matiere }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(matiereService.update).toHaveBeenCalledWith(matiere);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matiere>>();
      const matiere = new Matiere();
      jest.spyOn(matiereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matiere }));
      saveSubject.complete();

      // THEN
      expect(matiereService.create).toHaveBeenCalledWith(matiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Matiere>>();
      const matiere = { id: 123 };
      jest.spyOn(matiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matiereService.update).toHaveBeenCalledWith(matiere);
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
  });
});
