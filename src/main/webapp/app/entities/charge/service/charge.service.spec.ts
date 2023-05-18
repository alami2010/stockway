import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICharge } from '../charge.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../charge.test-samples';

import { ChargeService, RestCharge } from './charge.service';

const requireRestSample: RestCharge = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.format(DATE_FORMAT),
};

describe('Charge Service', () => {
  let service: ChargeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICharge | ICharge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChargeService);
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

    it('should create a Charge', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const charge = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(charge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Charge', () => {
      const charge = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(charge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Charge', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Charge', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Charge', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChargeToCollectionIfMissing', () => {
      it('should add a Charge to an empty array', () => {
        const charge: ICharge = sampleWithRequiredData;
        expectedResult = service.addChargeToCollectionIfMissing([], charge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(charge);
      });

      it('should not add a Charge to an array that contains it', () => {
        const charge: ICharge = sampleWithRequiredData;
        const chargeCollection: ICharge[] = [
          {
            ...charge,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChargeToCollectionIfMissing(chargeCollection, charge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Charge to an array that doesn't contain it", () => {
        const charge: ICharge = sampleWithRequiredData;
        const chargeCollection: ICharge[] = [sampleWithPartialData];
        expectedResult = service.addChargeToCollectionIfMissing(chargeCollection, charge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(charge);
      });

      it('should add only unique Charge to an array', () => {
        const chargeArray: ICharge[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const chargeCollection: ICharge[] = [sampleWithRequiredData];
        expectedResult = service.addChargeToCollectionIfMissing(chargeCollection, ...chargeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const charge: ICharge = sampleWithRequiredData;
        const charge2: ICharge = sampleWithPartialData;
        expectedResult = service.addChargeToCollectionIfMissing([], charge, charge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(charge);
        expect(expectedResult).toContain(charge2);
      });

      it('should accept null and undefined values', () => {
        const charge: ICharge = sampleWithRequiredData;
        expectedResult = service.addChargeToCollectionIfMissing([], null, charge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(charge);
      });

      it('should return initial array if no Charge is added', () => {
        const chargeCollection: ICharge[] = [sampleWithRequiredData];
        expectedResult = service.addChargeToCollectionIfMissing(chargeCollection, undefined, null);
        expect(expectedResult).toEqual(chargeCollection);
      });
    });

    describe('compareCharge', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCharge(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCharge(entity1, entity2);
        const compareResult2 = service.compareCharge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCharge(entity1, entity2);
        const compareResult2 = service.compareCharge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCharge(entity1, entity2);
        const compareResult2 = service.compareCharge(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
