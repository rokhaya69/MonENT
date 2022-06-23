import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContenuService } from '../service/contenu.service';
import { IContenu, Contenu } from '../contenu.model';

import { ContenuUpdateComponent } from './contenu-update.component';

describe('Contenu Management Update Component', () => {
  let comp: ContenuUpdateComponent;
  let fixture: ComponentFixture<ContenuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contenuService: ContenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContenuUpdateComponent],
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
      .overrideTemplate(ContenuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContenuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contenuService = TestBed.inject(ContenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contenu: IContenu = { id: 456 };

      activatedRoute.data = of({ contenu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contenu));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contenu>>();
      const contenu = { id: 123 };
      jest.spyOn(contenuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contenu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contenu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contenuService.update).toHaveBeenCalledWith(contenu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contenu>>();
      const contenu = new Contenu();
      jest.spyOn(contenuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contenu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contenu }));
      saveSubject.complete();

      // THEN
      expect(contenuService.create).toHaveBeenCalledWith(contenu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contenu>>();
      const contenu = { id: 123 };
      jest.spyOn(contenuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contenu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contenuService.update).toHaveBeenCalledWith(contenu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
