import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBonReceptionItem } from '../bon-reception-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bon-reception-item.test-samples';

import { BonReceptionItemService } from './bon-reception-item.service';

const requireRestSample: IBonReceptionItem = {
  ...sampleWithRequiredData,
};

describe('BonReceptionItem Service', () => {
  let service: BonReceptionItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IBonReceptionItem | IBonReceptionItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BonReceptionItemService);
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

    it('should create a BonReceptionItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const bonReceptionItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bonReceptionItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BonReceptionItem', () => {
      const bonReceptionItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bonReceptionItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BonReceptionItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BonReceptionItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BonReceptionItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBonReceptionItemToCollectionIfMissing', () => {
      it('should add a BonReceptionItem to an empty array', () => {
        const bonReceptionItem: IBonReceptionItem = sampleWithRequiredData;
        expectedResult = service.addBonReceptionItemToCollectionIfMissing([], bonReceptionItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bonReceptionItem);
      });

      it('should not add a BonReceptionItem to an array that contains it', () => {
        const bonReceptionItem: IBonReceptionItem = sampleWithRequiredData;
        const bonReceptionItemCollection: IBonReceptionItem[] = [
          {
            ...bonReceptionItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBonReceptionItemToCollectionIfMissing(bonReceptionItemCollection, bonReceptionItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BonReceptionItem to an array that doesn't contain it", () => {
        const bonReceptionItem: IBonReceptionItem = sampleWithRequiredData;
        const bonReceptionItemCollection: IBonReceptionItem[] = [sampleWithPartialData];
        expectedResult = service.addBonReceptionItemToCollectionIfMissing(bonReceptionItemCollection, bonReceptionItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bonReceptionItem);
      });

      it('should add only unique BonReceptionItem to an array', () => {
        const bonReceptionItemArray: IBonReceptionItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bonReceptionItemCollection: IBonReceptionItem[] = [sampleWithRequiredData];
        expectedResult = service.addBonReceptionItemToCollectionIfMissing(bonReceptionItemCollection, ...bonReceptionItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bonReceptionItem: IBonReceptionItem = sampleWithRequiredData;
        const bonReceptionItem2: IBonReceptionItem = sampleWithPartialData;
        expectedResult = service.addBonReceptionItemToCollectionIfMissing([], bonReceptionItem, bonReceptionItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bonReceptionItem);
        expect(expectedResult).toContain(bonReceptionItem2);
      });

      it('should accept null and undefined values', () => {
        const bonReceptionItem: IBonReceptionItem = sampleWithRequiredData;
        expectedResult = service.addBonReceptionItemToCollectionIfMissing([], null, bonReceptionItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bonReceptionItem);
      });

      it('should return initial array if no BonReceptionItem is added', () => {
        const bonReceptionItemCollection: IBonReceptionItem[] = [sampleWithRequiredData];
        expectedResult = service.addBonReceptionItemToCollectionIfMissing(bonReceptionItemCollection, undefined, null);
        expect(expectedResult).toEqual(bonReceptionItemCollection);
      });
    });

    describe('compareBonReceptionItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBonReceptionItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBonReceptionItem(entity1, entity2);
        const compareResult2 = service.compareBonReceptionItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBonReceptionItem(entity1, entity2);
        const compareResult2 = service.compareBonReceptionItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBonReceptionItem(entity1, entity2);
        const compareResult2 = service.compareBonReceptionItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
