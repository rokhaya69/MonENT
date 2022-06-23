import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvaluation, getEvaluationIdentifier } from '../evaluation.model';

export type EntityResponseType = HttpResponse<IEvaluation>;
export type EntityArrayResponseType = HttpResponse<IEvaluation[]>;

@Injectable({ providedIn: 'root' })
export class EvaluationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaluations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evaluation: IEvaluation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluation);
    return this.http
      .post<IEvaluation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(evaluation: IEvaluation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluation);
    return this.http
      .put<IEvaluation>(`${this.resourceUrl}/${getEvaluationIdentifier(evaluation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(evaluation: IEvaluation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluation);
    return this.http
      .patch<IEvaluation>(`${this.resourceUrl}/${getEvaluationIdentifier(evaluation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvaluation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvaluation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEvaluationToCollectionIfMissing(
    evaluationCollection: IEvaluation[],
    ...evaluationsToCheck: (IEvaluation | null | undefined)[]
  ): IEvaluation[] {
    const evaluations: IEvaluation[] = evaluationsToCheck.filter(isPresent);
    if (evaluations.length > 0) {
      const evaluationCollectionIdentifiers = evaluationCollection.map(evaluationItem => getEvaluationIdentifier(evaluationItem)!);
      const evaluationsToAdd = evaluations.filter(evaluationItem => {
        const evaluationIdentifier = getEvaluationIdentifier(evaluationItem);
        if (evaluationIdentifier == null || evaluationCollectionIdentifiers.includes(evaluationIdentifier)) {
          return false;
        }
        evaluationCollectionIdentifiers.push(evaluationIdentifier);
        return true;
      });
      return [...evaluationsToAdd, ...evaluationCollection];
    }
    return evaluationCollection;
  }

  protected convertDateFromClient(evaluation: IEvaluation): IEvaluation {
    return Object.assign({}, evaluation, {
      dateEva: evaluation.dateEva?.isValid() ? evaluation.dateEva.format(DATE_FORMAT) : undefined,
      heureDebEva: evaluation.heureDebEva?.isValid() ? evaluation.heureDebEva.toJSON() : undefined,
      heureFinEva: evaluation.heureFinEva?.isValid() ? evaluation.heureFinEva.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateEva = res.body.dateEva ? dayjs(res.body.dateEva) : undefined;
      res.body.heureDebEva = res.body.heureDebEva ? dayjs(res.body.heureDebEva) : undefined;
      res.body.heureFinEva = res.body.heureFinEva ? dayjs(res.body.heureFinEva) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((evaluation: IEvaluation) => {
        evaluation.dateEva = evaluation.dateEva ? dayjs(evaluation.dateEva) : undefined;
        evaluation.heureDebEva = evaluation.heureDebEva ? dayjs(evaluation.heureDebEva) : undefined;
        evaluation.heureFinEva = evaluation.heureFinEva ? dayjs(evaluation.heureFinEva) : undefined;
      });
    }
    return res;
  }
}
