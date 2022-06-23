import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAbsence, Absence } from '../absence.model';

import { AbsenceService } from './absence.service';

describe('Absence Service', () => {
  let service: AbsenceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAbsence;
  let expectedResult: IAbsence | IAbsence[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AbsenceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      motif: 'AAAAAAA',
      justifie: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Absence', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Absence()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Absence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          motif: 'BBBBBB',
          justifie: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Absence', () => {
      const patchObject = Object.assign({}, new Absence());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Absence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          motif: 'BBBBBB',
          justifie: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Absence', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAbsenceToCollectionIfMissing', () => {
      it('should add a Absence to an empty array', () => {
        const absence: IAbsence = { id: 123 };
        expectedResult = service.addAbsenceToCollectionIfMissing([], absence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(absence);
      });

      it('should not add a Absence to an array that contains it', () => {
        const absence: IAbsence = { id: 123 };
        const absenceCollection: IAbsence[] = [
          {
            ...absence,
          },
          { id: 456 },
        ];
        expectedResult = service.addAbsenceToCollectionIfMissing(absenceCollection, absence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Absence to an array that doesn't contain it", () => {
        const absence: IAbsence = { id: 123 };
        const absenceCollection: IAbsence[] = [{ id: 456 }];
        expectedResult = service.addAbsenceToCollectionIfMissing(absenceCollection, absence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(absence);
      });

      it('should add only unique Absence to an array', () => {
        const absenceArray: IAbsence[] = [{ id: 123 }, { id: 456 }, { id: 15777 }];
        const absenceCollection: IAbsence[] = [{ id: 123 }];
        expectedResult = service.addAbsenceToCollectionIfMissing(absenceCollection, ...absenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const absence: IAbsence = { id: 123 };
        const absence2: IAbsence = { id: 456 };
        expectedResult = service.addAbsenceToCollectionIfMissing([], absence, absence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(absence);
        expect(expectedResult).toContain(absence2);
      });

      it('should accept null and undefined values', () => {
        const absence: IAbsence = { id: 123 };
        expectedResult = service.addAbsenceToCollectionIfMissing([], null, absence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(absence);
      });

      it('should return initial array if no Absence is added', () => {
        const absenceCollection: IAbsence[] = [{ id: 123 }];
        expectedResult = service.addAbsenceToCollectionIfMissing(absenceCollection, undefined, null);
        expect(expectedResult).toEqual(absenceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
