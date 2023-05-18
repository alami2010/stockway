import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BonReceptionDetailComponent } from './bon-reception-detail.component';

describe('BonReception Management Detail Component', () => {
  let comp: BonReceptionDetailComponent;
  let fixture: ComponentFixture<BonReceptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BonReceptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bonReception: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BonReceptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BonReceptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bonReception on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bonReception).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
