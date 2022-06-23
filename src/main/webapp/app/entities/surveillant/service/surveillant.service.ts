import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISurveillant, getSurveillantIdentifier } from '../surveillant.model';

export type EntityResponseType = HttpResponse<ISurveillant>;
export type EntityArrayResponseType = HttpResponse<ISurveillant[]>;

@Injectable({ providedIn: 'root' })
export class SurveillantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/surveillants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(surveillant: ISurveillant): Observable<EntityResponseType> {
    return this.http.post<ISurveillant>(this.resourceUrl, surveillant, { observe: 'response' });
  }

  update(surveillant: ISurveillant): Observable<EntityResponseType> {
    return this.http.put<ISurveillant>(`${this.resourceUrl}/${getSurveillantIdentifier(surveillant) as number}`, surveillant, {
      observe: 'response',
    });
  }

  partialUpdate(surveillant: ISurveillant): Observable<EntityResponseType> {
    return this.http.patch<ISurveillant>(`${this.resourceUrl}/${getSurveillantIdentifier(surveillant) as number}`, surveillant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISurveillant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISurveillant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSurveillantToCollectionIfMissing(
    surveillantCollection: ISurveillant[],
    ...surveillantsToCheck: (ISurveillant | null | undefined)[]
  ): ISurveillant[] {
    const surveillants: ISurveillant[] = surveillantsToCheck.filter(isPresent);
    if (surveillants.length > 0) {
      const surveillantCollectionIdentifiers = surveillantCollection.map(surveillantItem => getSurveillantIdentifier(surveillantItem)!);
      const surveillantsToAdd = surveillants.filter(surveillantItem => {
        const surveillantIdentifier = getSurveillantIdentifier(surveillantItem);
        if (surveillantIdentifier == null || surveillantCollectionIdentifiers.includes(surveillantIdentifier)) {
          return false;
        }
        surveillantCollectionIdentifiers.push(surveillantIdentifier);
        return true;
      });
      return [...surveillantsToAdd, ...surveillantCollection];
    }
    return surveillantCollection;
  }
}
