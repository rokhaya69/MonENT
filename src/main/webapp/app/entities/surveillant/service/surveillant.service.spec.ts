import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { ISurveillant, Surveillant } from '../surveillant.model';

import { SurveillantService } from './surveillant.service';

describe('Surveillant Service', () => {
  let service: SurveillantService;
  let httpMock: HttpTestingController;
  let elemDefault: ISurveillant;
  let expectedResult: ISurveillant | ISurveillant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SurveillantService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      prenom: 'AAAAAAA',
      email: 'AAAAAAA',
      adresse: 'AAAAAAA',
      telephone: 'AAAAAAA',
      sexe: Sexe.Masculin,
      photoContentType: 'image/png',
      photo: 'AAAAAAA',
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

    it('should create a Surveillant', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Surveillant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Surveillant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          adresse: 'BBBBBB',
          telephone: 'BBBBBB',
          sexe: 'BBBBBB',
          photo: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Surveillant', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          email: 'BBBBBB',
          adresse: 'BBBBBB',
          telephone: 'BBBBBB',
        },
        new Surveillant()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Surveillant', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          adresse: 'BBBBBB',
          telephone: 'BBBBBB',
          sexe: 'BBBBBB',
          photo: 'BBBBBB',
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

    it('should delete a Surveillant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSurveillantToCollectionIfMissing', () => {
      it('should add a Surveillant to an empty array', () => {
        const surveillant: ISurveillant = { id: 123 };
        expectedResult = service.addSurveillantToCollectionIfMissing([], surveillant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surveillant);
      });

      it('should not add a Surveillant to an array that contains it', () => {
        const surveillant: ISurveillant = { id: 123 };
        const surveillantCollection: ISurveillant[] = [
          {
            ...surveillant,
          },
          { id: 456 },
        ];
        expectedResult = service.addSurveillantToCollectionIfMissing(surveillantCollection, surveillant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Surveillant to an array that doesn't contain it", () => {
        const surveillant: ISurveillant = { id: 123 };
        const surveillantCollection: ISurveillant[] = [{ id: 456 }];
        expectedResult = service.addSurveillantToCollectionIfMissing(surveillantCollection, surveillant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surveillant);
      });

      it('should add only unique Surveillant to an array', () => {
        const surveillantArray: ISurveillant[] = [{ id: 123 }, { id: 456 }, { id: 7775 }];
        const surveillantCollection: ISurveillant[] = [{ id: 123 }];
        expectedResult = service.addSurveillantToCollectionIfMissing(surveillantCollection, ...surveillantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const surveillant: ISurveillant = { id: 123 };
        const surveillant2: ISurveillant = { id: 456 };
        expectedResult = service.addSurveillantToCollectionIfMissing([], surveillant, surveillant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surveillant);
        expect(expectedResult).toContain(surveillant2);
      });

      it('should accept null and undefined values', () => {
        const surveillant: ISurveillant = { id: 123 };
        expectedResult = service.addSurveillantToCollectionIfMissing([], null, surveillant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surveillant);
      });

      it('should return initial array if no Surveillant is added', () => {
        const surveillantCollection: ISurveillant[] = [{ id: 123 }];
        expectedResult = service.addSurveillantToCollectionIfMissing(surveillantCollection, undefined, null);
        expect(expectedResult).toEqual(surveillantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
