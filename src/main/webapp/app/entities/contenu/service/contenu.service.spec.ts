import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContenu, Contenu } from '../contenu.model';

import { ContenuService } from './contenu.service';

describe('Contenu Service', () => {
  let service: ContenuService;
  let httpMock: HttpTestingController;
  let elemDefault: IContenu;
  let expectedResult: IContenu | IContenu[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContenuService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomContenu: 'AAAAAAA',
      contenuContentType: 'image/png',
      contenu: 'AAAAAAA',
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

    it('should create a Contenu', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Contenu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contenu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomContenu: 'BBBBBB',
          contenu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contenu', () => {
      const patchObject = Object.assign(
        {
          nomContenu: 'BBBBBB',
          contenu: 'BBBBBB',
        },
        new Contenu()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contenu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomContenu: 'BBBBBB',
          contenu: 'BBBBBB',
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

    it('should delete a Contenu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContenuToCollectionIfMissing', () => {
      it('should add a Contenu to an empty array', () => {
        const contenu: IContenu = { id: 123 };
        expectedResult = service.addContenuToCollectionIfMissing([], contenu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contenu);
      });

      it('should not add a Contenu to an array that contains it', () => {
        const contenu: IContenu = { id: 123 };
        const contenuCollection: IContenu[] = [
          {
            ...contenu,
          },
          { id: 456 },
        ];
        expectedResult = service.addContenuToCollectionIfMissing(contenuCollection, contenu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contenu to an array that doesn't contain it", () => {
        const contenu: IContenu = { id: 123 };
        const contenuCollection: IContenu[] = [{ id: 456 }];
        expectedResult = service.addContenuToCollectionIfMissing(contenuCollection, contenu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contenu);
      });

      it('should add only unique Contenu to an array', () => {
        const contenuArray: IContenu[] = [{ id: 123 }, { id: 456 }, { id: 54548 }];
        const contenuCollection: IContenu[] = [{ id: 123 }];
        expectedResult = service.addContenuToCollectionIfMissing(contenuCollection, ...contenuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contenu: IContenu = { id: 123 };
        const contenu2: IContenu = { id: 456 };
        expectedResult = service.addContenuToCollectionIfMissing([], contenu, contenu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contenu);
        expect(expectedResult).toContain(contenu2);
      });

      it('should accept null and undefined values', () => {
        const contenu: IContenu = { id: 123 };
        expectedResult = service.addContenuToCollectionIfMissing([], null, contenu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contenu);
      });

      it('should return initial array if no Contenu is added', () => {
        const contenuCollection: IContenu[] = [{ id: 123 }];
        expectedResult = service.addContenuToCollectionIfMissing(contenuCollection, undefined, null);
        expect(expectedResult).toEqual(contenuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
