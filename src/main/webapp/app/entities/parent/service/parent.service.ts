import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParent, getParentIdentifier } from '../parent.model';

export type EntityResponseType = HttpResponse<IParent>;
export type EntityArrayResponseType = HttpResponse<IParent[]>;

@Injectable({ providedIn: 'root' })
export class ParentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(parent: IParent): Observable<EntityResponseType> {
    return this.http.post<IParent>(this.resourceUrl, parent, { observe: 'response' });
  }

  update(parent: IParent): Observable<EntityResponseType> {
    return this.http.put<IParent>(`${this.resourceUrl}/${getParentIdentifier(parent) as number}`, parent, { observe: 'response' });
  }

  partialUpdate(parent: IParent): Observable<EntityResponseType> {
    return this.http.patch<IParent>(`${this.resourceUrl}/${getParentIdentifier(parent) as number}`, parent, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParentToCollectionIfMissing(parentCollection: IParent[], ...parentsToCheck: (IParent | null | undefined)[]): IParent[] {
    const parents: IParent[] = parentsToCheck.filter(isPresent);
    if (parents.length > 0) {
      const parentCollectionIdentifiers = parentCollection.map(parentItem => getParentIdentifier(parentItem)!);
      const parentsToAdd = parents.filter(parentItem => {
        const parentIdentifier = getParentIdentifier(parentItem);
        if (parentIdentifier == null || parentCollectionIdentifiers.includes(parentIdentifier)) {
          return false;
        }
        parentCollectionIdentifiers.push(parentIdentifier);
        return true;
      });
      return [...parentsToAdd, ...parentCollection];
    }
    return parentCollection;
  }
}
