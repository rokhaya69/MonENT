import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { IMefpai, Mefpai } from '../mefpai.model';

import { MefpaiService } from './mefpai.service';

describe('Mefpai Service', () => {
  let service: MefpaiService;
  let httpMock: HttpTestingController;
  let elemDefault: IMefpai;
  let expectedResult: IMefpai | IMefpai[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MefpaiService);
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

    it('should create a Mefpai', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Mefpai()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mefpai', () => {
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

    it('should partial update a Mefpai', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          adresse: 'BBBBBB',
          telephone: 'BBBBBB',
          sexe: 'BBBBBB',
        },
        new Mefpai()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mefpai', () => {
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

    it('should delete a Mefpai', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMefpaiToCollectionIfMissing', () => {
      it('should add a Mefpai to an empty array', () => {
        const mefpai: IMefpai = { id: 123 };
        expectedResult = service.addMefpaiToCollectionIfMissing([], mefpai);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mefpai);
      });

      it('should not add a Mefpai to an array that contains it', () => {
        const mefpai: IMefpai = { id: 123 };
        const mefpaiCollection: IMefpai[] = [
          {
            ...mefpai,
          },
          { id: 456 },
        ];
        expectedResult = service.addMefpaiToCollectionIfMissing(mefpaiCollection, mefpai);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mefpai to an array that doesn't contain it", () => {
        const mefpai: IMefpai = { id: 123 };
        const mefpaiCollection: IMefpai[] = [{ id: 456 }];
        expectedResult = service.addMefpaiToCollectionIfMissing(mefpaiCollection, mefpai);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mefpai);
      });

      it('should add only unique Mefpai to an array', () => {
        const mefpaiArray: IMefpai[] = [{ id: 123 }, { id: 456 }, { id: 80547 }];
        const mefpaiCollection: IMefpai[] = [{ id: 123 }];
        expectedResult = service.addMefpaiToCollectionIfMissing(mefpaiCollection, ...mefpaiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mefpai: IMefpai = { id: 123 };
        const mefpai2: IMefpai = { id: 456 };
        expectedResult = service.addMefpaiToCollectionIfMissing([], mefpai, mefpai2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mefpai);
        expect(expectedResult).toContain(mefpai2);
      });

      it('should accept null and undefined values', () => {
        const mefpai: IMefpai = { id: 123 };
        expectedResult = service.addMefpaiToCollectionIfMissing([], null, mefpai, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mefpai);
      });

      it('should return initial array if no Mefpai is added', () => {
        const mefpaiCollection: IMefpai[] = [{ id: 123 }];
        expectedResult = service.addMefpaiToCollectionIfMissing(mefpaiCollection, undefined, null);
        expect(expectedResult).toEqual(mefpaiCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
