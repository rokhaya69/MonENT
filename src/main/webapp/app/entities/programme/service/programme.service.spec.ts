import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProgramme, Programme } from '../programme.model';

import { ProgrammeService } from './programme.service';

describe('Programme Service', () => {
  let service: ProgrammeService;
  let httpMock: HttpTestingController;
  let elemDefault: IProgramme;
  let expectedResult: IProgramme | IProgramme[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProgrammeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomProgram: 'AAAAAAA',
      contenuProgramContentType: 'image/png',
      contenuProgram: 'AAAAAAA',
      annee: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Programme', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.create(new Programme()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Programme', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomProgram: 'BBBBBB',
          contenuProgram: 'BBBBBB',
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Programme', () => {
      const patchObject = Object.assign(
        {
          nomProgram: 'BBBBBB',
          annee: currentDate.format(DATE_FORMAT),
        },
        new Programme()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Programme', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomProgram: 'BBBBBB',
          contenuProgram: 'BBBBBB',
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Programme', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProgrammeToCollectionIfMissing', () => {
      it('should add a Programme to an empty array', () => {
        const programme: IProgramme = { id: 123 };
        expectedResult = service.addProgrammeToCollectionIfMissing([], programme);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(programme);
      });

      it('should not add a Programme to an array that contains it', () => {
        const programme: IProgramme = { id: 123 };
        const programmeCollection: IProgramme[] = [
          {
            ...programme,
          },
          { id: 456 },
        ];
        expectedResult = service.addProgrammeToCollectionIfMissing(programmeCollection, programme);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Programme to an array that doesn't contain it", () => {
        const programme: IProgramme = { id: 123 };
        const programmeCollection: IProgramme[] = [{ id: 456 }];
        expectedResult = service.addProgrammeToCollectionIfMissing(programmeCollection, programme);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(programme);
      });

      it('should add only unique Programme to an array', () => {
        const programmeArray: IProgramme[] = [{ id: 123 }, { id: 456 }, { id: 74846 }];
        const programmeCollection: IProgramme[] = [{ id: 123 }];
        expectedResult = service.addProgrammeToCollectionIfMissing(programmeCollection, ...programmeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const programme: IProgramme = { id: 123 };
        const programme2: IProgramme = { id: 456 };
        expectedResult = service.addProgrammeToCollectionIfMissing([], programme, programme2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(programme);
        expect(expectedResult).toContain(programme2);
      });

      it('should accept null and undefined values', () => {
        const programme: IProgramme = { id: 123 };
        expectedResult = service.addProgrammeToCollectionIfMissing([], null, programme, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(programme);
      });

      it('should return initial array if no Programme is added', () => {
        const programmeCollection: IProgramme[] = [{ id: 123 }];
        expectedResult = service.addProgrammeToCollectionIfMissing(programmeCollection, undefined, null);
        expect(expectedResult).toEqual(programmeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
