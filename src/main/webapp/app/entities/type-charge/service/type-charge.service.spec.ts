import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeCharge } from '../type-charge.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-charge.test-samples';

import { TypeChargeService } from './type-charge.service';

const requireRestSample: ITypeCharge = {
  ...sampleWithRequiredData,
};

describe('TypeCharge Service', () => {
  let service: TypeChargeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeCharge | ITypeCharge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeChargeService);
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

    it('should create a TypeCharge', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const typeCharge = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeCharge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeCharge', () => {
      const typeCharge = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeCharge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeCharge', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeCharge', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeCharge', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeChargeToCollectionIfMissing', () => {
      it('should add a TypeCharge to an empty array', () => {
        const typeCharge: ITypeCharge = sampleWithRequiredData;
        expectedResult = service.addTypeChargeToCollectionIfMissing([], typeCharge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeCharge);
      });

      it('should not add a TypeCharge to an array that contains it', () => {
        const typeCharge: ITypeCharge = sampleWithRequiredData;
        const typeChargeCollection: ITypeCharge[] = [
          {
            ...typeCharge,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeChargeToCollectionIfMissing(typeChargeCollection, typeCharge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeCharge to an array that doesn't contain it", () => {
        const typeCharge: ITypeCharge = sampleWithRequiredData;
        const typeChargeCollection: ITypeCharge[] = [sampleWithPartialData];
        expectedResult = service.addTypeChargeToCollectionIfMissing(typeChargeCollection, typeCharge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeCharge);
      });

      it('should add only unique TypeCharge to an array', () => {
        const typeChargeArray: ITypeCharge[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeChargeCollection: ITypeCharge[] = [sampleWithRequiredData];
        expectedResult = service.addTypeChargeToCollectionIfMissing(typeChargeCollection, ...typeChargeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeCharge: ITypeCharge = sampleWithRequiredData;
        const typeCharge2: ITypeCharge = sampleWithPartialData;
        expectedResult = service.addTypeChargeToCollectionIfMissing([], typeCharge, typeCharge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeCharge);
        expect(expectedResult).toContain(typeCharge2);
      });

      it('should accept null and undefined values', () => {
        const typeCharge: ITypeCharge = sampleWithRequiredData;
        expectedResult = service.addTypeChargeToCollectionIfMissing([], null, typeCharge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeCharge);
      });

      it('should return initial array if no TypeCharge is added', () => {
        const typeChargeCollection: ITypeCharge[] = [sampleWithRequiredData];
        expectedResult = service.addTypeChargeToCollectionIfMissing(typeChargeCollection, undefined, null);
        expect(expectedResult).toEqual(typeChargeCollection);
      });
    });

    describe('compareTypeCharge', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeCharge(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeCharge(entity1, entity2);
        const compareResult2 = service.compareTypeCharge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeCharge(entity1, entity2);
        const compareResult2 = service.compareTypeCharge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeCharge(entity1, entity2);
        const compareResult2 = service.compareTypeCharge(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
