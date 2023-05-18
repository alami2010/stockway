import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChargeDetailComponent } from './charge-detail.component';

describe('Charge Management Detail Component', () => {
  let comp: ChargeDetailComponent;
  let fixture: ComponentFixture<ChargeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChargeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ charge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChargeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChargeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load charge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.charge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
