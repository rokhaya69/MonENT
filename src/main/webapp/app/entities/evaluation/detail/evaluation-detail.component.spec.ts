import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EvaluationDetailComponent } from './evaluation-detail.component';

describe('Evaluation Management Detail Component', () => {
  let comp: EvaluationDetailComponent;
  let fixture: ComponentFixture<EvaluationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvaluationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ evaluation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EvaluationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaluationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evaluation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.evaluation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
