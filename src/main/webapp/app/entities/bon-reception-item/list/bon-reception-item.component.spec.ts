import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BonReceptionItemService } from '../service/bon-reception-item.service';

import { BonReceptionItemComponent } from './bon-reception-item.component';

describe('BonReceptionItem Management Component', () => {
  let comp: BonReceptionItemComponent;
  let fixture: ComponentFixture<BonReceptionItemComponent>;
  let service: BonReceptionItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'bon-reception-item', component: BonReceptionItemComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [BonReceptionItemComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(BonReceptionItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BonReceptionItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BonReceptionItemService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.bonReceptionItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to bonReceptionItemService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBonReceptionItemIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBonReceptionItemIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
