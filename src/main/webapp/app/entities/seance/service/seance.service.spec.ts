import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Jour } from 'app/entities/enumerations/jour.model';
import { ISeance, Seance } from '../seance.model';

import { SeanceService } from './seance.service';

describe('Seance Service', () => {
  let service: SeanceService;
  let httpMock: HttpTestingController;
  let elemDefault: ISeance;
  let expectedResult: ISeance | ISeance[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SeanceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      jourSeance: Jour.Lundi,
      dateSeance: currentDate,
      dateDebut: currentDate,
      dateFin: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateSeance: currentDate.format(DATE_FORMAT),
          dateDebut: currentDate.format(DATE_TIME_FORMAT),
          dateFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Seance', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateSeance: currentDate.format(DATE_FORMAT),
          dateDebut: currentDate.format(DATE_TIME_FORMAT),
          dateFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateSeance: currentDate,
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.create(new Seance()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Seance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          jourSeance: 'BBBBBB',
          dateSeance: currentDate.format(DATE_FORMAT),
          dateDebut: currentDate.format(DATE_TIME_FORMAT),
          dateFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateSeance: currentDate,
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Seance', () => {
      const patchObject = Object.assign(
        {
          jourSeance: 'BBBBBB',
        },
        new Seance()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateSeance: currentDate,
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Seance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          jourSeance: 'BBBBBB',
          dateSeance: currentDate.format(DATE_FORMAT),
          dateDebut: currentDate.format(DATE_TIME_FORMAT),
          dateFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateSeance: currentDate,
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Seance', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSeanceToCollectionIfMissing', () => {
      it('should add a Seance to an empty array', () => {
        const seance: ISeance = { id: 123 };
        expectedResult = service.addSeanceToCollectionIfMissing([], seance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seance);
      });

      it('should not add a Seance to an array that contains it', () => {
        const seance: ISeance = { id: 123 };
        const seanceCollection: ISeance[] = [
          {
            ...seance,
          },
          { id: 456 },
        ];
        expectedResult = service.addSeanceToCollectionIfMissing(seanceCollection, seance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Seance to an array that doesn't contain it", () => {
        const seance: ISeance = { id: 123 };
        const seanceCollection: ISeance[] = [{ id: 456 }];
        expectedResult = service.addSeanceToCollectionIfMissing(seanceCollection, seance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seance);
      });

      it('should add only unique Seance to an array', () => {
        const seanceArray: ISeance[] = [{ id: 123 }, { id: 456 }, { id: 78504 }];
        const seanceCollection: ISeance[] = [{ id: 123 }];
        expectedResult = service.addSeanceToCollectionIfMissing(seanceCollection, ...seanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const seance: ISeance = { id: 123 };
        const seance2: ISeance = { id: 456 };
        expectedResult = service.addSeanceToCollectionIfMissing([], seance, seance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seance);
        expect(expectedResult).toContain(seance2);
      });

      it('should accept null and undefined values', () => {
        const seance: ISeance = { id: 123 };
        expectedResult = service.addSeanceToCollectionIfMissing([], null, seance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seance);
      });

      it('should return initial array if no Seance is added', () => {
        const seanceCollection: ISeance[] = [{ id: 123 }];
        expectedResult = service.addSeanceToCollectionIfMissing(seanceCollection, undefined, null);
        expect(expectedResult).toEqual(seanceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
