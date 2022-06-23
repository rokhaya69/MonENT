import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TypeRessource } from 'app/entities/enumerations/type-ressource.model';
import { IRessource, Ressource } from '../ressource.model';

import { RessourceService } from './ressource.service';

describe('Ressource Service', () => {
  let service: RessourceService;
  let httpMock: HttpTestingController;
  let elemDefault: IRessource;
  let expectedResult: IRessource | IRessource[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RessourceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      libelRessource: 'AAAAAAA',
      typeRessource: TypeRessource.SupportCours,
      lienRessourceContentType: 'image/png',
      lienRessource: 'AAAAAAA',
      dateMise: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateMise: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Ressource', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateMise: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMise: currentDate,
        },
        returnedFromService
      );

      service.create(new Ressource()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ressource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelRessource: 'BBBBBB',
          typeRessource: 'BBBBBB',
          lienRessource: 'BBBBBB',
          dateMise: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMise: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ressource', () => {
      const patchObject = Object.assign({}, new Ressource());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateMise: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ressource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelRessource: 'BBBBBB',
          typeRessource: 'BBBBBB',
          lienRessource: 'BBBBBB',
          dateMise: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMise: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Ressource', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRessourceToCollectionIfMissing', () => {
      it('should add a Ressource to an empty array', () => {
        const ressource: IRessource = { id: 123 };
        expectedResult = service.addRessourceToCollectionIfMissing([], ressource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ressource);
      });

      it('should not add a Ressource to an array that contains it', () => {
        const ressource: IRessource = { id: 123 };
        const ressourceCollection: IRessource[] = [
          {
            ...ressource,
          },
          { id: 456 },
        ];
        expectedResult = service.addRessourceToCollectionIfMissing(ressourceCollection, ressource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ressource to an array that doesn't contain it", () => {
        const ressource: IRessource = { id: 123 };
        const ressourceCollection: IRessource[] = [{ id: 456 }];
        expectedResult = service.addRessourceToCollectionIfMissing(ressourceCollection, ressource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ressource);
      });

      it('should add only unique Ressource to an array', () => {
        const ressourceArray: IRessource[] = [{ id: 123 }, { id: 456 }, { id: 54084 }];
        const ressourceCollection: IRessource[] = [{ id: 123 }];
        expectedResult = service.addRessourceToCollectionIfMissing(ressourceCollection, ...ressourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ressource: IRessource = { id: 123 };
        const ressource2: IRessource = { id: 456 };
        expectedResult = service.addRessourceToCollectionIfMissing([], ressource, ressource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ressource);
        expect(expectedResult).toContain(ressource2);
      });

      it('should accept null and undefined values', () => {
        const ressource: IRessource = { id: 123 };
        expectedResult = service.addRessourceToCollectionIfMissing([], null, ressource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ressource);
      });

      it('should return initial array if no Ressource is added', () => {
        const ressourceCollection: IRessource[] = [{ id: 123 }];
        expectedResult = service.addRessourceToCollectionIfMissing(ressourceCollection, undefined, null);
        expect(expectedResult).toEqual(ressourceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
