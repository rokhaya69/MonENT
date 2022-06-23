import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RessourceService } from '../service/ressource.service';
import { IRessource, Ressource } from '../ressource.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';
import { ICours } from 'app/entities/cours/cours.model';
import { CoursService } from 'app/entities/cours/service/cours.service';
import { ISurveillant } from 'app/entities/surveillant/surveillant.model';
import { SurveillantService } from 'app/entities/surveillant/service/surveillant.service';
import { IProviseur } from 'app/entities/proviseur/proviseur.model';
import { ProviseurService } from 'app/entities/proviseur/service/proviseur.service';
import { IDirecteur } from 'app/entities/directeur/directeur.model';
import { DirecteurService } from 'app/entities/directeur/service/directeur.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';

import { RessourceUpdateComponent } from './ressource-update.component';

describe('Ressource Management Update Component', () => {
  let comp: RessourceUpdateComponent;
  let fixture: ComponentFixture<RessourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ressourceService: RessourceService;
  let apprenantService: ApprenantService;
  let groupeService: GroupeService;
  let coursService: CoursService;
  let surveillantService: SurveillantService;
  let proviseurService: ProviseurService;
  let directeurService: DirecteurService;
  let inspecteurService: InspecteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RessourceUpdateComponent],
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
      .overrideTemplate(RessourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RessourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ressourceService = TestBed.inject(RessourceService);
    apprenantService = TestBed.inject(ApprenantService);
    groupeService = TestBed.inject(GroupeService);
    coursService = TestBed.inject(CoursService);
    surveillantService = TestBed.inject(SurveillantService);
    proviseurService = TestBed.inject(ProviseurService);
    directeurService = TestBed.inject(DirecteurService);
    inspecteurService = TestBed.inject(InspecteurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Apprenant query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const apprenant: IApprenant = { id: 38370 };
      ressource.apprenant = apprenant;

      const apprenantCollection: IApprenant[] = [{ id: 65520 }];
      jest.spyOn(apprenantService, 'query').mockReturnValue(of(new HttpResponse({ body: apprenantCollection })));
      const additionalApprenants = [apprenant];
      const expectedCollection: IApprenant[] = [...additionalApprenants, ...apprenantCollection];
      jest.spyOn(apprenantService, 'addApprenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(apprenantService.query).toHaveBeenCalled();
      expect(apprenantService.addApprenantToCollectionIfMissing).toHaveBeenCalledWith(apprenantCollection, ...additionalApprenants);
      expect(comp.apprenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Groupe query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const groupe: IGroupe = { id: 62246 };
      ressource.groupe = groupe;

      const groupeCollection: IGroupe[] = [{ id: 50754 }];
      jest.spyOn(groupeService, 'query').mockReturnValue(of(new HttpResponse({ body: groupeCollection })));
      const additionalGroupes = [groupe];
      const expectedCollection: IGroupe[] = [...additionalGroupes, ...groupeCollection];
      jest.spyOn(groupeService, 'addGroupeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(groupeService.query).toHaveBeenCalled();
      expect(groupeService.addGroupeToCollectionIfMissing).toHaveBeenCalledWith(groupeCollection, ...additionalGroupes);
      expect(comp.groupesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cours query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const cours: ICours = { id: 18497 };
      ressource.cours = cours;

      const coursCollection: ICours[] = [{ id: 53759 }];
      jest.spyOn(coursService, 'query').mockReturnValue(of(new HttpResponse({ body: coursCollection })));
      const additionalCours = [cours];
      const expectedCollection: ICours[] = [...additionalCours, ...coursCollection];
      jest.spyOn(coursService, 'addCoursToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(coursService.query).toHaveBeenCalled();
      expect(coursService.addCoursToCollectionIfMissing).toHaveBeenCalledWith(coursCollection, ...additionalCours);
      expect(comp.coursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Surveillant query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const persoAdmin: ISurveillant = { id: 23379 };
      ressource.persoAdmin = persoAdmin;

      const surveillantCollection: ISurveillant[] = [{ id: 13013 }];
      jest.spyOn(surveillantService, 'query').mockReturnValue(of(new HttpResponse({ body: surveillantCollection })));
      const additionalSurveillants = [persoAdmin];
      const expectedCollection: ISurveillant[] = [...additionalSurveillants, ...surveillantCollection];
      jest.spyOn(surveillantService, 'addSurveillantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(surveillantService.query).toHaveBeenCalled();
      expect(surveillantService.addSurveillantToCollectionIfMissing).toHaveBeenCalledWith(surveillantCollection, ...additionalSurveillants);
      expect(comp.surveillantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Proviseur query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const proviseur: IProviseur = { id: 47839 };
      ressource.proviseur = proviseur;

      const proviseurCollection: IProviseur[] = [{ id: 96521 }];
      jest.spyOn(proviseurService, 'query').mockReturnValue(of(new HttpResponse({ body: proviseurCollection })));
      const additionalProviseurs = [proviseur];
      const expectedCollection: IProviseur[] = [...additionalProviseurs, ...proviseurCollection];
      jest.spyOn(proviseurService, 'addProviseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(proviseurService.query).toHaveBeenCalled();
      expect(proviseurService.addProviseurToCollectionIfMissing).toHaveBeenCalledWith(proviseurCollection, ...additionalProviseurs);
      expect(comp.proviseursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Directeur query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const directeur: IDirecteur = { id: 17798 };
      ressource.directeur = directeur;

      const directeurCollection: IDirecteur[] = [{ id: 12569 }];
      jest.spyOn(directeurService, 'query').mockReturnValue(of(new HttpResponse({ body: directeurCollection })));
      const additionalDirecteurs = [directeur];
      const expectedCollection: IDirecteur[] = [...additionalDirecteurs, ...directeurCollection];
      jest.spyOn(directeurService, 'addDirecteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(directeurService.query).toHaveBeenCalled();
      expect(directeurService.addDirecteurToCollectionIfMissing).toHaveBeenCalledWith(directeurCollection, ...additionalDirecteurs);
      expect(comp.directeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inspecteur query and add missing value', () => {
      const ressource: IRessource = { id: 456 };
      const inspecteur: IInspecteur = { id: 60899 };
      ressource.inspecteur = inspecteur;

      const inspecteurCollection: IInspecteur[] = [{ id: 96022 }];
      jest.spyOn(inspecteurService, 'query').mockReturnValue(of(new HttpResponse({ body: inspecteurCollection })));
      const additionalInspecteurs = [inspecteur];
      const expectedCollection: IInspecteur[] = [...additionalInspecteurs, ...inspecteurCollection];
      jest.spyOn(inspecteurService, 'addInspecteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(inspecteurService.query).toHaveBeenCalled();
      expect(inspecteurService.addInspecteurToCollectionIfMissing).toHaveBeenCalledWith(inspecteurCollection, ...additionalInspecteurs);
      expect(comp.inspecteursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ressource: IRessource = { id: 456 };
      const apprenant: IApprenant = { id: 33463 };
      ressource.apprenant = apprenant;
      const groupe: IGroupe = { id: 22309 };
      ressource.groupe = groupe;
      const cours: ICours = { id: 47043 };
      ressource.cours = cours;
      const persoAdmin: ISurveillant = { id: 84969 };
      ressource.persoAdmin = persoAdmin;
      const proviseur: IProviseur = { id: 21988 };
      ressource.proviseur = proviseur;
      const directeur: IDirecteur = { id: 30559 };
      ressource.directeur = directeur;
      const inspecteur: IInspecteur = { id: 82617 };
      ressource.inspecteur = inspecteur;

      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ressource));
      expect(comp.apprenantsSharedCollection).toContain(apprenant);
      expect(comp.groupesSharedCollection).toContain(groupe);
      expect(comp.coursSharedCollection).toContain(cours);
      expect(comp.surveillantsSharedCollection).toContain(persoAdmin);
      expect(comp.proviseursSharedCollection).toContain(proviseur);
      expect(comp.directeursSharedCollection).toContain(directeur);
      expect(comp.inspecteursSharedCollection).toContain(inspecteur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ressource>>();
      const ressource = { id: 123 };
      jest.spyOn(ressourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ressource }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ressourceService.update).toHaveBeenCalledWith(ressource);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ressource>>();
      const ressource = new Ressource();
      jest.spyOn(ressourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ressource }));
      saveSubject.complete();

      // THEN
      expect(ressourceService.create).toHaveBeenCalledWith(ressource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ressource>>();
      const ressource = { id: 123 };
      jest.spyOn(ressourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ressource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ressourceService.update).toHaveBeenCalledWith(ressource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApprenantById', () => {
      it('Should return tracked Apprenant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApprenantById(0, entity);
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

    describe('trackCoursById', () => {
      it('Should return tracked Cours primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCoursById(0, entity);
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

    describe('trackInspecteurById', () => {
      it('Should return tracked Inspecteur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInspecteurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
