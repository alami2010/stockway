import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BonReceptionItemDetailComponent } from './bon-reception-item-detail.component';

describe('BonReceptionItem Management Detail Component', () => {
  let comp: BonReceptionItemDetailComponent;
  let fixture: ComponentFixture<BonReceptionItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BonReceptionItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bonReceptionItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BonReceptionItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BonReceptionItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bonReceptionItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bonReceptionItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
