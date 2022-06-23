import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SeanceDetailComponent } from './seance-detail.component';

describe('Seance Management Detail Component', () => {
  let comp: SeanceDetailComponent;
  let fixture: ComponentFixture<SeanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SeanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ seance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SeanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SeanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load seance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.seance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
