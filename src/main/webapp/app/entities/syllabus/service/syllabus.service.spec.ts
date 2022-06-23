import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISyllabus, Syllabus } from '../syllabus.model';

import { SyllabusService } from './syllabus.service';

describe('Syllabus Service', () => {
  let service: SyllabusService;
  let httpMock: HttpTestingController;
  let elemDefault: ISyllabus;
  let expectedResult: ISyllabus | ISyllabus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SyllabusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomSyllabus: 'AAAAAAA',
      syllabusContentType: 'image/png',
      syllabus: 'AAAAAAA',
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

    it('should create a Syllabus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Syllabus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Syllabus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomSyllabus: 'BBBBBB',
          syllabus: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Syllabus', () => {
      const patchObject = Object.assign(
        {
          nomSyllabus: 'BBBBBB',
          syllabus: 'BBBBBB',
        },
        new Syllabus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Syllabus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomSyllabus: 'BBBBBB',
          syllabus: 'BBBBBB',
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

    it('should delete a Syllabus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSyllabusToCollectionIfMissing', () => {
      it('should add a Syllabus to an empty array', () => {
        const syllabus: ISyllabus = { id: 123 };
        expectedResult = service.addSyllabusToCollectionIfMissing([], syllabus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(syllabus);
      });

      it('should not add a Syllabus to an array that contains it', () => {
        const syllabus: ISyllabus = { id: 123 };
        const syllabusCollection: ISyllabus[] = [
          {
            ...syllabus,
          },
          { id: 456 },
        ];
        expectedResult = service.addSyllabusToCollectionIfMissing(syllabusCollection, syllabus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Syllabus to an array that doesn't contain it", () => {
        const syllabus: ISyllabus = { id: 123 };
        const syllabusCollection: ISyllabus[] = [{ id: 456 }];
        expectedResult = service.addSyllabusToCollectionIfMissing(syllabusCollection, syllabus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(syllabus);
      });

      it('should add only unique Syllabus to an array', () => {
        const syllabusArray: ISyllabus[] = [{ id: 123 }, { id: 456 }, { id: 95425 }];
        const syllabusCollection: ISyllabus[] = [{ id: 123 }];
        expectedResult = service.addSyllabusToCollectionIfMissing(syllabusCollection, ...syllabusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const syllabus: ISyllabus = { id: 123 };
        const syllabus2: ISyllabus = { id: 456 };
        expectedResult = service.addSyllabusToCollectionIfMissing([], syllabus, syllabus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(syllabus);
        expect(expectedResult).toContain(syllabus2);
      });

      it('should accept null and undefined values', () => {
        const syllabus: ISyllabus = { id: 123 };
        expectedResult = service.addSyllabusToCollectionIfMissing([], null, syllabus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(syllabus);
      });

      it('should return initial array if no Syllabus is added', () => {
        const syllabusCollection: ISyllabus[] = [{ id: 123 }];
        expectedResult = service.addSyllabusToCollectionIfMissing(syllabusCollection, undefined, null);
        expect(expectedResult).toEqual(syllabusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
