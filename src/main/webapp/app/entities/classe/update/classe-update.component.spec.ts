import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClasseService } from '../service/classe.service';
import { IClasse, Classe } from '../classe.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { ISerie } from 'app/entities/serie/serie.model';
import { SerieService } from 'app/entities/serie/service/serie.service';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { ProviseurService } from 'app/entities/proviseur/service/proviseur.service';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { DirecteurService } from 'app/entities/directeur/service/directeur.service';
import { ISurveillant } from 'app/entities/surveillant/surveillant.model';
import { SurveillantService } from 'app/entities/surveillant/service/surveillant.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';

import { ClasseUpdateComponent } from './classe-update.component';

describe('Classe Management Update Component', () => {
  let comp: ClasseUpdateComponent;
  let fixture: ComponentFixture<ClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classeService: ClasseService;
  let filiereService: FiliereService;
  let serieService: SerieService;
  let proviseurService: ProviseurService;
  let directeurService: DirecteurService;
  let surveillantService: SurveillantService;
  let professeurService: ProfesseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClasseUpdateComponent],
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
      .overrideTemplate(ClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classeService = TestBed.inject(ClasseService);
    filiereService = TestBed.inject(FiliereService);
    serieService = TestBed.inject(SerieService);
    proviseurService = TestBed.inject(ProviseurService);
    directeurService = TestBed.inject(DirecteurService);
    surveillantService = TestBed.inject(SurveillantService);
    professeurService = TestBed.inject(ProfesseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Filiere query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const filiere: IFiliere = { id: 56677 };
      classe.filiere = filiere;

      const filiereCollection: IFiliere[] = [{ id: 70749 }];
      jest.spyOn(filiereService, 'query').mockReturnValue(of(new HttpResponse({ body: filiereCollection })));
      const additionalFilieres = [filiere];
      const expectedCollection: IFiliere[] = [...additionalFilieres, ...filiereCollection];
      jest.spyOn(filiereService, 'addFiliereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(filiereService.query).toHaveBeenCalled();
      expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, ...additionalFilieres);
      expect(comp.filieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Serie query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const serie: ISerie = { id: 51802 };
      classe.serie = serie;

      const serieCollection: ISerie[] = [{ id: 63393 }];
      jest.spyOn(serieService, 'query').mockReturnValue(of(new HttpResponse({ body: serieCollection })));
      const additionalSeries = [serie];
      const expectedCollection: ISerie[] = [...additionalSeries, ...serieCollection];
      jest.spyOn(serieService, 'addSerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(serieService.query).toHaveBeenCalled();
      expect(serieService.addSerieToCollectionIfMissing).toHaveBeenCalledWith(serieCollection, ...additionalSeries);
      expect(comp.seriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Proviseur query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const proviseur: IProviseur = { id: 96655 };
      classe.proviseur = proviseur;

      const proviseurCollection: IProviseur[] = [{ id: 12856 }];
      jest.spyOn(proviseurService, 'query').mockReturnValue(of(new HttpResponse({ body: proviseurCollection })));
      const additionalProviseurs = [proviseur];
      const expectedCollection: IProviseur[] = [...additionalProviseurs, ...proviseurCollection];
      jest.spyOn(proviseurService, 'addProviseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(proviseurService.query).toHaveBeenCalled();
      expect(proviseurService.addProviseurToCollectionIfMissing).toHaveBeenCalledWith(proviseurCollection, ...additionalProviseurs);
      expect(comp.proviseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Directeur query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const directeur: IDirecteur = { id: 23991 };
      classe.directeur = directeur;

      const directeurCollection: IDirecteur[] = [{ id: 23807 }];
      jest.spyOn(directeurService, 'query').mockReturnValue(of(new HttpResponse({ body: directeurCollection })));
      const additionalDirecteurs = [directeur];
      const expectedCollection: IDirecteur[] = [...additionalDirecteurs, ...directeurCollection];
      jest.spyOn(directeurService, 'addDirecteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(directeurService.query).toHaveBeenCalled();
      expect(directeurService.addDirecteurToCollectionIfMissing).toHaveBeenCalledWith(directeurCollection, ...additionalDirecteurs);
      expect(comp.directeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Surveillant query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const surveillant: ISurveillant = { id: 67431 };
      classe.surveillant = surveillant;

      const surveillantCollection: ISurveillant[] = [{ id: 2668 }];
      jest.spyOn(surveillantService, 'query').mockReturnValue(of(new HttpResponse({ body: surveillantCollection })));
      const additionalSurveillants = [surveillant];
      const expectedCollection: ISurveillant[] = [...additionalSurveillants, ...surveillantCollection];
      jest.spyOn(surveillantService, 'addSurveillantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(surveillantService.query).toHaveBeenCalled();
      expect(surveillantService.addSurveillantToCollectionIfMissing).toHaveBeenCalledWith(surveillantCollection, ...additionalSurveillants);
      expect(comp.surveillantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Professeur query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const professeur: IProfesseur = { id: 3507 };
      classe.professeur = professeur;

      const professeurCollection: IProfesseur[] = [{ id: 83505 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [professeur];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classe: IClasse = { id: 456 };
      const filiere: IFiliere = { id: 12065 };
      classe.filiere = filiere;
      const serie: ISerie = { id: 96265 };
      classe.serie = serie;
      const proviseur: IProviseur = { id: 84910 };
      classe.proviseur = proviseur;
      const directeur: IDirecteur = { id: 34737 };
      classe.directeur = directeur;
      const surveillant: ISurveillant = { id: 96350 };
      classe.surveillant = surveillant;
      const professeur: IProfesseur = { id: 55317 };
      classe.professeur = professeur;

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classe));
      expect(comp.filieresSharedCollection).toContain(filiere);
      expect(comp.seriesSharedCollection).toContain(serie);
      expect(comp.proviseursSharedCollection).toContain(proviseur);
      expect(comp.directeursSharedCollection).toContain(directeur);
      expect(comp.surveillantsSharedCollection).toContain(surveillant);
      expect(comp.professeursSharedCollection).toContain(professeur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = { id: 123 };
      jest.spyOn(classeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classe }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classeService.update).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = new Classe();
      jest.spyOn(classeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classe }));
      saveSubject.complete();

      // THEN
      expect(classeService.create).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = { id: 123 };
      jest.spyOn(classeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classeService.update).toHaveBeenCalledWith(classe);
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

    describe('trackProviseurById', () => {
      it('Should return tracked Proviseur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProviseurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDirecteurById', () => {
      it('Should return tracked Directeur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDirecteurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSurveillantById', () => {
      it('Should return tracked Surveillant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSurveillantById(0, entity);
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
  });
});
