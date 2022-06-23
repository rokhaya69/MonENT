import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfesseur, getProfesseurIdentifier } from '../professeur.model';

export type EntityResponseType = HttpResponse<IProfesseur>;
export type EntityArrayResponseType = HttpResponse<IProfesseur[]>;

@Injectable({ providedIn: 'root' })
export class ProfesseurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/professeurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(professeur: IProfesseur): Observable<EntityResponseType> {
    return this.http.post<IProfesseur>(this.resourceUrl, professeur, { observe: 'response' });
  }

  update(professeur: IProfesseur): Observable<EntityResponseType> {
    return this.http.put<IProfesseur>(`${this.resourceUrl}/${getProfesseurIdentifier(professeur) as number}`, professeur, {
      observe: 'response',
    });
  }

  partialUpdate(professeur: IProfesseur): Observable<EntityResponseType> {
    return this.http.patch<IProfesseur>(`${this.resourceUrl}/${getProfesseurIdentifier(professeur) as number}`, professeur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfesseur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfesseur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProfesseurToCollectionIfMissing(
    professeurCollection: IProfesseur[],
    ...professeursToCheck: (IProfesseur | null | undefined)[]
  ): IProfesseur[] {
    const professeurs: IProfesseur[] = professeursToCheck.filter(isPresent);
    if (professeurs.length > 0) {
      const professeurCollectionIdentifiers = professeurCollection.map(professeurItem => getProfesseurIdentifier(professeurItem)!);
      const professeursToAdd = professeurs.filter(professeurItem => {
        const professeurIdentifier = getProfesseurIdentifier(professeurItem);
        if (professeurIdentifier == null || professeurCollectionIdentifiers.includes(professeurIdentifier)) {
          return false;
        }
        professeurCollectionIdentifiers.push(professeurIdentifier);
        return true;
      });
      return [...professeursToAdd, ...professeurCollection];
    }
    return professeurCollection;
  }
}
