import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBonReception } from '../bon-reception.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bon-reception.test-samples';

import { BonReceptionService, RestBonReception } from './bon-reception.service';

const requireRestSample: RestBonReception = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.format(DATE_FORMAT),
};

describe('BonReception Service', () => {
  let service: BonReceptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IBonReception | IBonReception[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BonReceptionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BonReception', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const bonReception = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bonReception).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BonReception', () => {
      const bonReception = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bonReception).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BonReception', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BonReception', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BonReception', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBonReceptionToCollectionIfMissing', () => {
      it('should add a BonReception to an empty array', () => {
        const bonReception: IBonReception = sampleWithRequiredData;
        expectedResult = service.addBonReceptionToCollectionIfMissing([], bonReception);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bonReception);
      });

      it('should not add a BonReception to an array that contains it', () => {
        const bonReception: IBonReception = sampleWithRequiredData;
        const bonReceptionCollection: IBonReception[] = [
          {
            ...bonReception,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBonReceptionToCollectionIfMissing(bonReceptionCollection, bonReception);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BonReception to an array that doesn't contain it", () => {
        const bonReception: IBonReception = sampleWithRequiredData;
        const bonReceptionCollection: IBonReception[] = [sampleWithPartialData];
        expectedResult = service.addBonReceptionToCollectionIfMissing(bonReceptionCollection, bonReception);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bonReception);
      });

      it('should add only unique BonReception to an array', () => {
        const bonReceptionArray: IBonReception[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bonReceptionCollection: IBonReception[] = [sampleWithRequiredData];
        expectedResult = service.addBonReceptionToCollectionIfMissing(bonReceptionCollection, ...bonReceptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bonReception: IBonReception = sampleWithRequiredData;
        const bonReception2: IBonReception = sampleWithPartialData;
        expectedResult = service.addBonReceptionToCollectionIfMissing([], bonReception, bonReception2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bonReception);
        expect(expectedResult).toContain(bonReception2);
      });

      it('should accept null and undefined values', () => {
        const bonReception: IBonReception = sampleWithRequiredData;
        expectedResult = service.addBonReceptionToCollectionIfMissing([], null, bonReception, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bonReception);
      });

      it('should return initial array if no BonReception is added', () => {
        const bonReceptionCollection: IBonReception[] = [sampleWithRequiredData];
        expectedResult = service.addBonReceptionToCollectionIfMissing(bonReceptionCollection, undefined, null);
        expect(expectedResult).toEqual(bonReceptionCollection);
      });
    });

    describe('compareBonReception', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBonReception(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBonReception(entity1, entity2);
        const compareResult2 = service.compareBonReception(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBonReception(entity1, entity2);
        const compareResult2 = service.compareBonReception(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBonReception(entity1, entity2);
        const compareResult2 = service.compareBonReception(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
