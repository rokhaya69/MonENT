import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMefpai, getMefpaiIdentifier } from '../mefpai.model';

export type EntityResponseType = HttpResponse<IMefpai>;
export type EntityArrayResponseType = HttpResponse<IMefpai[]>;

@Injectable({ providedIn: 'root' })
export class MefpaiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mefpais');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mefpai: IMefpai): Observable<EntityResponseType> {
    return this.http.post<IMefpai>(this.resourceUrl, mefpai, { observe: 'response' });
  }

  update(mefpai: IMefpai): Observable<EntityResponseType> {
    return this.http.put<IMefpai>(`${this.resourceUrl}/${getMefpaiIdentifier(mefpai) as number}`, mefpai, { observe: 'response' });
  }

  partialUpdate(mefpai: IMefpai): Observable<EntityResponseType> {
    return this.http.patch<IMefpai>(`${this.resourceUrl}/${getMefpaiIdentifier(mefpai) as number}`, mefpai, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMefpai>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMefpai[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMefpaiToCollectionIfMissing(mefpaiCollection: IMefpai[], ...mefpaisToCheck: (IMefpai | null | undefined)[]): IMefpai[] {
    const mefpais: IMefpai[] = mefpaisToCheck.filter(isPresent);
    if (mefpais.length > 0) {
      const mefpaiCollectionIdentifiers = mefpaiCollection.map(mefpaiItem => getMefpaiIdentifier(mefpaiItem)!);
      const mefpaisToAdd = mefpais.filter(mefpaiItem => {
        const mefpaiIdentifier = getMefpaiIdentifier(mefpaiItem);
        if (mefpaiIdentifier == null || mefpaiCollectionIdentifiers.includes(mefpaiIdentifier)) {
          return false;
        }
        mefpaiCollectionIdentifiers.push(mefpaiIdentifier);
        return true;
      });
      return [...mefpaisToAdd, ...mefpaiCollection];
    }
    return mefpaiCollection;
  }
}
