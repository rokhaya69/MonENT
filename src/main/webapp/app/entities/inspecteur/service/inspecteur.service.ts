import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspecteur, getInspecteurIdentifier } from '../inspecteur.model';

export type EntityResponseType = HttpResponse<IInspecteur>;
export type EntityArrayResponseType = HttpResponse<IInspecteur[]>;

@Injectable({ providedIn: 'root' })
export class InspecteurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspecteurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspecteur: IInspecteur): Observable<EntityResponseType> {
    return this.http.post<IInspecteur>(this.resourceUrl, inspecteur, { observe: 'response' });
  }

  update(inspecteur: IInspecteur): Observable<EntityResponseType> {
    return this.http.put<IInspecteur>(`${this.resourceUrl}/${getInspecteurIdentifier(inspecteur) as number}`, inspecteur, {
      observe: 'response',
    });
  }

  partialUpdate(inspecteur: IInspecteur): Observable<EntityResponseType> {
    return this.http.patch<IInspecteur>(`${this.resourceUrl}/${getInspecteurIdentifier(inspecteur) as number}`, inspecteur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInspecteur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspecteur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInspecteurToCollectionIfMissing(
    inspecteurCollection: IInspecteur[],
    ...inspecteursToCheck: (IInspecteur | null | undefined)[]
  ): IInspecteur[] {
    const inspecteurs: IInspecteur[] = inspecteursToCheck.filter(isPresent);
    if (inspecteurs.length > 0) {
      const inspecteurCollectionIdentifiers = inspecteurCollection.map(inspecteurItem => getInspecteurIdentifier(inspecteurItem)!);
      const inspecteursToAdd = inspecteurs.filter(inspecteurItem => {
        const inspecteurIdentifier = getInspecteurIdentifier(inspecteurItem);
        if (inspecteurIdentifier == null || inspecteurCollectionIdentifiers.includes(inspecteurIdentifier)) {
          return false;
        }
        inspecteurCollectionIdentifiers.push(inspecteurIdentifier);
        return true;
      });
      return [...inspecteursToAdd, ...inspecteurCollection];
    }
    return inspecteurCollection;
  }
}
