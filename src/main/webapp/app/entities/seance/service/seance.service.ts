import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISeance, getSeanceIdentifier } from '../seance.model';

export type EntityResponseType = HttpResponse<ISeance>;
export type EntityArrayResponseType = HttpResponse<ISeance[]>;

@Injectable({ providedIn: 'root' })
export class SeanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/seances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(seance: ISeance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seance);
    return this.http
      .post<ISeance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(seance: ISeance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seance);
    return this.http
      .put<ISeance>(`${this.resourceUrl}/${getSeanceIdentifier(seance) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(seance: ISeance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(seance);
    return this.http
      .patch<ISeance>(`${this.resourceUrl}/${getSeanceIdentifier(seance) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISeance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISeance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSeanceToCollectionIfMissing(seanceCollection: ISeance[], ...seancesToCheck: (ISeance | null | undefined)[]): ISeance[] {
    const seances: ISeance[] = seancesToCheck.filter(isPresent);
    if (seances.length > 0) {
      const seanceCollectionIdentifiers = seanceCollection.map(seanceItem => getSeanceIdentifier(seanceItem)!);
      const seancesToAdd = seances.filter(seanceItem => {
        const seanceIdentifier = getSeanceIdentifier(seanceItem);
        if (seanceIdentifier == null || seanceCollectionIdentifiers.includes(seanceIdentifier)) {
          return false;
        }
        seanceCollectionIdentifiers.push(seanceIdentifier);
        return true;
      });
      return [...seancesToAdd, ...seanceCollection];
    }
    return seanceCollection;
  }

  protected convertDateFromClient(seance: ISeance): ISeance {
    return Object.assign({}, seance, {
      dateSeance: seance.dateSeance?.isValid() ? seance.dateSeance.format(DATE_FORMAT) : undefined,
      dateDebut: seance.dateDebut?.isValid() ? seance.dateDebut.toJSON() : undefined,
      dateFin: seance.dateFin?.isValid() ? seance.dateFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateSeance = res.body.dateSeance ? dayjs(res.body.dateSeance) : undefined;
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((seance: ISeance) => {
        seance.dateSeance = seance.dateSeance ? dayjs(seance.dateSeance) : undefined;
        seance.dateDebut = seance.dateDebut ? dayjs(seance.dateDebut) : undefined;
        seance.dateFin = seance.dateFin ? dayjs(seance.dateFin) : undefined;
      });
    }
    return res;
  }
}
