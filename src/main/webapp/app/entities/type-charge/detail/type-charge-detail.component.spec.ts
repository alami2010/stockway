import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeChargeDetailComponent } from './type-charge-detail.component';

describe('TypeCharge Management Detail Component', () => {
  let comp: TypeChargeDetailComponent;
  let fixture: ComponentFixture<TypeChargeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypeChargeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typeCharge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypeChargeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeChargeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typeCharge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typeCharge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
