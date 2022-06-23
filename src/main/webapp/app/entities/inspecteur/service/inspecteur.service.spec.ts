import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { IInspecteur, Inspecteur } from '../inspecteur.model';

import { InspecteurService } from './inspecteur.service';

describe('Inspecteur Service', () => {
  let service: InspecteurService;
  let httpMock: HttpTestingController;
  let elemDefault: IInspecteur;
  let expectedResult: IInspecteur | IInspecteur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InspecteurService);
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

    it('should create a Inspecteur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Inspecteur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inspecteur', () => {
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

    it('should partial update a Inspecteur', () => {
      const patchObject = Object.assign(
        {
          email: 'BBBBBB',
          telephone: 'BBBBBB',
        },
        new Inspecteur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inspecteur', () => {
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

    it('should delete a Inspecteur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInspecteurToCollectionIfMissing', () => {
      it('should add a Inspecteur to an empty array', () => {
        const inspecteur: IInspecteur = { id: 123 };
        expectedResult = service.addInspecteurToCollectionIfMissing([], inspecteur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspecteur);
      });

      it('should not add a Inspecteur to an array that contains it', () => {
        const inspecteur: IInspecteur = { id: 123 };
        const inspecteurCollection: IInspecteur[] = [
          {
            ...inspecteur,
          },
          { id: 456 },
        ];
        expectedResult = service.addInspecteurToCollectionIfMissing(inspecteurCollection, inspecteur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inspecteur to an array that doesn't contain it", () => {
        const inspecteur: IInspecteur = { id: 123 };
        const inspecteurCollection: IInspecteur[] = [{ id: 456 }];
        expectedResult = service.addInspecteurToCollectionIfMissing(inspecteurCollection, inspecteur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspecteur);
      });

      it('should add only unique Inspecteur to an array', () => {
        const inspecteurArray: IInspecteur[] = [{ id: 123 }, { id: 456 }, { id: 69549 }];
        const inspecteurCollection: IInspecteur[] = [{ id: 123 }];
        expectedResult = service.addInspecteurToCollectionIfMissing(inspecteurCollection, ...inspecteurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inspecteur: IInspecteur = { id: 123 };
        const inspecteur2: IInspecteur = { id: 456 };
        expectedResult = service.addInspecteurToCollectionIfMissing([], inspecteur, inspecteur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspecteur);
        expect(expectedResult).toContain(inspecteur2);
      });

      it('should accept null and undefined values', () => {
        const inspecteur: IInspecteur = { id: 123 };
        expectedResult = service.addInspecteurToCollectionIfMissing([], null, inspecteur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspecteur);
      });

      it('should return initial array if no Inspecteur is added', () => {
        const inspecteurCollection: IInspecteur[] = [{ id: 123 }];
        expectedResult = service.addInspecteurToCollectionIfMissing(inspecteurCollection, undefined, null);
        expect(expectedResult).toEqual(inspecteurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
