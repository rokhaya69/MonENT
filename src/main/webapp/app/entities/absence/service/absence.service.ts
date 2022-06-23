import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAbsence, getAbsenceIdentifier } from '../absence.model';

export type EntityResponseType = HttpResponse<IAbsence>;
export type EntityArrayResponseType = HttpResponse<IAbsence[]>;

@Injectable({ providedIn: 'root' })
export class AbsenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/absences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(absence: IAbsence): Observable<EntityResponseType> {
    return this.http.post<IAbsence>(this.resourceUrl, absence, { observe: 'response' });
  }

  update(absence: IAbsence): Observable<EntityResponseType> {
    return this.http.put<IAbsence>(`${this.resourceUrl}/${getAbsenceIdentifier(absence) as number}`, absence, { observe: 'response' });
  }

  partialUpdate(absence: IAbsence): Observable<EntityResponseType> {
    return this.http.patch<IAbsence>(`${this.resourceUrl}/${getAbsenceIdentifier(absence) as number}`, absence, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAbsence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAbsence[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAbsenceToCollectionIfMissing(absenceCollection: IAbsence[], ...absencesToCheck: (IAbsence | null | undefined)[]): IAbsence[] {
    const absences: IAbsence[] = absencesToCheck.filter(isPresent);
    if (absences.length > 0) {
      const absenceCollectionIdentifiers = absenceCollection.map(absenceItem => getAbsenceIdentifier(absenceItem)!);
      const absencesToAdd = absences.filter(absenceItem => {
        const absenceIdentifier = getAbsenceIdentifier(absenceItem);
        if (absenceIdentifier == null || absenceCollectionIdentifiers.includes(absenceIdentifier)) {
          return false;
        }
        absenceCollectionIdentifiers.push(absenceIdentifier);
        return true;
      });
      return [...absencesToAdd, ...absenceCollection];
    }
    return absenceCollection;
  }
}
