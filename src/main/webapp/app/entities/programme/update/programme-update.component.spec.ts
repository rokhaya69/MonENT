import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProgrammeService } from '../service/programme.service';
import { IProgramme, Programme } from '../programme.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { ISerie } from 'app/entities/serie/serie.model';
import { SerieService } from 'app/entities/serie/service/serie.service';

import { ProgrammeUpdateComponent } from './programme-update.component';

describe('Programme Management Update Component', () => {
  let comp: ProgrammeUpdateComponent;
  let fixture: ComponentFixture<ProgrammeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let programmeService: ProgrammeService;
  let filiereService: FiliereService;
  let serieService: SerieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProgrammeUpdateComponent],
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
      .overrideTemplate(ProgrammeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProgrammeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    programmeService = TestBed.inject(ProgrammeService);
    filiereService = TestBed.inject(FiliereService);
    serieService = TestBed.inject(SerieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call filiere query and add missing value', () => {
      const programme: IProgramme = { id: 456 };
      const filiere: IFiliere = { id: 22772 };
      programme.filiere = filiere;

      const filiereCollection: IFiliere[] = [{ id: 5788 }];
      jest.spyOn(filiereService, 'query').mockReturnValue(of(new HttpResponse({ body: filiereCollection })));
      const expectedCollection: IFiliere[] = [filiere, ...filiereCollection];
      jest.spyOn(filiereService, 'addFiliereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ programme });
      comp.ngOnInit();

      expect(filiereService.query).toHaveBeenCalled();
      expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, filiere);
      expect(comp.filieresCollection).toEqual(expectedCollection);
    });

    it('Should call serie query and add missing value', () => {
      const programme: IProgramme = { id: 456 };
      const serie: ISerie = { id: 17117 };
      programme.serie = serie;

      const serieCollection: ISerie[] = [{ id: 37638 }];
      jest.spyOn(serieService, 'query').mockReturnValue(of(new HttpResponse({ body: serieCollection })));
      const expectedCollection: ISerie[] = [serie, ...serieCollection];
      jest.spyOn(serieService, 'addSerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ programme });
      comp.ngOnInit();

      expect(serieService.query).toHaveBeenCalled();
      expect(serieService.addSerieToCollectionIfMissing).toHaveBeenCalledWith(serieCollection, serie);
      expect(comp.seriesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const programme: IProgramme = { id: 456 };
      const filiere: IFiliere = { id: 80481 };
      programme.filiere = filiere;
      const serie: ISerie = { id: 82877 };
      programme.serie = serie;

      activatedRoute.data = of({ programme });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(programme));
      expect(comp.filieresCollection).toContain(filiere);
      expect(comp.seriesCollection).toContain(serie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Programme>>();
      const programme = { id: 123 };
      jest.spyOn(programmeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: programme }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(programmeService.update).toHaveBeenCalledWith(programme);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Programme>>();
      const programme = new Programme();
      jest.spyOn(programmeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: programme }));
      saveSubject.complete();

      // THEN
      expect(programmeService.create).toHaveBeenCalledWith(programme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Programme>>();
      const programme = { id: 123 };
      jest.spyOn(programmeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ programme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(programmeService.update).toHaveBeenCalledWith(programme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFiliereById', () => {
      it('Should return tracked Filiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiliereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSerieById', () => {
      it('Should return tracked Serie primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSerieById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
