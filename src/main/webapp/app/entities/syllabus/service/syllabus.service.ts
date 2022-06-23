import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISyllabus, getSyllabusIdentifier } from '../syllabus.model';

export type EntityResponseType = HttpResponse<ISyllabus>;
export type EntityArrayResponseType = HttpResponse<ISyllabus[]>;

@Injectable({ providedIn: 'root' })
export class SyllabusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/syllabi');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(syllabus: ISyllabus): Observable<EntityResponseType> {
    return this.http.post<ISyllabus>(this.resourceUrl, syllabus, { observe: 'response' });
  }

  update(syllabus: ISyllabus): Observable<EntityResponseType> {
    return this.http.put<ISyllabus>(`${this.resourceUrl}/${getSyllabusIdentifier(syllabus) as number}`, syllabus, { observe: 'response' });
  }

  partialUpdate(syllabus: ISyllabus): Observable<EntityResponseType> {
    return this.http.patch<ISyllabus>(`${this.resourceUrl}/${getSyllabusIdentifier(syllabus) as number}`, syllabus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISyllabus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISyllabus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSyllabusToCollectionIfMissing(syllabusCollection: ISyllabus[], ...syllabiToCheck: (ISyllabus | null | undefined)[]): ISyllabus[] {
    const syllabi: ISyllabus[] = syllabiToCheck.filter(isPresent);
    if (syllabi.length > 0) {
      const syllabusCollectionIdentifiers = syllabusCollection.map(syllabusItem => getSyllabusIdentifier(syllabusItem)!);
      const syllabiToAdd = syllabi.filter(syllabusItem => {
        const syllabusIdentifier = getSyllabusIdentifier(syllabusItem);
        if (syllabusIdentifier == null || syllabusCollectionIdentifiers.includes(syllabusIdentifier)) {
          return false;
        }
        syllabusCollectionIdentifiers.push(syllabusIdentifier);
        return true;
      });
      return [...syllabiToAdd, ...syllabusCollection];
    }
    return syllabusCollection;
  }
}
