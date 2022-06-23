import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContenu, getContenuIdentifier } from '../contenu.model';

export type EntityResponseType = HttpResponse<IContenu>;
export type EntityArrayResponseType = HttpResponse<IContenu[]>;

@Injectable({ providedIn: 'root' })
export class ContenuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contenus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contenu: IContenu): Observable<EntityResponseType> {
    return this.http.post<IContenu>(this.resourceUrl, contenu, { observe: 'response' });
  }

  update(contenu: IContenu): Observable<EntityResponseType> {
    return this.http.put<IContenu>(`${this.resourceUrl}/${getContenuIdentifier(contenu) as number}`, contenu, { observe: 'response' });
  }

  partialUpdate(contenu: IContenu): Observable<EntityResponseType> {
    return this.http.patch<IContenu>(`${this.resourceUrl}/${getContenuIdentifier(contenu) as number}`, contenu, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContenu>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContenu[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContenuToCollectionIfMissing(contenuCollection: IContenu[], ...contenusToCheck: (IContenu | null | undefined)[]): IContenu[] {
    const contenus: IContenu[] = contenusToCheck.filter(isPresent);
    if (contenus.length > 0) {
      const contenuCollectionIdentifiers = contenuCollection.map(contenuItem => getContenuIdentifier(contenuItem)!);
      const contenusToAdd = contenus.filter(contenuItem => {
        const contenuIdentifier = getContenuIdentifier(contenuItem);
        if (contenuIdentifier == null || contenuCollectionIdentifiers.includes(contenuIdentifier)) {
          return false;
        }
        contenuCollectionIdentifiers.push(contenuIdentifier);
        return true;
      });
      return [...contenusToAdd, ...contenuCollection];
    }
    return contenuCollection;
  }
}
