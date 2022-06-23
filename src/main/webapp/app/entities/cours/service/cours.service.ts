import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICours, getCoursIdentifier } from '../cours.model';

export type EntityResponseType = HttpResponse<ICours>;
export type EntityArrayResponseType = HttpResponse<ICours[]>;

@Injectable({ providedIn: 'root' })
export class CoursService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cours');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cours: ICours): Observable<EntityResponseType> {
    return this.http.post<ICours>(this.resourceUrl, cours, { observe: 'response' });
  }

  update(cours: ICours): Observable<EntityResponseType> {
    return this.http.put<ICours>(`${this.resourceUrl}/${getCoursIdentifier(cours) as number}`, cours, { observe: 'response' });
  }

  partialUpdate(cours: ICours): Observable<EntityResponseType> {
    return this.http.patch<ICours>(`${this.resourceUrl}/${getCoursIdentifier(cours) as number}`, cours, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICours>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICours[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCoursToCollectionIfMissing(coursCollection: ICours[], ...coursToCheck: (ICours | null | undefined)[]): ICours[] {
    const cours: ICours[] = coursToCheck.filter(isPresent);
    if (cours.length > 0) {
      const coursCollectionIdentifiers = coursCollection.map(coursItem => getCoursIdentifier(coursItem)!);
      const coursToAdd = cours.filter(coursItem => {
        const coursIdentifier = getCoursIdentifier(coursItem);
        if (coursIdentifier == null || coursCollectionIdentifiers.includes(coursIdentifier)) {
          return false;
        }
        coursCollectionIdentifiers.push(coursIdentifier);
        return true;
      });
      return [...coursToAdd, ...coursCollection];
    }
    return coursCollection;
  }
}
