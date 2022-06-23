import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NiveauEnseignement } from 'app/entities/enumerations/niveau-enseignement.model';
import { IProfesseur, Professeur } from '../professeur.model';

import { ProfesseurService } from './professeur.service';

describe('Professeur Service', () => {
  let service: ProfesseurService;
  let httpMock: HttpTestingController;
  let elemDefault: IProfesseur;
  let expectedResult: IProfesseur | IProfesseur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProfesseurService);
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
      specialite: 'AAAAAAA',
      niveauEnseign: NiveauEnseignement.LyceeTech,
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

    it('should create a Professeur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Professeur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Professeur', () => {
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
          specialite: 'BBBBBB',
          niveauEnseign: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Professeur', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          sexe: 'BBBBBB',
          specialite: 'BBBBBB',
        },
        new Professeur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Professeur', () => {
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
          specialite: 'BBBBBB',
          niveauEnseign: 'BBBBBB',
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

    it('should delete a Professeur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProfesseurToCollectionIfMissing', () => {
      it('should add a Professeur to an empty array', () => {
        const professeur: IProfesseur = { id: 123 };
        expectedResult = service.addProfesseurToCollectionIfMissing([], professeur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(professeur);
      });

      it('should not add a Professeur to an array that contains it', () => {
        const professeur: IProfesseur = { id: 123 };
        const professeurCollection: IProfesseur[] = [
          {
            ...professeur,
          },
          { id: 456 },
        ];
        expectedResult = service.addProfesseurToCollectionIfMissing(professeurCollection, professeur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Professeur to an array that doesn't contain it", () => {
        const professeur: IProfesseur = { id: 123 };
        const professeurCollection: IProfesseur[] = [{ id: 456 }];
        expectedResult = service.addProfesseurToCollectionIfMissing(professeurCollection, professeur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(professeur);
      });

      it('should add only unique Professeur to an array', () => {
        const professeurArray: IProfesseur[] = [{ id: 123 }, { id: 456 }, { id: 71339 }];
        const professeurCollection: IProfesseur[] = [{ id: 123 }];
        expectedResult = service.addProfesseurToCollectionIfMissing(professeurCollection, ...professeurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const professeur: IProfesseur = { id: 123 };
        const professeur2: IProfesseur = { id: 456 };
        expectedResult = service.addProfesseurToCollectionIfMissing([], professeur, professeur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(professeur);
        expect(expectedResult).toContain(professeur2);
      });

      it('should accept null and undefined values', () => {
        const professeur: IProfesseur = { id: 123 };
        expectedResult = service.addProfesseurToCollectionIfMissing([], null, professeur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(professeur);
      });

      it('should return initial array if no Professeur is added', () => {
        const professeurCollection: IProfesseur[] = [{ id: 123 }];
        expectedResult = service.addProfesseurToCollectionIfMissing(professeurCollection, undefined, null);
        expect(expectedResult).toEqual(professeurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
