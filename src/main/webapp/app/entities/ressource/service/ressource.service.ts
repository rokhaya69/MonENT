import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRessource, getRessourceIdentifier } from '../ressource.model';

export type EntityResponseType = HttpResponse<IRessource>;
export type EntityArrayResponseType = HttpResponse<IRessource[]>;

@Injectable({ providedIn: 'root' })
export class RessourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ressources');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ressource: IRessource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ressource);
    return this.http
      .post<IRessource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ressource: IRessource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ressource);
    return this.http
      .put<IRessource>(`${this.resourceUrl}/${getRessourceIdentifier(ressource) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(ressource: IRessource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ressource);
    return this.http
      .patch<IRessource>(`${this.resourceUrl}/${getRessourceIdentifier(ressource) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRessource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRessource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRessourceToCollectionIfMissing(
    ressourceCollection: IRessource[],
    ...ressourcesToCheck: (IRessource | null | undefined)[]
  ): IRessource[] {
    const ressources: IRessource[] = ressourcesToCheck.filter(isPresent);
    if (ressources.length > 0) {
      const ressourceCollectionIdentifiers = ressourceCollection.map(ressourceItem => getRessourceIdentifier(ressourceItem)!);
      const ressourcesToAdd = ressources.filter(ressourceItem => {
        const ressourceIdentifier = getRessourceIdentifier(ressourceItem);
        if (ressourceIdentifier == null || ressourceCollectionIdentifiers.includes(ressourceIdentifier)) {
          return false;
        }
        ressourceCollectionIdentifiers.push(ressourceIdentifier);
        return true;
      });
      return [...ressourcesToAdd, ...ressourceCollection];
    }
    return ressourceCollection;
  }

  protected convertDateFromClient(ressource: IRessource): IRessource {
    return Object.assign({}, ressource, {
      dateMise: ressource.dateMise?.isValid() ? ressource.dateMise.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateMise = res.body.dateMise ? dayjs(res.body.dateMise) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ressource: IRessource) => {
        ressource.dateMise = ressource.dateMise ? dayjs(ressource.dateMise) : undefined;
      });
    }
    return res;
  }
}
