import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { IParent, Parent } from '../parent.model';

import { ParentService } from './parent.service';

describe('Parent Service', () => {
  let service: ParentService;
  let httpMock: HttpTestingController;
  let elemDefault: IParent;
  let expectedResult: IParent | IParent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParentService);
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

    it('should create a Parent', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Parent()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Parent', () => {
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

    it('should partial update a Parent', () => {
      const patchObject = Object.assign(
        {
          prenom: 'BBBBBB',
          email: 'BBBBBB',
        },
        new Parent()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Parent', () => {
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

    it('should delete a Parent', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addParentToCollectionIfMissing', () => {
      it('should add a Parent to an empty array', () => {
        const parent: IParent = { id: 123 };
        expectedResult = service.addParentToCollectionIfMissing([], parent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parent);
      });

      it('should not add a Parent to an array that contains it', () => {
        const parent: IParent = { id: 123 };
        const parentCollection: IParent[] = [
          {
            ...parent,
          },
          { id: 456 },
        ];
        expectedResult = service.addParentToCollectionIfMissing(parentCollection, parent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Parent to an array that doesn't contain it", () => {
        const parent: IParent = { id: 123 };
        const parentCollection: IParent[] = [{ id: 456 }];
        expectedResult = service.addParentToCollectionIfMissing(parentCollection, parent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parent);
      });

      it('should add only unique Parent to an array', () => {
        const parentArray: IParent[] = [{ id: 123 }, { id: 456 }, { id: 89271 }];
        const parentCollection: IParent[] = [{ id: 123 }];
        expectedResult = service.addParentToCollectionIfMissing(parentCollection, ...parentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parent: IParent = { id: 123 };
        const parent2: IParent = { id: 456 };
        expectedResult = service.addParentToCollectionIfMissing([], parent, parent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parent);
        expect(expectedResult).toContain(parent2);
      });

      it('should accept null and undefined values', () => {
        const parent: IParent = { id: 123 };
        expectedResult = service.addParentToCollectionIfMissing([], null, parent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parent);
      });

      it('should return initial array if no Parent is added', () => {
        const parentCollection: IParent[] = [{ id: 123 }];
        expectedResult = service.addParentToCollectionIfMissing(parentCollection, undefined, null);
        expect(expectedResult).toEqual(parentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
