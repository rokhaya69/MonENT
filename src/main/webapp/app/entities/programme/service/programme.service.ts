import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProgramme, getProgrammeIdentifier } from '../programme.model';

export type EntityResponseType = HttpResponse<IProgramme>;
export type EntityArrayResponseType = HttpResponse<IProgramme[]>;

@Injectable({ providedIn: 'root' })
export class ProgrammeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/programmes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(programme: IProgramme): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programme);
    return this.http
      .post<IProgramme>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(programme: IProgramme): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programme);
    return this.http
      .put<IProgramme>(`${this.resourceUrl}/${getProgrammeIdentifier(programme) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(programme: IProgramme): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programme);
    return this.http
      .patch<IProgramme>(`${this.resourceUrl}/${getProgrammeIdentifier(programme) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProgramme>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProgramme[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProgrammeToCollectionIfMissing(
    programmeCollection: IProgramme[],
    ...programmesToCheck: (IProgramme | null | undefined)[]
  ): IProgramme[] {
    const programmes: IProgramme[] = programmesToCheck.filter(isPresent);
    if (programmes.length > 0) {
      const programmeCollectionIdentifiers = programmeCollection.map(programmeItem => getProgrammeIdentifier(programmeItem)!);
      const programmesToAdd = programmes.filter(programmeItem => {
        const programmeIdentifier = getProgrammeIdentifier(programmeItem);
        if (programmeIdentifier == null || programmeCollectionIdentifiers.includes(programmeIdentifier)) {
          return false;
        }
        programmeCollectionIdentifiers.push(programmeIdentifier);
        return true;
      });
      return [...programmesToAdd, ...programmeCollection];
    }
    return programmeCollection;
  }

  protected convertDateFromClient(programme: IProgramme): IProgramme {
    return Object.assign({}, programme, {
      annee: programme.annee?.isValid() ? programme.annee.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.annee = res.body.annee ? dayjs(res.body.annee) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((programme: IProgramme) => {
        programme.annee = programme.annee ? dayjs(programme.annee) : undefined;
      });
    }
    return res;
  }
}
